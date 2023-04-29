package com.example.slowvf.Controller;

import android.content.Context;

import com.example.slowvf.Dao.Impl.ReceivedSentLocalDao;
import com.example.slowvf.Model.Local;
import com.example.slowvf.Model.LocalForMessage;
import com.example.slowvf.Model.ReceivedMessage;
import com.example.slowvf.Model.SentMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//   chatController chatController = new chatController(getApplicationContext()); pour l'appeler
public class chatController {
    private Context context;

    public chatController(Context context) throws IOException {
        this.context = context;
    }

public Local getMessagesReceivedSentLocal() throws IOException {

        return ReceivedSentLocalDao.localfile(context);
}

    public List<String> getUniqueIdSendersAndReceivers() throws IOException {
        Set<String> sendersAndReceivers = new HashSet<>();
        Local local = getMessagesReceivedSentLocal();
        for (SentMessage sentMessage : local.getSent_messages()) {
            sendersAndReceivers.add(sentMessage.getId_receiver());
        }

        for (ReceivedMessage receivedMessage : local.getReceived_messages()) {
            sendersAndReceivers.add(receivedMessage.getId_sender());
        }

        return new ArrayList<>(sendersAndReceivers);
    }

    public List<LocalForMessage> getLastMessagesForUniqueSendersAndReceivers() throws IOException {
        Local local = getMessagesReceivedSentLocal();
        List<String> uniqueSendersAndReceivers = getUniqueIdSendersAndReceivers();
        List<LocalForMessage> lastMessages = new ArrayList<>();

        // Map pour stocker le dernier message de chaque id_sender et id_receiver
        Map<String, String> lastMessagesMap = new HashMap<>();

        // Map pour stocker la date du dernier message de chaque id_sender et id_receiver
        Map<String, String> lastDatesMap = new HashMap<>();

        // Itération à travers les sent_messages de Local
        for (SentMessage sentMessage : local.getSent_messages()) {
            String id_receiver = sentMessage.getId_receiver();
            String message = sentMessage.getTexte();
            String date = sentMessage.getDate_writing();

            // Si l'id_receiver est dans la liste des id_sender et id_receiver uniques
            if (uniqueSendersAndReceivers.contains(id_receiver)) {
                // Si le message est plus récent que le dernier message connu pour cet id_receiver
                if (!lastDatesMap.containsKey(id_receiver) || date.compareTo(lastDatesMap.get(id_receiver)) > 0) {
                    lastMessagesMap.put(id_receiver, message);
                    lastDatesMap.put(id_receiver, date);
                }
            }
        }

        // Itération à travers les received_messages de Local
        for (ReceivedMessage receivedMessage : local.getReceived_messages()) {
            String id_sender = receivedMessage.getId_sender();
            String message = receivedMessage.getTexte();
            String date = receivedMessage.getDate_writing();

            // Si l'id_sender est dans la liste des id_sender et id_receiver uniques
            if (uniqueSendersAndReceivers.contains(id_sender)) {
                // Si le message est plus récent que le dernier message connu pour cet id_sender
                if (!lastDatesMap.containsKey(id_sender) || date.compareTo(lastDatesMap.get(id_sender)) > 0) {
                    lastMessagesMap.put(id_sender, message);
                    lastDatesMap.put(id_sender, date);
                }
            }
        }

        // Création de la liste de résultats
        for (String id : uniqueSendersAndReceivers) {
            String message = lastMessagesMap.get(id);
            String date = lastDatesMap.get(id);
            lastMessages.add(new LocalForMessage("voirContact",id,message,date));
        }

        return lastMessages;
    }


}
