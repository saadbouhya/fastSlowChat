package com.example.slowvf.Controller;

import android.content.Context;

import com.example.slowvf.Dao.IdentificationDao;
import com.example.slowvf.Dao.Impl.IdentificationDaoImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IdentificationController {
        private IdentificationDao identificationDao;

    public IdentificationController(Context context) {
        identificationDao = new IdentificationDaoImpl(context);
    }

    public void createUser(String pseudo){
        identificationDao.createUser(pseudo, getMacAdresse(), getActualDate());
    }

    private String getMacAdresse(){
        //BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // je ne peux pas tester avec un emulateur le bluetooth
        return "58:AD:65:98:SQ";
    }
    private String getActualDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(new Date());
    }
}
