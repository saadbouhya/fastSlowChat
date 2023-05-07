package com.example.slowvf.Controller.Exchange;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothController {

    private static final UUID APP_UUID = UUID.randomUUID();
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Handler handler;
    private AppCompatActivity activity;

    public BluetoothController() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        handler = new Handler();
    }
    public BluetoothController(AppCompatActivity activity){
        this();
        this.activity = activity;
    }

    public void connectToDevice(String address) {
        device = bluetoothAdapter.getRemoteDevice(address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
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
        sendMessage("coucou");
        startReceivingMessages();

    }

    public void sendMessage(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outputStream.write(message.getBytes());
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
        }).start();
    }

    public void startReceivingMessages() {
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
                                onMessageReceived(message);
                            }
                        });
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
    public void onMessageReceived(String message) {
        // Do something with the received message
    }

    public void onReceiveError() {
        // Do something when there is an error receiving a message
    }
}
