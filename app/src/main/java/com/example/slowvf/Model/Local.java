package com.example.slowvf.Model;
import java.util.ArrayList;
import java.util.List;

public class Local {
    private String idLocal;
    private List<SentMessage> sentMessages;
    private List<ReceivedMessage> receivedMessages;
    private String sizeFile;

    public Local(String idLocal,String sizeFile) {
        this.idLocal = idLocal;
        this.sentMessages = new ArrayList<>();
        this.receivedMessages = new ArrayList<>();
        this.sizeFile = sizeFile;
    }

    public String getIdLocal() {
        return idLocal;
    }

    public List<SentMessage> getSentMessages() {
        return sentMessages;
    }

    public List<ReceivedMessage> getReceivedMessages() {
        return receivedMessages;
    }

}