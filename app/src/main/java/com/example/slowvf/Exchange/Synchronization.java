package com.example.slowvf.Exchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.slowvf.R;

import java.io.Serializable;

public class Synchronization extends AppCompatActivity {

    private int CurrentProgress = 0;
    private ProgressBar progressBar;
    private BluetoothItem selectedDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchonosation);
        progressBar = findViewById(R.id.progressBar);
        Intent intent = getIntent();
        selectedDevice = (BluetoothItem) intent.getSerializableExtra("bluetoothItem");
        TextView deviceNameTextView = findViewById(R.id.device_name_textview);
        deviceNameTextView.setText(selectedDevice.getName());

        ImageButton backButton = findViewById(R.id.back_synchro);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // closes the current activity and returns to the previous one
            }
        });

        Button nextSynch = findViewById(R.id.next_synch);
        nextSynch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Synchronization.this, FInishedSynchronization.class);
                intent.putExtra("bluetoothItem", (Serializable) selectedDevice);
                startActivity(intent);
            }
        });
    }
}