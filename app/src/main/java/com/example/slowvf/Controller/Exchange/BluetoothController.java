package com.example.slowvf.Controller.Exchange;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.slowvf.Dao.Impl.ExchangeDaoImpl;
import com.example.slowvf.Model.BluetoothItem;
import com.example.slowvf.Model.ExchangeCounter;
import com.example.slowvf.Model.MessageEchange;
import com.example.slowvf.Model.ReceivedMessage;
import com.example.slowvf.Model.SentMessage;
import com.example.slowvf.View.Exchange.Exchange;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.Data;

@Data
public class BluetoothController implements Serializable {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;
    private static final long TIMEOUT_DELAY_MS = 10000; // 10 seconds
    private static final String REQUEST_MESSAGE = "SLOW_CHAT_CONNECTION_REQUEST";
    private static final String ACCEPT_MESSAGE = "SLOW_CHAT_CONNECTION_ACCEPT";
    private static final String REFUSE_MESSAGE = "SLOW_CHAT_CONNECTION_REFUSE";
    private static final UUID APP_UUID = UUID.fromString("688575AD-9126-4F1C-A82A-495DAE7D5D52");
    private static final String serviceName = "FastSlowChat";
    private static final String TAG = "ExchangeBt";
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private BluetoothSocket socketAccepte;
    private BluetoothServerSocket serverSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Handler handler;
    private Exchange activity;
    ExchangeDaoImpl exchangeDao = new ExchangeDaoImpl();
    private Runnable timeoutRunnable;
    private Thread serverThread;
    private volatile boolean isTimeoutExpired = false;
    private ExchangeCounter counter;

