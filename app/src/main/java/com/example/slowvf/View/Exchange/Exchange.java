package com.example.slowvf.View.Exchange;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slowvf.Controller.Exchange.BluetoothController;
import com.example.slowvf.Model.BluetoothItem;
import com.example.slowvf.R;
import com.example.slowvf.View.Adapters.ExchangeAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.Getter;

public class Exchange extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;

    private final static String emptyList = "Cliquer sur le bouton scan pour chercher les appareils à proximité";
    private final static String nonEmptyList = "Choisir un appareil pour échanger vos données";

    private RecyclerView exchangeView;
    private ExchangeAdapter exchangeAdapter;
    private RecyclerView.LayoutManager exchangeLayoutManager;
    BluetoothController bluetoothController;

    private List<BluetoothItem> bluetoothDevices = new ArrayList<>();

    @Getter
    private ProgressDialog progressDialog;
    private TextView instructionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bluetoothController = new BluetoothController(this);

        Intent discoverableIntent = new Intent(bluetoothController.getBluetoothAdapter().ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(bluetoothController.getBluetoothAdapter().EXTRA_DISCOVERABLE_DURATION, 0);
        startActivity(discoverableIntent);

        setContentView(R.layout.activity_exchange);

        exchangeView = findViewById(R.id.exchangeView);
        exchangeLayoutManager = new LinearLayoutManager(this);
        exchangeAdapter = new ExchangeAdapter(bluetoothDevices, bluetoothController);
        exchangeView.setLayoutManager(exchangeLayoutManager);
        exchangeView.setAdapter(exchangeAdapter);

        instructionView = findViewById(R.id.instruction_view);
        Button scanButton = findViewById(R.id.scan_button);

        // Hide the RecyclerView and show the empty view initially
        exchangeView.setVisibility(View.GONE);
        instructionView.setText(emptyList);


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothController.checkBluetoothPermission()) {
                    // Show a waiting dialog
                    progressDialog = new ProgressDialog(Exchange.this);
                    progressDialog.setMessage("Waiting for Bluetooth devices...");
                    progressDialog.setCancelable(false);
                     progressDialog.show();

                    // Clear the list of Bluetooth devices
                    bluetoothDevices.clear();
                    bluetoothController.startDiscovery();



                    // Register a BroadcastReceiver to listen for scan results
                    BroadcastReceiver receiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            String action = intent.getAction();
                            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                                if (!TextUtils.isEmpty(device.getName())) {
                                    boolean isDuplicate = false;
                                    for (BluetoothItem bluetoothItem : bluetoothDevices) {
                                        if (bluetoothItem.getMacAddress().equals(device.getAddress())) {
                                            isDuplicate = true;
                                            break;
                                        }
                                    }
                                    if (!isDuplicate) {
                                        bluetoothDevices.add(new BluetoothItem(device.getName(), device.getAddress(), device));
                                    }
                                }
                            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                                bluetoothController.stopDiscovery();
                                refreshBluetoothList();
                                // Dismiss the waiting dialog
                                progressDialog.dismiss();
                                // Unregister the BroadcastReceiver
                                unregisterReceiver(this);
                            }
                        }
                    };

                    // Register the BroadcastReceiver for scan results
                    IntentFilter filter = new IntentFilter();
                    filter.addAction(BluetoothDevice.ACTION_FOUND);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    registerReceiver(receiver, filter);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Bluetooth has been enabled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Bluetooth has been disabled", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }





    public void refreshBluetoothList(){
        if (bluetoothDevices.isEmpty()) {
            // If the list is empty, show the empty view and hide the RecyclerView
            instructionView.setVisibility(View.VISIBLE);
            exchangeView.setVisibility(View.GONE);
        } else {
            // If the list has devices, hide the empty view and show the RecyclerView
            instructionView.setText(nonEmptyList);
            exchangeView.setVisibility(View.VISIBLE);
        }
        exchangeAdapter.notifyDataSetChanged();
    }


    public void openSynchronization(BluetoothItem bluetoothItem) {
        Intent intent = new Intent(this, Synchronization.class);
        intent.putExtra("bluetoothItem", bluetoothItem);
        intent.putExtra("bluetoothController", bluetoothController);
        startActivity(intent);
    }

    public void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Exchange.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}