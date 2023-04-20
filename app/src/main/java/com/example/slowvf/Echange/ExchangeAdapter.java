package com.example.slowvf.Echange;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slowvf.R;

import java.io.Serializable;
import java.util.List;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder> {
    private List<BluetoothItem> bluetoothDevices;

    public ExchangeAdapter(List<BluetoothItem> devices) {
        bluetoothDevices = devices;
    }

    @NonNull
    @Override
    public ExchangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_item, parent, false);
        return new ExchangeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExchangeViewHolder holder, int position) {
        BluetoothItem device = bluetoothDevices.get(position);
        holder.textView.setText(device.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a popup dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(),R.style.MyAlertDialogTheme);
                builder.setTitle("Synchronisation");

                // Set the dialog message to the device name and MAC address
                SpannableString message = new SpannableString("Device: " + device.getName() + "\n" + "MAC Address: " + device.getMacAddress());
                message.setSpan(new ForegroundColorSpan(Color.WHITE), 0, message.length(), 0);
                builder.setMessage(message);
                builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Context context = holder.itemView.getContext();
                        Intent intent = new Intent(context, Synchonosation.class);
                        intent.putExtra("bluetoothItem", (Serializable) device);
                        context.startActivity(intent);
                    }
                });
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when Close button is clicked
                    }
                });

                // Show the dialog
                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawableResource(R.color.background);
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bluetoothDevices.size();
    }

    public static class ExchangeViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ExchangeViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView);
        }
    }
}
