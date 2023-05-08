package com.example.slowvf.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageEchange {
    private String idSender;
    private String idReceiver;
    private String dateWriting;
    private String messageText;
    private String dateReceived;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageEchange that = (MessageEchange) o;

        if (!idSender.equals(that.idSender)) return false;
        if (!idReceiver.equals(that.idReceiver)) return false;
        if (!dateWriting.equals(that.dateWriting)) return false;
        return messageText.equals(that.messageText);
    }

    @Override
    public int hashCode() {
        int result = idSender.hashCode();
        result = 31 * result + idReceiver.hashCode();
        result = 31 * result + dateWriting.hashCode();
        result = 31 * result + messageText.hashCode();
        return result;
    }
}