    public BluetoothController() {
        if(bluetoothAdapter == null) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        handler = new Handler();
        timeoutRunnable = new Runnable() {
            @Override
            public void run() {

                // Handle the timeout event
                // For example, show a pop-up indicating that the other device didn't respond
                AlertDialog.Builder builder = new AlertDialog.Builder(activity.getContext());
                builder.setTitle("Timeout");
                builder.setMessage("The other device did not respond in time.");
                builder.setPositiveButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                Thread.currentThread().interrupt();
                isTimeoutExpired = true;
            }
        };

        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Log.d(TAG, "On créée le serveur socket...");
                    serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(serviceName, APP_UUID);
                } catch (Exception e) {
                    Log.e(TAG, "Echec de la création du serveur socket.");
                    e.printStackTrace();
                }
                while(!serverThread.isInterrupted())
                {
                    try {
                        Log.d(TAG, "Le serveur socket attend une connexion...");
                        // Attendre une connexion entrante (cette opération bloque l'exécution)
                        socketAccepte = serverSocket.accept();
                        Log.d(TAG, "Connexion acceptée !");

                        device = socketAccepte.getRemoteDevice();

                        InputStream inputStream = socketAccepte.getInputStream();
                        socketAccepte.getOutputStream().flush();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                        // Lecture du message reçu
                        String receivedMessage = bufferedReader.readLine();
                        Log.d(TAG, "Message reçu : " + receivedMessage);

                        activity.requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (receivedMessage.contains(REQUEST_MESSAGE)){
                                    handler.removeCallbacks(timeoutRunnable);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity.getContext());
                                    builder.setTitle("Demande de connexion reçue");
                                    builder.setMessage("Acceptez vous la demande d'échange avec l'appareil " + device.getName() + "?");

                                    builder.setPositiveButton("Accepter", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //activity.openSynchronization(new BluetoothItem(device.getName(), device.getAddress(),device));
                                            sendMessage(ACCEPT_MESSAGE,socketAccepte);

                                            // Partie du code où on fait de la sync
                                            Log.d(TAG, "Ouverture activié synchronisation...");
                                            activity.openSynchronization(new BluetoothItem(device.getName(), device.getAddress()));
                                            try {
                                                secondaryConnection(socketAccepte);
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }

                                            // fermeture du socket
                                            try {
                                                Log.d(TAG, "Interruption socket connected dans le WHILE...");
                                                socketAccepte.close();
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                                    builder.setNegativeButton("Refuser", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            sendMessage(REFUSE_MESSAGE,socketAccepte);
                                            // fermeture du socket
                                            try {
                                                Log.d(TAG, "Interruption socket connected dans le WHILE...");
                                                socketAccepte.close();
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });
        serverThread.start();
        Log.d(TAG,"On start le thread serverSocket...");
    }

    public BluetoothController(Exchange activity) {
        this();
        this.activity = activity;
    }
    public void startDiscovery() {
        if (bluetoothAdapter == null) {
            activity.showDialog("No Bluetooth", "Bluetooth is not supported on this device. This function is not available");
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                activity.showDialog("Bluetooth disabeled", "Bluetooth is not enabled. Please enable it");
            } else {
                if (checkBluetoothPermission()) {
                    // Get nearby Bluetooth devices
                    Set<BluetoothDevice> nearbyDevices = new HashSet<>();

                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }
                    bluetoothAdapter.startDiscovery();
                }
            }
        }
    }

    public void stopDiscovery() {
        bluetoothAdapter.cancelDiscovery();
    }

    public static boolean checkBluetoothPermission(Exchange activity) {
        if (ContextCompat.checkSelfPermission(activity.getContext(), Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {
            // Bluetooth permission is not granted, request it
            activity.requestPermissions(new String[]{Manifest.permission.BLUETOOTH}, REQUEST_ENABLE_BT);

            return false; // Return here to prevent the scan from starting until the user grants the permission
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
            if (ContextCompat.checkSelfPermission(activity.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Location permission is not granted, request it
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                return false; // Return here to prevent the scan from starting until the user grants the permission
            } else if (ContextCompat.checkSelfPermission(activity.getContext(), Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                // Location permission is not granted, request it
                activity.requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_ENABLE_BT);
                return false; // Return here to prevent the scan from starting until the user grants the permission
            } else if (ContextCompat.checkSelfPermission(activity.getContext(), Manifest.permission.BLUETOOTH_SCAN)
                    != PackageManager.PERMISSION_GRANTED) {
                // Location permission is not granted, request it
                activity.requestPermissions(new String[]{Manifest.permission.BLUETOOTH_SCAN}, REQUEST_ENABLE_BT);
                return false; // Return here to prevent the scan from starting until the user grants the permission
            }
        } else {
            if (ContextCompat.checkSelfPermission(activity.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Location permission is not granted, request it
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                return false; // Return here to prevent the scan from starting until the user grants the permission
            } else if (ContextCompat.checkSelfPermission(activity.getContext(), Manifest.permission.BLUETOOTH_ADMIN)
                    != PackageManager.PERMISSION_GRANTED) {
                // Location permission is not granted, request it
                activity.requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_ENABLE_BT);
                return false; // Return here to prevent the scan from starting until the user grants the permission
            }
        }
        return true;
    }
    public boolean checkBluetoothPermission() {
       return checkBluetoothPermission(activity);
    }

    public void connectToDevice(String deviceAddress) throws IOException {

        // Handle timeout expiration
        isTimeoutExpired = false;

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        bluetoothAdapter.cancelDiscovery();
        Log.d(TAG,"On essaie de se connecter à " + deviceAddress.toString() + " via UUID " + APP_UUID.toString());
        socket = null;
        try {
            // Start the timeout countdown
            handler.postDelayed(timeoutRunnable, TIMEOUT_DELAY_MS);
            socket = device.createInsecureRfcommSocketToServiceRecord(APP_UUID);
            socket.connect();

            handler.removeCallbacks(timeoutRunnable);

            sendMessage(REQUEST_MESSAGE,socket);
            String answer = waitForMessageAndReturn();

            if (isTimeoutExpired) {
                return; // Exit the method
            }

            if (answer == null)
            {
                activity.requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacks(timeoutRunnable);
                        Toast.makeText(activity.getContext(), "Connexion interrompue", Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (answer.contains(ACCEPT_MESSAGE)) {
                activity.requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacks(timeoutRunnable);


                        activity.openSynchronization(new BluetoothItem(device.getName(), device.getAddress()));
                        try {
                            primaryConnection(socket);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });

            } else if (answer.contains(REFUSE_MESSAGE)) {
                activity.requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacks(timeoutRunnable);
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity.getContext());
                        builder.setTitle("Connexion refusée");
                        builder.setMessage("L'appareil a refusé la connexion.");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur de connexion
        }
    }


    public String waitForMessageAndReturn() {

        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String message = bufferedReader.readLine();
            return message;
        } catch (IOException e) {
            Log.e(TAG,"Exception waitForMessage");
            e.printStackTrace();
        }

        return null;
    }

    public void sendMessage(String message,BluetoothSocket destinataire) {

        OutputStream outputStream;

        try {
            checkBluetoothPermission();
            outputStream = destinataire.getOutputStream();
            byte[] buffer = (message + "\n").getBytes();
            outputStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void primaryConnection(BluetoothSocket destinataire) throws IOException {
        counter = new ExchangeCounter();
        sendAllMessages(destinataire);
        List<MessageEchange> receivedMessages = startReceivingMessages(destinataire);
        onMessagesReceived(receivedMessages);
        closeExchangeSocket();
    }

    public void secondaryConnection(BluetoothSocket destinataire) throws IOException {
        counter = new ExchangeCounter();
        List<MessageEchange> receivedMessages = startReceivingMessages(destinataire);
        sendAllMessages(destinataire);
        onMessagesReceived(receivedMessages);
        closeExchangeSocket();
    }



    public List<MessageEchange>  startReceivingMessages(BluetoothSocket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        try {
            // Lire les données sérialisées du InputStream
            List<MessageEchange> receivedMessages = (List<MessageEchange>) objectInputStream.readObject();

            return receivedMessages;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void sendAllMessages(BluetoothSocket destinataire) throws IOException {
        List<MessageEchange> allMessages = exchangeDao.getExchangeMessages(activity.getContext());

        for (MessageEchange message : allMessages) {
            Log.d(TAG, "MESSAGES ENVOYES - id: " + message.getIdSender() + " contenu: " + message.getMessageText());
        }

        OutputStream outputStream = destinataire.getOutputStream();

        try {
            // Sérialiser l'ArrayList
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(allMessages);

            // Écrire les données sérialisées du ByteArrayOutputStream dans le OutputStream du BluetoothSocket
            byte[] serializedData = byteArrayOutputStream.toByteArray();
            outputStream.write(serializedData);
            outputStream.flush();

        } catch (IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onSendError();
                }
            });
            e.printStackTrace();
        }
    }

    public void onSendError() {
        Log.e(TAG,"Erreur lors de l'envoi des messages");
    }
    public void onReceiveError() {
        Log.e(TAG,"Erreur lors de l'envoi des messages");
    }


    public void closeSockets() {
       closeExchangeSocket();
       closeServerSocket();
    }
    public void closeServerSocket() {
        try {
            Log.d(TAG, "Interruption thread serveur...");
            serverThread.interrupt();
            if (socketAccepte != null) {
                Log.d(TAG, "Interruption socket connected...");
                socketAccepte.close();
            }
            Log.d(TAG, "Interruption socket serveur...");
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeExchangeSocket() {
        try {
            if (socket != null) {
                Log.d(TAG, "Interruption socket connexion...");
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onMessagesReceived(List<MessageEchange> messages) {
        String currentUser = activity.getUserId();
        for (MessageEchange message : messages) {
            if (message.getIdReceiver().equals( currentUser) ){
                if (!exchangeDao.messageExist(activity.getContext(), message)) {
                    counter.setReceivedMessages(counter.getReceivedMessages() + 1);
                    LocalDateTime now = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String formattedDateTime = now.format(formatter);
                        message.setDateReceived(formattedDateTime);
                        exchangeDao.addReceivedMessage(activity.getContext(), new ReceivedMessage(message.getIdSender(),
                                message.getMessageText(), message.getDateWriting(), message.getDateReceived()));
                        exchangeDao.addUpdateExchangeMessage(activity.getContext(), message);
                    }

                }
            } else if (message.getIdSender().equals(currentUser) ){
                if (message.getDateReceived() != null) {
                    if (exchangeDao.messageExist(activity.getContext(), message)) {
                        exchangeDao.updateSentMessage(activity.getContext(), new SentMessage(message.getIdReceiver(),
                                message.getMessageText(), message.getDateWriting(), message.getDateReceived()));
                        exchangeDao.addUpdateExchangeMessage(activity.getContext(), message);
                    }
                }

            } else if (exchangeDao.messageExist(activity.getContext(), message)) {
                exchangeDao.addUpdateExchangeMessage(activity.getContext(), message);
            } else {
                counter.setReceivedMessages(counter.getReceivedMessages() + 1);
                exchangeDao.addUpdateExchangeMessage(activity.getContext(), message);
            }
        }
    }



}
