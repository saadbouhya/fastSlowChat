package com.example.slowvf.Dao.Impl;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;

import com.example.slowvf.Dao.ExchangeDao;
import com.example.slowvf.Model.MessageEchange;
import com.example.slowvf.Model.ReceivedMessage;
import com.example.slowvf.Model.SentMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
    public void addUpdateExchangeMessage(Context context, MessageEchange message) {
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
    public void updateMessage(Context context, MessageEchange updatedMessage) {
        List<MessageEchange> messages = getExchangeMessages(context);
        for (MessageEchange message : messages) {
            if (message.equals(updatedMessage)) {
                messages.remove(message);
                messages.add(updatedMessage);
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

    @Override
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
        private String id_local;
        private List<SentMessage> sent_messages;
        private List<ReceivedMessage> received_messages;
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

    @Override
    public List<ReceivedMessage> getReceivedMessages(Context context) {
        List<ReceivedMessage> messages = new ArrayList<>();
        Gson gson = new Gson();
        Type containerType = new TypeToken<MessagesContainer>() {}.getType();

        try {
            InputStream inputStream = context.getAssets().open(LOCALE_FILE);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            MessagesContainer container = gson.fromJson(bufferedReader, containerType);
            if (container != null) {
                if (container.getReceived_messages() != null) {
                    messages.addAll(container.getReceived_messages());
                }
            }
            inputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Une erreur s'est produite lors de l'implémentation DAO");
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public List<SentMessage> getSentMessages(Context context) {
        List<SentMessage> messages = new ArrayList<>();
        Gson gson = new Gson();
        Type containerType = new TypeToken<MessagesContainer>() {}.getType();

        try {
            InputStream inputStream = context.getAssets().open(LOCALE_FILE);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            MessagesContainer container = gson.fromJson(bufferedReader, containerType);
            if (container != null) {
                if (container.getSent_messages() != null) {
                    messages.addAll(container.getSent_messages());
                }
            }
            inputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Une erreur s'est produite lors de l'implémentation DAO");
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public void addReceivedMessage(Context context, ReceivedMessage message) {
        List<ReceivedMessage> messages = getReceivedMessages(context);
        messages.add(message);
        saveReceivedMessages(context, messages);
    }

    @Override
    public void updateReceivedMessage(Context context, ReceivedMessage message) {
        List<ReceivedMessage> messages = getReceivedMessages(context);
        int index = findReceivedMessageIndex(messages, message.getId_sender());
        if (index != -1) {
            messages.set(index, message);
            saveReceivedMessages(context, messages);
        }
    }

    private int findReceivedMessageIndex(List<ReceivedMessage> messages, String idSender) {
        for (int i = 0; i < messages.size(); i++) {
            ReceivedMessage message = messages.get(i);
            if (message.getId_sender().equals(idSender)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void addSentMessage(Context context, SentMessage message) {
        List<SentMessage> messages = getSentMessages(context);
        messages.add(message);
        saveSentMessages(context, messages);
    }

    @Override
    public void updateSentMessage(Context context, SentMessage message) {
        List<SentMessage> messages = getSentMessages(context);
        int index = findSentMessageIndex(messages, message.getId_receiver());
        if (index != -1) {
            messages.set(index, message);
            saveSentMessages(context, messages);
        }
    }

    private int findSentMessageIndex(List<SentMessage> messages, String idReceiver) {
        for (int i = 0; i < messages.size(); i++) {
            SentMessage message = messages.get(i);
            if (message.getId_receiver().equals(idReceiver)) {
                return i;
            }
        }
        return -1;
    }

    public void saveSentMessages(Context context, List<SentMessage> messages) {
        MessagesContainer container = loadMessagesContainer(context);
        if (container == null) {
            container = new MessagesContainer();
        }
        container.setSent_messages(messages);
        saveMessagesContainer(context, container);
    }

    public void saveReceivedMessages(Context context, List<ReceivedMessage> messages) {
        MessagesContainer container = loadMessagesContainer(context);
        if (container == null) {
            container = new MessagesContainer();
        }
        container.setReceived_messages(messages);
        saveMessagesContainer(context, container);
    }

    private MessagesContainer loadMessagesContainer(Context context) {
        Gson gson = new Gson();
        Type containerType = new TypeToken<MessagesContainer>() {}.getType();

        try {
            InputStream inputStream = context.getAssets().open(LOCALE_FILE);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            MessagesContainer container = gson.fromJson(bufferedReader, containerType);
            inputStream.close();
            return container;
        } catch (IOException e) {
            Log.e(TAG, "An error occurred while loading the messages container");
            e.printStackTrace();
        }
        return null;
    }

    private void saveMessagesContainer(Context context, MessagesContainer container) {
        Gson gson = new Gson();
        String json = gson.toJson(container);

        try (OutputStream outputStream = context.openFileOutput(LOCALE_FILE, Context.MODE_PRIVATE);
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            bufferedWriter.write(json);
        } catch (IOException e) {
            Log.e(TAG, "An error occurred while saving the messages container");
            e.printStackTrace();
        }
    }
}
