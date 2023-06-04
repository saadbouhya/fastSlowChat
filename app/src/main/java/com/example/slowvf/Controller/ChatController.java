package com.example.slowvf.Controller;

import android.content.Context;
import android.os.Build;

import com.example.slowvf.Dao.Impl.ReceivedSentEchangelDaoImpl;
import com.example.slowvf.Dao.Impl.ReceivedSentLocalDaoImpl;
import com.example.slowvf.Model.Echange;
import com.example.slowvf.Model.Local;
import com.example.slowvf.Model.LocalForConversation;
import com.example.slowvf.Model.LocalForMessage;
import com.example.slowvf.Model.MessageEchange;
import com.example.slowvf.Model.ReceivedMessage;
import com.example.slowvf.Model.SentMessage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChatController {
private Context context;
private static ChatController chatController;

    public ChatController(Context context) {
        this.context = context;
    }

    // Méthode statique pour récupérer l'instance unique de la classe
    public static ChatController getInstance(Context context) throws IOException {
        if(chatController == null) {
            chatController = new ChatController(context);
        }
        return chatController;
    }

    public Local getMessagesReceivedSentLocal() throws IOException {
        return ReceivedSentLocalDaoImpl.localFile(context);
    }
    public void createFileLocalOnInternalStorage() throws IOException {
         ReceivedSentLocalDaoImpl.createFileOnInternalStorage(context);
    }
    public void createFileEchangeOnInternalStorage() throws IOException {
       ReceivedSentEchangelDaoImpl.createFileEchangeOnInternalStorage(context);
    }
    public String readInternalFile(String fileName) throws IOException {
        return ReceivedSentLocalDaoImpl.readInternalFile(context,fileName);
    }



    public Echange getMessagesEchange() throws IOException {

        return ReceivedSentEchangelDaoImpl.echangeFile(context);
    }

    public List<String> getUniqueIdSendersAndReceivers() throws IOException {
        Set<String> sendersAndReceivers = new HashSet<>();
        Local local = getMessagesReceivedSentLocal();
        if (local != null) {
        for (SentMessage sentMessage : local.getSentMessages()) {
            sendersAndReceivers.add(sentMessage.getIdReceiver());
        }

        for (ReceivedMessage receivedMessage : local.getReceivedMessages()) {
            sendersAndReceivers.add(receivedMessage.getIdSender());
        }

        return new ArrayList<>(sendersAndReceivers);}
        else throw new IOException("localFile is null");
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
        for (SentMessage sentMessage : local.getSentMessages()) {
            String id_receiver = sentMessage.getIdReceiver();
            String message = sentMessage.getTexte();
            String date = sentMessage.getDateWriting();

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
        for (ReceivedMessage receivedMessage : local.getReceivedMessages()) {
            String id_sender = receivedMessage.getIdSender();
            String message = receivedMessage.getTexte();
            String date = receivedMessage.getDateWriting();

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
            sortedSentMessages = local.getSentMessages().stream()
                    .filter(sentMessage -> sentMessage.getIdReceiver().equals(id))
                    .sorted(Comparator.comparing(SentMessage::getDateWriting).reversed())
                    .collect(Collectors.toList());
        }
        List<ReceivedMessage> sortedReceivedMessages = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sortedReceivedMessages = local.getReceivedMessages().stream()
                    .filter(receivedMessage -> receivedMessage.getIdSender().equals(id))
                    .sorted(Comparator.comparing(ReceivedMessage::getDateWriting).reversed())
                    .collect(Collectors.toList());
        }

        if(sortedSentMessages != null) {
            for (SentMessage sentMessage : sortedSentMessages) {
                LocalForConversation localForConversation = new LocalForConversation();
                localForConversation.setContenu(sentMessage.getTexte());
                localForConversation.setAuteur(local.getIdLocal());
                localForConversation.setDestinataire(sentMessage.getIdReceiver());
                localForConversation.setDateWriting(sentMessage.getDateWriting());
                localForConversation.setDateReceived(sentMessage.getDateReceived());
                localForConversations.add(localForConversation);
            }
        }

        if(sortedReceivedMessages != null) {
            for (ReceivedMessage receivedMessage : sortedReceivedMessages) {
                LocalForConversation localForConversation = new LocalForConversation();
                localForConversation.setContenu(receivedMessage.getTexte());
                localForConversation.setAuteur(receivedMessage.getIdSender());
                localForConversation.setDestinataire(local.getIdLocal());
                localForConversation.setDateWriting(receivedMessage.getDateWriting());
                localForConversation.setDateReceived(receivedMessage.getDateReceived());
                localForConversations.add(localForConversation);
            }
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
           local.getSentMessages().add(new SentMessage(id,texte, formattedDateTime,"null"));
        }
        ReceivedSentLocalDaoImpl.writeToJsonFile(context,local);

       String string =  ReceivedSentLocalDaoImpl.readInternalFile(context,"Local.json");
    }

    public void addEchangeMessage(String id,String texte) throws IOException {

        Echange echange = getMessagesEchange();
        Local local = getMessagesReceivedSentLocal();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            echange.getMessages().add(new MessageEchange(local.getIdLocal(),id, formattedDateTime,texte,"null"));
        }
        ReceivedSentEchangelDaoImpl.writeToJsonFile(context,echange);

        String string =  ReceivedSentLocalDaoImpl.readInternalFile(context,"Echange.json");
    }

}
