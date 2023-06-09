package com.example.slowvf.Dao.Impl;

import android.content.Context;

import com.example.slowvf.Dao.receivedSentLocalDao;
import com.example.slowvf.Model.Local;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReceivedSentLocalDaoImpl implements receivedSentLocalDao {

    public static Local localFile(Context context) throws IOException {
        FileInputStream fis = context.openFileInput("Local.json");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        fis.close();

        Gson gson = new Gson();
        Local messageInfo = gson.fromJson(stringBuilder.toString(), Local.class);

        return messageInfo;
    }

    public static void writeToJsonFile(Context context, Local local) throws IOException {
        Gson gson = new Gson();
        String jsonString = gson.toJson(local);
        FileOutputStream fileOutputStream = context.openFileOutput("Local.json", Context.MODE_PRIVATE);
        fileOutputStream.write(jsonString.getBytes());
        fileOutputStream.close();
    }

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