package com.example.slowvf.View.Exchange;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.slowvf.Model.Local;
import com.example.slowvf.R;
import com.example.slowvf.View.Adapters.ExchangeAdapter;
import com.example.slowvf.View.Adapters.MypagerAdapter;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.Getter;

public class Exchange extends Fragment implements Serializable{
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;
    private static final int REQUEST_DISCOVERABLE = 3;

    private final static String emptyList = "Cliquer sur le bouton scan pour chercher les appareils à proximité";
    private final static String nonEmptyList = "Choisir un appareil pour échanger vos données";

    private RecyclerView exchangeView;
    private ExchangeAdapter exchangeAdapter;
    private RecyclerView.LayoutManager exchangeLayoutManager;
    private TextView visibilityText;
    private BluetoothController bluetoothController;

    private List<BluetoothItem> bluetoothDevices = new ArrayList<>();

    @Getter
    private ProgressDialog progressDialog;
    private TextView instructionView;
    private Button scanButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_exchange, container, false);
        scanButton = rootView.findViewById(R.id.scan_button);
        visibilityText = rootView.findViewById(R.id.visibility_Text);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BluetoothController.checkBluetoothPermission(Exchange.this)) {
                    enableBluetoothFunctions(scanButton);
                }
            }
        });

        if (BluetoothController.checkBluetoothPermission(this)) {
            enableBluetoothFunctions( scanButton);

        }

        exchangeView = rootView.findViewById(R.id.exchange_view);
        exchangeLayoutManager = new LinearLayoutManager(getContext());
        exchangeAdapter = new ExchangeAdapter(bluetoothDevices, bluetoothController,getContext());
        exchangeView.setLayoutManager(exchangeLayoutManager);
        exchangeView.setAdapter(exchangeAdapter);

        instructionView = rootView.findViewById(R.id.instruction_view);

        // Hide the RecyclerView and show the empty view initially
        exchangeView.setVisibility(View.GONE);
        instructionView.setText(emptyList);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Ajoutez le code pour fermer le BluetoothServerSocket ici
        if (bluetoothController != null) {
            bluetoothController.closeSockets();
        }
    }


    public void enableBluetoothFunctions(Button scanButton ){
        bluetoothController = new BluetoothController(this);

        //Bluetooth Visibility

        Intent discoverableIntent = new Intent(bluetoothController.getBluetoothAdapter().ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(bluetoothController.getBluetoothAdapter().EXTRA_DISCOVERABLE_DURATION, 1200);
        startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothController.checkBluetoothPermission()) {
                    // Show a waiting dialog
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Waiting for Bluetooth devices...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    // Clear the list of Bluetooth devices
                    bluetoothDevices.clear();
                    bluetoothController.startDiscovery();



                    // Register a BroadcastReceiver to listen for scan results
                    BroadcastReceiver receiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            String action = intent.getAction();
                            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                                if (!TextUtils.isEmpty(device.getName())) {
                                    boolean isDuplicate = false;
                                    for (BluetoothItem bluetoothItem : bluetoothDevices) {
                                        if (bluetoothItem.getMacAddress().equals(device.getAddress())) {
                                            isDuplicate = true;
                                            break;
                                        }
                                    }
                                    if (!isDuplicate) {
                                        bluetoothDevices.add(new BluetoothItem(device.getName(), device.getAddress()));
                                    }
                                }
                            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                                bluetoothController.stopDiscovery();
                                refreshBluetoothList();
                                // Dismiss the waiting dialog
                                progressDialog.dismiss();
                                // Unregister the BroadcastReceiver
                                requireActivity().unregisterReceiver(this);
                            }
                        }
                    };

                    // Register the BroadcastReceiver for scan results
                    IntentFilter filter = new IntentFilter();
                    filter.addAction(BluetoothDevice.ACTION_FOUND);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    requireActivity().registerReceiver(receiver, filter);
                }
            }
        });
    }





    public void refreshBluetoothList(){
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
    }


    public void openSynchronization(BluetoothItem bluetoothItem) {
        Intent intent = new Intent(getContext(), FInishedSynchronization.class);
        intent.putExtra("bluetoothItem", bluetoothItem);
        startActivity(intent);
        getActivity().finish();
    }

    public void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // à remplacer par l'appel à Params Contrommer apres merge develop
    public String getUserId() {
        String filename = "Local.json";
        try {
            FileInputStream inputStream = getContext().openFileInput(filename);

            StringBuilder stringBuilder = new StringBuilder();
            int data;
            while ((data = inputStream.read()) != -1) {
                stringBuilder.append((char) data);
            }
            String contenuFichier = stringBuilder.toString();

            Gson gson = new Gson();
            Local local = gson.fromJson(contenuFichier, Local.class);

            return local.getIdLocal();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ENABLE_BT || requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (BluetoothController.checkBluetoothPermission(Exchange.this)) {
                    enableBluetoothFunctions(scanButton);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_DISCOVERABLE) {
            if (resultCode == 1200) {
                visibilityText.setText("Visible en tant que " + bluetoothController.getBluetoothAdapter().getDefaultAdapter().getName());
            } else {
                // User declined or canceled the discoverability request
                // Perform any actions needed after the user declines or cancels
            }
        }
    }



}