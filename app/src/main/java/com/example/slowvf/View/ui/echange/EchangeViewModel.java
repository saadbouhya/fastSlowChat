package com.example.slowvf.View.ui.echange;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EchangeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EchangeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment de l'échange");
    }

    public LiveData<String> getText() {
        return mText;
    }
}