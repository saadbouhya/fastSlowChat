package com.example.slowvf.Dao.Impl;

import android.content.Context;

import com.example.slowvf.Dao.IdentificationDao;
import com.example.slowvf.Model.Local;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class IdentificationDaoImpl implements IdentificationDao {
    private ObjectMapper objectMapper;
    Context context;

    public IdentificationDaoImpl() {
        objectMapper = new ObjectMapper();
    }

    public IdentificationDaoImpl(Context context) {
        objectMapper = new ObjectMapper();
        this.context = context;
    }

    @Override
    public void createUser(String pseudo, String macAdresse, String actualDate) {
        Local local = new Local(macAdresse+pseudo+actualDate,"50");
        Gson gson = new Gson();
        String filename = "Local.json";
        String localJson = gson.toJson(local);
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(localJson.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // code pour la lecture du fichier ( a enlever plus tard d'ici)
        try {
            // Ouvrir le fichier pour lecture
            FileInputStream inputStream = context.openFileInput(filename);

            // Lire le contenu du fichier
            StringBuilder stringBuilder = new StringBuilder();
            int data2;
            while ((data2 = inputStream.read()) != -1) {
                stringBuilder.append((char) data2);
            }
            String contenuFichier = stringBuilder.toString();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
