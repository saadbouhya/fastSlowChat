package com.example.slowvf.Controller.Exchange;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.slowvf.Dao.Impl.ExchangeDaoImpl;
import com.example.slowvf.Model.BluetoothItem;
import com.example.slowvf.Model.MessageEchange;
import com.example.slowvf.View.Exchange.Exchange;
import com.example.slowvf.View.Exchange.Synchronization;

import java.io.IOException;
import java.io.InputStream;
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
    private static final String REQUEST_MESSAGE= "SLOW_CHAT_CONNECTION_REQUEST";
    private static final String ACCEPT_MESSAGE= "SLOW_CHAT_CONNECTION_ACCEPT";
    private static final String REFUSE_MESSAGE= "SLOW_CHAT_CONNECTION_REFUSE";



    private static final UUID APP_UUID = UUID.randomUUID();
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Handler handler;
    private Exchange activity;
    ExchangeDaoImpl exchangeDao = new ExchangeDaoImpl();
    private Runnable timeoutRunnable;
    private Thread messageThread;
    private boolean listeningForMessages = false;

    public BluetoothController() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        handler = new Handler();
        timeoutRunnable = new Runnable() {
            @Override
            public void run() {
                // Handle the timeout event
                // For example, show a pop-up indicating that the other device didn't respond
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Timeout");
                builder.setMessage("The other device did not respond in time.");
                builder.setPositiveButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };

        messageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (listeningForMessages) {
                    try {
                        byte[] buffer = new byte[1024];
                        int bytes = inputStream.read(buffer);
                        final String message = new String(buffer, 0, bytes);
                        final String deviceAddress = device.getAddress();
                        final String deviceName = device.getName();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (message.contains(REQUEST_MESSAGE)){
                                    handler.removeCallbacks(timeoutRunnable);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                    builder.setTitle("Connection Request");
                                    builder.setMessage("Do you accept the connection request from device " + deviceName + "?");
                                    builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            activity.openSynchronization(new BluetoothItem(deviceName, deviceAddress));
                                            secondaryConnection(deviceAddress);
                                        }
                                    });
                                    builder.setNegativeButton("Refuse", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            sendRefuseMessage(deviceAddress);
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();

                                } else if (message.contains(ACCEPT_MESSAGE)) {
                                    String deviceName = message.substring(ACCEPT_MESSAGE.length());
                                    activity.openSynchronization(new BluetoothItem(deviceName, deviceAddress));
                                    primalConnection(deviceAddress);

                                } else if (message.contains(REFUSE_MESSAGE)) {
                                    handler.removeCallbacks(timeoutRunnable);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                    builder.setTitle("Connection Refused");
                                    builder.setMessage("The device has refused the connection.");
                                    builder.setPositiveButton("OK", null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        startListeningForMessages();
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
                    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
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


    public void communConnection(String address){
        device = bluetoothAdapter.getRemoteDevice(address);
        stopListeningForMessages();
        checkBluetoothPermission();
        try {
            socket = device.createRfcommSocketToServiceRecord(APP_UUID);
            socket.connect();
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void primalConnection(String address){
        communConnection(address);
        sendAllMessages();
        List<MessageEchange> receivedMessages = startReceivingMessages();
        onMessagesReceived(receivedMessages);
        stopReceivingMessages();
        startListeningForMessages();

    }

    public void secondaryConnection(String address){
        communConnection(address);
        List<MessageEchange> receivedMessages = startReceivingMessages();
        sendAllMessages();
        onMessagesReceived(receivedMessages);
        stopReceivingMessages();
        startListeningForMessages();
    }

    public void sendAllMessages() {
        List<MessageEchange> exchangeMessages = exchangeDao.getExchangeMessages(activity);
        List<MessageEchange> localMessages = exchangeDao.getLocalMessages(activity);
        List<MessageEchange> allMessages = new ArrayList<>();
        allMessages.addAll(exchangeMessages);
        allMessages.addAll(localMessages);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (MessageEchange message : allMessages) {
                        outputStream.write(message.toString().getBytes());
                        outputStream.flush();
                    }
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
        }).start();
    }

    public List<MessageEchange> startReceivingMessages() {
        List<MessageEchange> receivedMessages = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[1024];
                int bytes;

                while (true) {
                    try {
                        bytes = inputStream.read(buffer);
                        final String message = new String(buffer, 0, bytes);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // do  nothing
                            }
                        });

                        // Parse the received message and add it to the list of received messages
                        MessageEchange receivedMessage = parseMessage(message);
                        receivedMessages.add(receivedMessage);
                    } catch (IOException e) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onReceiveError();
                            }
                        });
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }).start();

        return receivedMessages;
    }


    public void stopReceivingMessages() {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Override these methods to handle message sending events
    public void onSendError() {
        // Do something when there is an error sending a message
    }

    // Override these methods to handle message receiving events
    public void onMessagesReceived(List<MessageEchange> messages) {
        String currentUser = getUserId();
        for (MessageEchange message : messages) {
            if (message.getIdReceiver() == currentUser) {
                if (!exchangeDao.messageExist(activity, message, true)) {
                    LocalDateTime now = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String formattedDateTime = now.format(formatter);
                        message.setDateReceived(formattedDateTime);
                        exchangeDao.updateMessage(activity, message, true);
                    }

                }
            } else if (message.getIdSender() == currentUser) {
                if (message.getDateReceived() != null) {
                    if (exchangeDao.messageExist(activity, message, false)) {
                        exchangeDao.deleteMessage(activity, message, true);
                        exchangeDao.addMessage(activity, message, false);
                    }
                }

            } else if (exchangeDao.messageExist(activity, message, false)) {
                exchangeDao.updateMessage(activity, message, false);
            } else {
                exchangeDao.addMessage(activity, message, false);
            }
        }
    }

    public void onReceiveError() {
        // Do something when there is an error receiving a message
    }

    private MessageEchange parseMessage(String message) {
        // Split the message string into individual components
        String[] components = message.split(";");

        // Extract the relevant information from the components
        String idSender = components[0];
        String idReceiver = components[1];
        String dateWriting = components[2];
        String messageText = components[3];
        String dateReceived = components[4];

        // Create a new MessageEchange object with the extracted information
        MessageEchange parsedMessage = new MessageEchange(idSender, idReceiver, dateWriting, messageText, dateReceived);

        return parsedMessage;
    }


    public boolean checkBluetoothPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {
            // Bluetooth permission is not granted, request it
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.BLUETOOTH},
                    REQUEST_ENABLE_BT);
            return false; // Return here to prevent the scan from starting until the user grants the permission
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Location permission is not granted, request it
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
                return false; // Return here to prevent the scan from starting until the user grants the permission
            } else if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                // Location permission is not granted, request it
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                        REQUEST_ENABLE_BT);
                return false; // Return here to prevent the scan from starting until the user grants the permission
            } else if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN)
                    != PackageManager.PERMISSION_GRANTED) {
                // Location permission is not granted, request it
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.BLUETOOTH_SCAN},
                        REQUEST_ENABLE_BT);
                return false; // Return here to prevent the scan from starting until the user grants the permission
            }
        } else {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Location permission is not granted, request it
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
                return false; // Return here to prevent the scan from starting until the user grants the permission
            } else if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_ADMIN)
                    != PackageManager.PERMISSION_GRANTED) {
                // Location permission is not granted, request it
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                        REQUEST_ENABLE_BT);
                return false; // Return here to prevent the scan from starting until the user grants the permission
            }
        }
        return true;
    }


    public void sendHelloMessage(String deviceAddress) {
        // Find the BluetoothDevice object for the specified address
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);

        // Create and send the "SlowChatHello" message
        String helloMessage = "SlowChatHello";
        sendMessage(device, helloMessage);

        // Start the timeout countdown
        handler.postDelayed(timeoutRunnable, TIMEOUT_DELAY_MS);
    }

    public void sendMessage(BluetoothDevice device, String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    checkBluetoothPermission();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(APP_UUID);
                    socket.connect();
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(message.getBytes());
                    outputStream.flush();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void sendRefuseMessage(String deviceAddress) {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        String refuseMessage = "SLOW_CHAT_CONNECTION_REFUSE";
        sendMessage(device, refuseMessage);
    }


    private String getUserId(){
        return "id";
    }

    public void startListeningForMessages() {
        if (socket != null && inputStream != null) {
            listeningForMessages = true;
        }
        messageThread.start();
    }

    public void stopListeningForMessages() {
        listeningForMessages = false;
    }



}
