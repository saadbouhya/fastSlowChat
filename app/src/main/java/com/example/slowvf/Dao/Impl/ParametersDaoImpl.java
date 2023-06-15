package com.example.slowvf.Dao.Impl;

import android.content.Context;

import com.example.slowvf.Dao.ParametersDao;
import com.example.slowvf.Model.Local;
import com.google.gson.Gson;

import java.io.FileInputStream;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ParametersDaoImpl implements ParametersDao {

    Context context;

    @Override
    public String getUserId() {
        String filename = "Local.json";
        try {
            FileInputStream inputStream = context.openFileInput(filename);

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

}
