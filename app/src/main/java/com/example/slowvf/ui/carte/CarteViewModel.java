package com.example.slowvf.ui.carte;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CarteViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CarteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment de la carte");
    }

    public LiveData<String> getText() {
        return mText;
    }
}