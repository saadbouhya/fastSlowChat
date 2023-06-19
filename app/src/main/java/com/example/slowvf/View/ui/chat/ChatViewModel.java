package com.example.slowvf.View.ui.chat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChatViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ChatViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment du Chat");


        // TODO: bind conversation so variables update in realtime
    }

    public LiveData<String> getText() {
        return mText;
    }
}