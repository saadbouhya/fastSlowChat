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

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExchangeDaoImpl implements ExchangeDao {

    private static final String ECHANGE_FILE = "Echange.json";
    private static final String LOCALE_FILE = "Local.json";

    @Override
    public void addUpdateMessage(Context context, MessageEchange message) {
        List<MessageEchange> messages = getExchangeMessages(context);
        if (!messages.contains(message)) {
            messages.add(message);
            writeMessagesToFile(context, messages);
        }
        else {
            updateMessage(context, message);
        }
    }



    @Override
    public void updateMessage(Context context, MessageEchange updatedMmessage) {
        List<MessageEchange> messages = getExchangeMessages(context);
        for (MessageEchange  message : messages) {
            if (message.equals(updatedMmessage)) {
                messages.remove(message);
                messages.add(updatedMmessage);
                break;
            }
        }
        writeMessagesToFile(context, messages);
    }

    @Override
    public boolean messageExist(Context context, MessageEchange message) {
        List<MessageEchange> messages = getExchangeMessages(context);
        return messages.contains(message);
    }


    public List<MessageEchange> getExchangeMessages(Context context) {
        List<MessageEchange> messages = new ArrayList<>();
        Gson gson = new Gson();
        Type containerType = new TypeToken<MessagesContainer>() {}.getType();

        try {
            FileInputStream inputStream = context.openFileInput(ECHANGE_FILE);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            MessagesContainer container = gson.fromJson(bufferedReader, containerType);
            if (container != null && container.getMessages() != null) {
                messages.addAll(container.getMessages());
            }
            inputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "An error occurred during DAO implementation");
            e.printStackTrace();
        }
        return messages;
    }


    @Override
    public List<MessageEchange> getLocalMessages(Context context) {
        return getMessagesFromFile(context, LOCALE_FILE);
    }

    public List<MessageEchange> getMessagesFromFile(Context context, String fileName) {
        return null;
    }
    @Data
    public class MessagesContainer {
        private List<MessageEchange> messages;

        public List<MessageEchange> getMessages() {
            return messages;
        }
    }

    public void writeMessagesToFile(Context context, List<MessageEchange> messages) {
        String fileName = ECHANGE_FILE;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        MessagesContainer container = new MessagesContainer();
        container.setMessages(messages);

        String json = gson.toJson(container);

        try (OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void deleteMessage(Context context, MessageEchange message) {
        List<MessageEchange> messages = getExchangeMessages(context);
        messages.remove(message);
        writeMessagesToFile(context, messages);
    }


}
