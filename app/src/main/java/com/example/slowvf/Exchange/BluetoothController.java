package com.example.slowvf.Exchange;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.os.Build;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class BluetoothController {
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1;
    private AppCompatActivity activity;

// l'utilisation de java.util.stream de java nécessite une version d'android api > 27
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ScanNearby(List<BluetoothItem> bluetoothDevices){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBluetoothPermission();

        if (bluetoothAdapter == null) {

        } else {
            if (!bluetoothAdapter.isEnabled()) {
                // Bluetooth is not enabled on the device
                // bluetoothAdapter.enable();
            } else {

                // Change the current device discovery name
                bluetoothAdapter.setName(getUserPseudo());
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300); // 5 minutes
                activity.startActivity(discoverableIntent);

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
                Function<BluetoothDevice, BluetoothItem> deviceToItem = device ->
                    new BluetoothItem(device.getName(), device.getAddress());
                bluetoothDevices = pairedDevices.stream().map(deviceToItem).collect(Collectors.toList());
            }
        }


    }



    public void checkBluetoothPermission(){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
            // Bluetooth permissions are granted
            // Start scanning for Bluetooth devices
        } else {
            // Bluetooth permissions are not granted, request them from the user
            ActivityCompat.requestPermissions(activity, new String[] {
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN
            }, REQUEST_BLUETOOTH_PERMISSIONS);
        }

    }



    public List<BluetoothItem> getDevices( ){


        List<BluetoothItem> nearbyItems = new ArrayList<>();

        return nearbyItems;

    }

    /**
     * To replace with the real method when the local file's DAO is officent
     * */
    private String getUserPseudo(){
        return "Pierre";
    }
}
