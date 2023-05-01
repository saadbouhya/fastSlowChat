package com.example.slowvf.View.Exchange;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Exchange extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;

    private final static String emptyList = "Cliquer sur le bouton scan pour chercher les appareils à proximité";
    private final static String nonEmptyList = "Choisir un appareil pour échanger vos données";

    private RecyclerView exchangeView;
    private ExchangeAdapter exchangeAdapter;
    private RecyclerView.LayoutManager exchangeLayoutManager;

    private List<BluetoothItem> bluetoothDevices = new ArrayList<>();

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BluetoothController bluetoothController = new BluetoothController(this);

        setContentView(R.layout.activity_exchange);

        exchangeView = findViewById(R.id.exchangeView);
        exchangeLayoutManager = new LinearLayoutManager(this);
        exchangeAdapter = new ExchangeAdapter(bluetoothDevices);
        exchangeView.setLayoutManager(exchangeLayoutManager);
        exchangeView.setAdapter(exchangeAdapter);

        TextView instructionView = findViewById(R.id.instruction_view);
        Button scanButton = findViewById(R.id.scan_button);

        // Hide the RecyclerView and show the empty view initially
        exchangeView.setVisibility(View.GONE);
        instructionView.setText(emptyList);



        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBluetoothPermission();
                // Show a waiting dialog
                progressDialog = new ProgressDialog(Exchange.this);
                progressDialog.setMessage("Waiting for Bluetooth devices...");
                progressDialog.setCancelable(false);
                // Clear the list of Bluetooth devices
                bluetoothDevices.clear();
                progressDialog.show();

                bluetoothController.ScanNearby(bluetoothDevices);



                // Update the UI
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

                // Dismiss the waiting dialog
                progressDialog.dismiss();
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Bluetooth permission granted, proceed with the operation
            } else {
                // Bluetooth permission denied, show an error message or handle accordingly
            }
        } else if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, proceed with the operation
            } else {
                // Location permission denied, show an error message or handle accordingly
            }
        }
    }


    public boolean checkBluetoothPermission() {
        if (ContextCompat.checkSelfPermission(Exchange.this, Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {
            // Bluetooth permission is not granted, request it
            ActivityCompat.requestPermissions(Exchange.this,
                    new String[]{Manifest.permission.BLUETOOTH},
                    REQUEST_ENABLE_BT);
            return false; // Return here to prevent the scan from starting until the user grants the permission
        } else if (ContextCompat.checkSelfPermission(Exchange.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Location permission is not granted, request it
            ActivityCompat.requestPermissions(Exchange.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            return false; // Return here to prevent the scan from starting until the user grants the permission
        } else {
            return true;
        }
    }




}