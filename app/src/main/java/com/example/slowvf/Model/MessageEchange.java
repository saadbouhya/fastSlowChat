package com.example.slowvf.Model;

import java.io.Serializable;

import lombok.Data;

@Data
public class MessageEchange implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageEchange)) return false;

        MessageEchange that = (MessageEchange) o;

        if (getIdSender() != null ? !getIdSender().equals(that.getIdSender()) : that.getIdSender() != null)
            return false;
        if (getIdReceiver() != null ? !getIdReceiver().equals(that.getIdReceiver()) : that.getIdReceiver() != null)
            return false;
        return getDateWriting() != null ? getDateWriting().equals(that.getDateWriting()) : that.getDateWriting() == null;
    }

    @Override
    public int hashCode() {
        int result = getIdSender() != null ? getIdSender().hashCode() : 0;
        result = 31 * result + (getIdReceiver() != null ? getIdReceiver().hashCode() : 0);
        result = 31 * result + (getDateWriting() != null ? getDateWriting().hashCode() : 0);
        return result;
    }
}