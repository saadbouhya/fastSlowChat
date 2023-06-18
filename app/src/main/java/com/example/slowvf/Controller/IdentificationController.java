package com.example.slowvf.Controller;

import android.bluetooth.BluetoothAdapter;

import android.content.Context;

import com.example.slowvf.Dao.IdentificationDao;
import com.example.slowvf.Dao.Impl.IdentificationDaoImpl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.provider.Settings;

public class IdentificationController {
        private IdentificationDao identificationDao;
        private Context context;
    public IdentificationController(Context context) {
        identificationDao = new IdentificationDaoImpl(context);
        this.context = context;
    }

    public void createUser(String pseudo){
        identificationDao.createUser(pseudo, getMacAdresse(), getActualDate());
    }

    private String getMacAdresse(){
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }
    private String getActualDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(new Date());
    }

    public boolean fileExists(String fileName, Context context) {
        File file = context.getFileStreamPath(fileName);
        return file.exists();
    }
    public void deleteFile(String fileName, Context context){
        identificationDao.deleteFile(fileName,context);
    }

}
