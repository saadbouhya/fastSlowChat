package com.example.slowvf.Controller;

import android.content.Context;
import android.os.Build;

import com.example.slowvf.Dao.Impl.ReceivedSentLocalDaoImpl;
import com.example.slowvf.Model.Local;
import com.example.slowvf.Model.LocalForConversation;
import com.example.slowvf.Model.LocalForMessage;
import com.example.slowvf.Model.ReceivedMessage;
import com.example.slowvf.Model.SentMessage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//   ChatController ChatController = new ChatController(getApplicationContext()); pour l'appeler
public class ChatController {
    private Context context;

    public ChatController(Context context) throws IOException {
        this.context = context;
    }

public Local getMessagesReceivedSentLocal() throws IOException {

        return ReceivedSentLocalDaoImpl.localfile(context);
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
            lastMessages.add(new LocalForMessage("voirContact",id,message,date,null));
        }

        return lastMessages;
    }

    public List<LocalForConversation> getMessagesBySenderIdOrReceiverId(String id) throws IOException {
        List<LocalForConversation> localForConversations = new ArrayList<>();
        Local local = getMessagesReceivedSentLocal();
        List<SentMessage> sortedSentMessages = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sortedSentMessages = local.getSent_messages().stream()
                    .filter(sentMessage -> sentMessage.getId_receiver().equals(id))
                    .sorted(Comparator.comparing(SentMessage::getDate_writing).reversed())
                    .collect(Collectors.toList());
        }
        List<ReceivedMessage> sortedReceivedMessages = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sortedReceivedMessages = local.getReceived_messages().stream()
                    .filter(receivedMessage -> receivedMessage.getId_sender().equals(id))
                    .sorted(Comparator.comparing(ReceivedMessage::getDate_writing).reversed())
                    .collect(Collectors.toList());
        }

        for (SentMessage sentMessage : sortedSentMessages) {
            LocalForConversation localForConversation = new LocalForConversation();
            localForConversation.setContenu(sentMessage.getTexte());
            localForConversation.setAuteur(local.getId_local());
            localForConversation.setDestinataire(sentMessage.getId_receiver());
            localForConversation.setDate_writing(sentMessage.getDate_writing());
            localForConversation.setDate_received(sentMessage.getDate_received());
            localForConversations.add(localForConversation);
        }

        for (ReceivedMessage receivedMessage : sortedReceivedMessages) {
            LocalForConversation localForConversation = new LocalForConversation();
            localForConversation.setContenu(receivedMessage.getTexte());
            localForConversation.setAuteur(receivedMessage.getId_sender());
            localForConversation.setDestinataire(local.getId_local());
            localForConversation.setDate_writing(receivedMessage.getDate_writing());
            localForConversation.setDate_received(receivedMessage.getDate_received());
            localForConversations.add(localForConversation);
        }
Collections.reverse(localForConversations);
        return localForConversations;
    }



    public void addLocalSentMessage(String id,String texte) throws IOException {

        Local local = getMessagesReceivedSentLocal();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
           local.getSent_messages().add(new SentMessage(id,texte, formattedDateTime,"null"));
        }
        ReceivedSentLocalDaoImpl.writeToJsonFile(context,local);

       String string =  ReceivedSentLocalDaoImpl.readInternalFile(context,"Local.json");
        System.out.println(string);
    }

}
