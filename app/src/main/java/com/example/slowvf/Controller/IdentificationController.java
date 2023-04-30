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
        String adresseMac = getMacAdresse();
        identificationDao.createUser(pseudo, adresseMac);
    }

    private String getMacAdresse(){
        //BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // ajouter la vérification que le bluetooth est activé
        return "58:AD:65:98:SQ";
    }
}
