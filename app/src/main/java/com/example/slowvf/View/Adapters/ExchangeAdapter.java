package com.example.slowvf.View.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slowvf.Controller.Exchange.BluetoothController;
import com.example.slowvf.Model.BluetoothItem;
import com.example.slowvf.R;

import java.io.IOException;
import java.util.List;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder> {
    private List<BluetoothItem> bluetoothDevices;
    private BluetoothController bluetoothController;
    private Thread connectThread;
    private Context context;

    public ExchangeAdapter(List<BluetoothItem> devices, BluetoothController bluetoothController, Context context) {
        bluetoothDevices = devices;
        this.bluetoothController = bluetoothController;
        this.context = context;
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

                if (connectThread != null && connectThread.isAlive()) {
                    // Un thread est déjà en cours d'exécution, annulez-le si nécessaire
                    connectThread.interrupt();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirmation");
                builder.setMessage("Êtes-vous sûr de vouloir vous synchroniser avec cet appareil " + device.getName());
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Créer un nouveau thread
                        connectThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    bluetoothController.connectToDevice(device.getMacAddress());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                        // Démarrer le thread
                        connectThread.start();
                    }
                });
                builder.setNegativeButton("Non", null);
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
