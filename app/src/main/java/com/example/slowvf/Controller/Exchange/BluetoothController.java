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
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.slowvf.Dao.Impl.ExchangeDaoImpl;
import com.example.slowvf.Model.BluetoothItem;
import com.example.slowvf.Model.MessageEchange;
import com.example.slowvf.View.Exchange.Exchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothController {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;

    private static final UUID APP_UUID = UUID.randomUUID();
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Handler handler;
    private AppCompatActivity activity;
    ExchangeDaoImpl exchangeDao = new ExchangeDaoImpl();

    public BluetoothController() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        handler = new Handler();
    }
    public BluetoothController(AppCompatActivity activity){
        this();
        this.activity = activity;
    }


    public void ScanNearby(List<BluetoothItem> bluetoothItems){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Bluetooth is not supported on this device. This function is not available")
                    .setCancelable(false)
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Bluetooth is not enabled. Please enable it.")
                        .setCancelable(false)
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                if (checkBluetoothPermission()) {
                        // Get nearby Bluetooth devices
                        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                    Set<BluetoothDevice> nearbyDevices = new HashSet<>();

                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }

                    bluetoothAdapter.startDiscovery();
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    activity.registerReceiver(new BroadcastReceiver() {
                        public void onReceive(Context context, Intent intent) {
                            String action = intent.getAction();
                            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                                if (!pairedDevices.contains(device)) {
                                    nearbyDevices.add(device);
                                }
                            }
                        }
                    }, filter);

                    bluetoothItems.clear();
                    for (BluetoothDevice nearbyDevice : nearbyDevices) {
                        bluetoothItems.add(new BluetoothItem(nearbyDevice.getName(), nearbyDevice.getAddress()));
                    }
                }
            }
        }


    }


    public void connectToDevice(String address) {
        device = bluetoothAdapter.getRemoteDevice(address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    socket = device.createRfcommSocketToServiceRecord(APP_UUID);
                    socket.connect();
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onDeviceConnected();
                        }
                    });

                    // Send all messages
                    sendAllMessages();

                    // Receive messages
                    List<MessageEchange> receivedMessages = startReceivingMessages();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onMessagesReceived(receivedMessages);
                        }
                    });
                } catch (IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onConnectionError();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }).start();
        sendAllMessages();
        startReceivingMessages();

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

    // Override these methods to handle connection events
    public void onDeviceConnected() {
        // Do something when the device is connected
    }

    public void onConnectionError() {
        // Do something when there is an error connecting to the device
    }

    // Override these methods to handle message sending events
    public void onSendError() {
        // Do something when there is an error sending a message
    }

    // Override these methods to handle message receiving events
    public void onMessagesReceived(List<MessageEchange> messages) {
        String currentUser = getUserId();
        for (MessageEchange message : messages){
            if(message.getIdReceiver() == currentUser){
                if (!exchangeDao.messageExist(activity,message, true)) {
                    LocalDateTime now = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String formattedDateTime = now.format(formatter);
                        message.setDateReceived(formattedDateTime);
                        exchangeDao.updateMessage(activity,message,true);
                    }

                }
            } else if (message.getIdSender() == currentUser) {
                if (message.getDateReceived() != null){
                    if (exchangeDao.messageExist(activity, message, false)){
                        exchangeDao.deleteMessage(activity, message, true);
                        exchangeDao.addMessage(activity, message, false);
                    }
                }

            } else if (exchangeDao.messageExist(activity, message, false)){
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
        } else if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Location permission is not granted, request it
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            return false; // Return here to prevent the scan from starting until the user grants the permission
        } else {
            return true;
        }
    }

    private String getUserId(){
        return "id";
    }

}
