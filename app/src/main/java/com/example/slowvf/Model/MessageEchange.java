package com.example.slowvf.Model;

public class MessageEchange {
    private String idSender;
    private String idReceiver;
    private String dateWriting;
    private String messageText;
    private String dateReceived;

    public MessageEchange(String idSender, String idReceiver, String dateWriting, String messageText, String dateReceived) {
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.dateWriting = dateWriting;
        this.messageText = messageText;
        this.dateReceived = dateReceived;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public String getDateWriting() {
        return dateWriting;
    }

    public void setDateWriting(String dateWriting) {
        this.dateWriting = dateWriting;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }
}