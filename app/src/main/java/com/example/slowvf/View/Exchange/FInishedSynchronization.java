package com.example.slowvf.View.Exchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.slowvf.R;

public class FInishedSynchronization extends AppCompatActivity {

    private BluetoothItem selectedDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_synchronization);

        Intent intent = getIntent();
        selectedDevice = (BluetoothItem) intent.getSerializableExtra("bluetoothItem");
        TextView deviceNameTextView = findViewById(R.id.device_name_textview);
        deviceNameTextView.setText(selectedDevice.getName());
        Button finishButton = findViewById(R.id.fin_synch);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FInishedSynchronization.this, Exchange.class);
                startActivity(intent);
                finish();
            }
        });
        ImageButton backButton = findViewById(R.id.back_synchro);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FInishedSynchronization.this, Exchange.class);
                startActivity(intent);
                finish(); // closes the current activity and returns to the previous one
            }
        });
    }
}