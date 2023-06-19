package com.example.slowvf.Model;

import java.util.List;

public class LocalMessageList {
    List<LocalForMessage> localForMessages;

    public LocalMessageList(List<LocalForMessage> localForMessages) {
        this.localForMessages = localForMessages;
    }

    public List<LocalForMessage> getLocalForMessages() {
        return localForMessages;
    }

    public void setLocalForMessages(List<LocalForMessage> localForMessages) {
        this.localForMessages = localForMessages;
    }
}
