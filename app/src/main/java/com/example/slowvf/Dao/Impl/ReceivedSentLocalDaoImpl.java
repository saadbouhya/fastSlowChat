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

    public static Local localfile(Context context) throws IOException {
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

    public static void createFileOnInternalStorage(Context context) throws IOException {
        String fileName = "Local.json";
        String fileContent = "{\n" +
                "  \"id_local\": \"5E:FF:56:A2:AF:15walid14-04-2023\",\n" +
                "  \"sent_messages\": [\n" +
                "    {\n" +
                "      \"id_receiver\": \"5E:FF:56:A2:AF:15theo19-04-2023\",\n" +
                "      \"texte\": \"Bonjour Theo, comment vas-tu ?\",\n" +
                "      \"date_writing\": \"2022-01-01 10:00:00\",\n" +
                "      \"date_received\": \"null (si pas encore reçu)\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id_receiver\": \"5E:FF:56:A2:AF:15Paul16-04-2023\",\n" +
                "      \"texte\": \"aussi merci!\",\n" +
                "      \"date_writing\": \"2022-01-01 10:05:00\",\n" +
                "      \"date_received\": \"2023-01-01 10:20:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id_receiver\": \"5E:FF:56:A2:AF:15MIAM16-04-2023\",\n" +
                "      \"texte\": \"YOLOOOOOOO\",\n" +
                "      \"date_writing\": \"2050-01-01 10:05:00\",\n" +
                "      \"date_received\": \"2060-01-01 10:20:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id_receiver\": \"5E:FF:56:A2:AF:15Yoda16-04-2023\",\n" +
                "      \"texte\": \"J'aime les licornes\",\n" +
                "      \"date_writing\": \"2022-01-01 10:05:00\",\n" +
                "      \"date_received\": \"2023-01-01 10:20:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id_receiver\": \"5E:FF:56:A2:AF:15kevin16-04-2023\",\n" +
                "      \"texte\": \"OULALALA\",\n" +
                "      \"date_writing\": \"2022-01-01 10:05:00\",\n" +
                "      \"date_received\": \"2023-01-01 10:20:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id_receiver\": \"5E:FF:56:A2:AF:15kevin16-04-2023\",\n" +
                "      \"texte\": \"J'adore le sel\",\n" +
                "      \"date_writing\": \"2022-01-01 10:05:00\",\n" +
                "      \"date_received\": \"2023-01-01 10:20:00\"\n" +
                "    }\n" +
                "\n" +
                "  ],\n" +
                "  \"received_messages\": [\n" +
                "    {\n" +
                "      \"id_sender\": \"Inconnu\",\n" +
                "      \"texte\": \"Bonjour Walid, ça va bien. Et toi ?\",\n" +
                "      \"date_writing\": \"2021-01-01 10:01:00\",\n" +
                "      \"date_received\": \"2021-01-01 10:20:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id_sender\": \"5E:FF:56:A2:AF:15Mouad16-04-2023\",\n" +
                "      \"texte\": \"Merci Naruto.\",\n" +
                "      \"date_writing\": \"2020-01-01 10:01:00\",\n" +
                "      \"date_received\": \"2027-01-01 10:20:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id_sender\": \"5E:FF:56:A2:AF:15Moulad16-04-2023\",\n" +
                "      \"texte\": \"MOULA MOULA\",\n" +
                "      \"date_writing\": \"2025-01-01 10:01:00\",\n" +
                "      \"date_received\": \"2030-01-01 10:20:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id_sender\": \"5E:FF:56:A2:AF:15Yoda16-04-2023\",\n" +
                "      \"texte\": \"Force tu as\",\n" +
                "      \"date_writing\": \"2022-01-01 10:01:00\",\n" +
                "      \"date_received\": \"2023-03-01 10:20:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id_sender\": \"5E:FF:56:A2:AF:15Mouad16-04-2023\",\n" +
                "      \"texte\": \"Merci Naruto.\",\n" +
                "      \"date_writing\": \"2022-01-01 10:01:00\",\n" +
                "      \"date_received\": \"2023-05-14 10:20:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id_sender\": \"5E:FF:56:A2:AF:15Yoda16-04-2023\",\n" +
                "      \"texte\": \"Manger tu dois\",\n" +
                "      \"date_writing\": \"2022-01-01 10:01:00\",\n" +
                "      \"date_received\": \"2030-01-01 10:20:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id_sender\": \"5E:FF:56:A2:AF:15MIAM16-04-2023\",\n" +
                "      \"texte\": \"Merci beaucoup\",\n" +
                "      \"date_writing\": \"2022-01-01 10:01:00\",\n" +
                "      \"date_received\": \"2023-01-01 10:20:00\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"size_file\" : 50\n" +
                "}" ;

        File file = new File(context.getFilesDir(), fileName);
        if (!file.exists()) {
            FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(fileContent.getBytes());
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