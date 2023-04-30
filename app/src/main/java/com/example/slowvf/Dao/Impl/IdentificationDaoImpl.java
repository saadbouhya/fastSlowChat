package com.example.slowvf.Dao.Impl;

import android.content.Context;

import com.example.slowvf.Dao.IdentificationDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    public void createUser(String pseudo, String adresseMac) {
        System.out.println(pseudo + " " + adresseMac);
        Map<String, Object> userData = new HashMap<>();
        userData.put("id_local", pseudo);
        userData.put("sent_messages", new String[]{});
        userData.put("received_messages", new String[]{});
        userData.put("size_file", 50);
        // Remplir la liste des utilisateurs avec les données de l'application
        Context context = context.getApplicationContext();
        String filename = "monFichier.txt";
        int mode = Context.MODE_PRIVATE;
        String data = "Ceci est une phrase dans mon fichier.";

        try {
            FileOutputStream outputStream = context.openFileOutput(filename, mode);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
