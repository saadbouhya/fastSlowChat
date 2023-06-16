package com.example.slowvf.Model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageEchange implements Serializable {
    private String id_sender;
    private String id_receiver;
    private String date_writing;
    private String message_text;
    private String date_received;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageEchange that = (MessageEchange) o;

        if (!id_sender.equals(that.id_sender)) return false;
        if (!id_sender.equals(that.id_sender)) return false;
        if (!date_writing.equals(that.date_writing)) return false;
        return message_text.equals(that.message_text);
    }

    @Override
    public int hashCode() {
        int result = id_sender.hashCode();
        result = 31 * result + id_receiver.hashCode();
        result = 31 * result + date_writing.hashCode();
        result = 31 * result + message_text.hashCode();
        return result;
    }
}
