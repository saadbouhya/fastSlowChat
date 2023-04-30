package com.example.slowvf.Controller;

import android.bluetooth.BluetoothAdapter;

import com.example.slowvf.Dao.IdentificationDao;
import com.example.slowvf.Dao.Impl.IdentificationDaoImpl;

public class IdentificationController {
        private IdentificationDao identificationDao;

    public IdentificationController() {
        identificationDao = new IdentificationDaoImpl();
    }

    public void createUser(String pseudo){
        String adresseMac = getAdresseMac();
        identificationDao.createUser(pseudo, adresseMac);
    }

    private String getAdresseMac(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // ajouter la vérification que le bluetooth est activé
        return bluetoothAdapter.getAddress();
    }
}
