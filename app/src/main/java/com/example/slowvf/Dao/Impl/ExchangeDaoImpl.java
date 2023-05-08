package com.example.slowvf.Dao.Impl;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;

import com.example.slowvf.Dao.ExchangeDao;
import com.example.slowvf.Model.MessageEchange;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExchangeDaoImpl implements ExchangeDao {

    private static final String ECHANGE_FILE = "Echange.json";
    private static final String LOCALE_FILE = "Local.json";

    @Override
    public void addMessage(Context context, MessageEchange message, boolean isLocal) {
        List<MessageEchange> messages = isLocal ? getLocalMessages(context) : getExchangeMessages(context);
        if (!messages.contains(message)) {
            messages.add(message);
            writeMessagesToFile(context, messages, isLocal);
        }
    }



    @Override
    public void updateMessage(Context context, MessageEchange message, boolean isLocal) {
        List<MessageEchange> messages = isLocal ? getLocalMessages(context) : getExchangeMessages(context);
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).equals(message)) {
                messages.set(i, message);
                writeMessagesToFile(context, messages, isLocal);
                break;
            }
        }
    }

    @Override
    public boolean messageExist(Context context, MessageEchange message, boolean isLocal) {
        List<MessageEchange> messages = isLocal ? getLocalMessages(context) : getExchangeMessages(context);
        return messages.contains(message);
    }


    @Override
    public List<MessageEchange> getExchangeMessages(Context context) {
        return getMessagesFromFile(context, ECHANGE_FILE);
    }

    @Override
    public List<MessageEchange> getLocalMessages(Context context) {
        return getMessagesFromFile(context, LOCALE_FILE);
    }

    public List<MessageEchange> getMessagesFromFile(Context context, String fileName) {
        List<MessageEchange> messages = new ArrayList<>();
        Gson gson = new Gson();
        Type messageType = new TypeToken<List<MessageEchange>>() {}.getType();

        try {
            InputStream inputStream = context.getAssets().open(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            List<MessageEchange> parsedMessages = gson.fromJson(bufferedReader, messageType);
            messages.addAll(parsedMessages);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public void writeMessagesToFile(Context context, List<MessageEchange> messages, boolean isLocal) {
        String fileName = isLocal ? LOCALE_FILE : ECHANGE_FILE;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(messages);
        try {
            OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
