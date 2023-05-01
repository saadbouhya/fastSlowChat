package com.example.slowvf.Exchange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.slowvf.R;

import java.util.ArrayList;
import java.util.List;

public class Exchange extends AppCompatActivity {
    private final static String emptyList = "Cliquer sur le bouton scan pour chercher les appareils à proximité";
    private final static String nonEmptyList ="Choisir un appareil pour échanger vos données";
    private RecyclerView exchangeView;
    private  RecyclerView.Adapter exchangeAdapter;
    private RecyclerView.LayoutManager exchangeLayoutManager;
    private List<BluetoothItem> bluetoothDevices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        BluetoothController bluetoothController = new BluetoothController(this);
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

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bluetoothController.ScanNearby(bluetoothDevices);

                if (bluetoothDevices.isEmpty()) {
                    // If the list is empty, show the empty view and hide the RecyclerView
                    instructionView.setVisibility(View.VISIBLE);
                    exchangeView.setVisibility(View.GONE);
                } else {
                    // If the list has devices, hide the empty view and show the RecyclerView
                    instructionView.setText(nonEmptyList);
                    exchangeView.setVisibility(View.VISIBLE);

                    // Update the adapter with the new list of devices
                    exchangeAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void populateBluetoothDevices() {
        // Clear the list
        bluetoothDevices.clear();
        // Add Bluetooth devices to the list
        bluetoothDevices.add(new BluetoothItem("Galaxy S21", "F8:DA:0C:1A:BE:71"));
        bluetoothDevices.add(new BluetoothItem("iPhone 12 Pro Max", "B0:3B:C6:2F:6C:54"));
        bluetoothDevices.add(new BluetoothItem("Pixel 5", "9C:3D:3E:1F:0B:91"));
        bluetoothDevices.add(new BluetoothItem("iPad Air 4", "E9:6C:1D:0A:C8:17"));
        bluetoothDevices.add(new BluetoothItem("Surface Pro 7", "A4:9B:4F:4A:11:32"));
        bluetoothDevices.add(new BluetoothItem("ThinkPad X1 Carbon", "D1:BF:9C:FA:43:7E"));
        bluetoothDevices.add(new BluetoothItem("MacBook Air M1", "F0:18:98:29:94:2A"));
        bluetoothDevices.add(new BluetoothItem("Lenovo Yoga C940", "C8:62:9F:31:0D:7B"));
        bluetoothDevices.add(new BluetoothItem("Galaxy Tab S7", "5C:89:D4:2A:01:5E"));
        bluetoothDevices.add(new BluetoothItem("iPad Pro 2021", "60:77:E2:3A:DE:0D"));
        bluetoothDevices.add(new BluetoothItem("Pixelbook Go", "A4:C3:61:47:3E:24"));
        exchangeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
    }

}