package com.example.slowvf.View.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slowvf.Model.BluetoothItem;
import com.example.slowvf.R;
import com.example.slowvf.View.Exchange.Synchronization;

import java.io.Serializable;
import java.util.List;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder> {
    private List<BluetoothItem> bluetoothDevices;
    private Context context;

    public ExchangeAdapter(List<BluetoothItem> devices, Context context) {
        bluetoothDevices = devices;
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = LayoutInflater.from(v.getContext()).inflate(R.layout.activity_connection_confirmation, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setView(popupView);
                ImageView closeButton = popupView.findViewById(R.id.close_button);
                AlertDialog alertDialog = builder.create();
                View.OnClickListener closeListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Obtention des attributs du thème
                        TypedArray themeAttributes = context.getTheme().obtainStyledAttributes(R.style.Theme_SlowVF,
                                new int[]{android.R.attr.colorBackground, com.google.android.material.R.attr.colorOnBackground, com.google.android.material.R.attr.colorPrimary});

// Récupération des valeurs des attributs
                        int colorBackgroundValue = themeAttributes.getColor(0, 0);
                        int colorOnBackgroundValue = themeAttributes.getColor(1, 0);
                        int colorPrimaryValue = themeAttributes.getColor(2, 0);

                        // Dismiss the popup when the Close button is clicked
                        alertDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(),R.style.MyAlertDialogTheme);

                        SpannableString titre = new SpannableString("Demande déclinée");
                        titre.setSpan(new ForegroundColorSpan(colorOnBackgroundValue), 0, titre.length(), 0);
                        builder.setTitle(titre);

                        // Set the dialog message to the device name and MAC address
                        SpannableString message = new SpannableString("Demande de synchronisation declinée");
                        message.setSpan(new ForegroundColorSpan(colorOnBackgroundValue), 0, message.length(), 0);
                        builder.setMessage(message);

                        SpannableString boutton = new SpannableString("Fermer");
                        boutton.setSpan(new ForegroundColorSpan(colorPrimaryValue), 0, boutton.length(), 0);
                        builder.setNegativeButton(boutton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do something when Close button is clicked
                            }
                        });

                        // Show the dialog
                        AlertDialog dialog = builder.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(colorBackgroundValue));
                        dialog.show();
                    }
                };
                closeButton.setOnClickListener(closeListener);
                Button declineButton = popupView.findViewById(R.id.synch_decline);
                declineButton.setOnClickListener(closeListener);
                Button acceptButton = popupView.findViewById(R.id.synch_accept);
                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = holder.itemView.getContext();
                        Intent intent = new Intent(context, Synchronization.class);
                        intent.putExtra("bluetoothItem", (Serializable) device);
                        context.startActivity(intent);
                    }
                });

                alertDialog.show();
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
