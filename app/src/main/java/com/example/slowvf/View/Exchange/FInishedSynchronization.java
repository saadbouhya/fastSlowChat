package com.example.slowvf.View.Exchange;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.slowvf.Dao.Impl.ExchangeDaoImpl;
import com.example.slowvf.Model.BluetoothItem;
import com.example.slowvf.R;
import com.example.slowvf.View.MainActivityNavigation;

public class FInishedSynchronization extends AppCompatActivity {

    private BluetoothItem selectedDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_synchronization);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Synchronisation terminÃ©e");

        Intent intent = getIntent();
        selectedDevice = (BluetoothItem) intent.getSerializableExtra("bluetoothItem");
        TextView deviceNameTextView = findViewById(R.id.device_name_textview);
        Button finishButton = findViewById(R.id.fin_synch);


        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FInishedSynchronization.this, MainActivityNavigation.class);
                startActivity(intent);
                finish();
            }
        });
        TextView totalMessagesTextView = findViewById(R.id.total_messages_textview);
        TextView totalReceivedTextView = findViewById(R.id.total_received_textview);
        TextView totalAcknowledgedTextView = findViewById(R.id.total_acknowledged_textview);

        ExchangeDaoImpl exchangeDao = new ExchangeDaoImpl();

        int totalMessages = exchangeDao.getExchangeMessages(this).size();
        int totalReceived = exchangeDao.getReceivedMessages(this).size();
        int totalAcknowledged = exchangeDao.getSentMessages(this).size();

        totalMessagesTextView.setText("Total Messages: " + totalMessages);
        totalReceivedTextView.setText("Total Received: " + totalReceived);
        totalAcknowledgedTextView.setText("Total Sent: " + totalAcknowledged);

    }
}