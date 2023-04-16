package com.example.slowvf.ui.contacts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContactsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ContactsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment des contacts");
    }

    public LiveData<String> getText() {
        return mText;
    }
}