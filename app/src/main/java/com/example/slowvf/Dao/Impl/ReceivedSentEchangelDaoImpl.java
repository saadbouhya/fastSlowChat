package com.example.slowvf.Dao.Impl;

import android.content.Context;


import com.example.slowvf.Dao.receivedSentEchangelDao;
import com.example.slowvf.Model.Echange;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReceivedSentEchangelDaoImpl implements receivedSentEchangelDao {

    public static Echange echangeFile(Context context) throws IOException {
        FileInputStream fis = context.openFileInput("Echange.json");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        fis.close();

        Gson gson = new Gson();
        Echange messageInfo = gson.fromJson(stringBuilder.toString(), Echange.class);

        return messageInfo;
    }

    public static void writeToJsonFile(Context context, Echange local) throws IOException {
        Gson gson = new Gson();
        String jsonString = gson.toJson(local);
        FileOutputStream fileOutputStream = context.openFileOutput("Echange.json", Context.MODE_PRIVATE);
        fileOutputStream.write(jsonString.getBytes());
        fileOutputStream.close();
    }

    public static void createFileEchangeOnInternalStorage(Context context) throws IOException {
        String fileName = "Echange.json";
        File file = new File(context.getFilesDir(), fileName);
        if (!file.exists()) {
            FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }}

    public static String readInternalFile(Context context, String filename) throws IOException {
        FileInputStream fis = context.openFileInput(filename);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        isr.close();
        fis.close();
        return sb.toString();


    }


}