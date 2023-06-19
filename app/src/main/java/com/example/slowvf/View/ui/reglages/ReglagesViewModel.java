package com.example.slowvf.View.ui.reglages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReglagesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ReglagesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment des réglages");
    }

    public LiveData<String> getText() {
        return mText;
    }
}