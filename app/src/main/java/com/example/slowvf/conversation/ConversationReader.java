package com.example.slowvf.conversation;

import android.content.Context;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ConversationReader {
    private String nom;
    private Context context;

    public ConversationReader(String nom, Context context) {
        this.nom = nom;
        this.context = context;
    }

    public List<Message> lireMessages() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Message> messages = null;

        // to do: json input stream
        try {
            InputStream is = context.getAssets().open("conversation.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            TypeReference<List<Message>> typeReference = new TypeReference<List<Message>>() {};
            messages = objectMapper.readValue(json, typeReference);
            List<Message> messagesFiltres = new ArrayList<>();
            for (Message message : messages) {
                if (message.getDestinataire().equals(this.nom) || message.getAuteur().equals(this.nom)) {
                    messagesFiltres.add(message);
                }
            }
            return messagesFiltres;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
