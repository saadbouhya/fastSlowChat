package com.example.slowvf.View.Adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slowvf.Controller.Exchange.BluetoothController;
import com.example.slowvf.Model.BluetoothItem;
import com.example.slowvf.R;
import java.util.List;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder> {
    private List<BluetoothItem> bluetoothDevices;
    private BluetoothController bluetoothController;

    public ExchangeAdapter(List<BluetoothItem> devices, BluetoothController bluetoothController) {
        bluetoothDevices = devices;
        this.bluetoothController = bluetoothController;
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
        holder.addressTextView.setText(device.getMacAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirmation");
                builder.setMessage("Êtes-vous sûr de vouloir vous synchroniser avec cet appareil " + device.getName());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bluetoothController.sendHelloMessage(device.getMacAddress());
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
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
        public TextView addressTextView;

        public ExchangeViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView);
            addressTextView = view.findViewById(R.id.address);
        }
    }
}
