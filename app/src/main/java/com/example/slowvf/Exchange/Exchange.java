package com.example.slowvf.Exchange;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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

import com.example.slowvf.Exchange.BluetoothItem;
import com.example.slowvf.Exchange.ExchangeAdapter;
import com.example.slowvf.R;

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

    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Get the Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check if Bluetooth is supported on the device
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Check if Bluetooth is enabled on the device
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Check if the app has location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a waiting dialog
                progressDialog = new ProgressDialog(Exchange.this);
                progressDialog.setMessage("Waiting for Bluetooth devices...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                // Clear the list of Bluetooth devices
                bluetoothDevices.clear();

                // Scan for nearby Bluetooth devices
                if (ActivityCompat.checkSelfPermission(Exchange.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                for (BluetoothDevice device : pairedDevices) {
                    bluetoothDevices.add(new BluetoothItem(device.getName(), device.getAddress()));
                }

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

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}