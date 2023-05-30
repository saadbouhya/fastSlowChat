package com.example.slowvf.View.Contact;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.slowvf.Controller.ContactController;
import com.example.slowvf.Model.Contact;
import com.example.slowvf.R;
import com.example.slowvf.View.Adapters.AdapterContact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Contacts extends Fragment {
    private RecyclerView recyclerViewContacts;
    private ArrayList<Contact> contacts;
    private AdapterContact adapterContact;
    private ContactController contactController;

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                    // Refresh the contact list
                    contacts = contactController.findAll(getContext());
                    adapterContact = new AdapterContact(contacts);
                    recyclerViewContacts.setAdapter(adapterContact);
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.activity_contacts, container, false);

        FloatingActionButton buttonAddContact = rootView.findViewById(R.id.buttonAddContact);
        TextView search = rootView.findViewById(R.id.search);
        recyclerViewContacts = rootView.findViewById(R.id.recyclerViewContacts);

        recyclerViewContacts.setHasFixedSize(true);

        buttonAddContact.setOnClickListener(v -> {
            // move to new activity to add contact
            Intent intent = new Intent(getActivity(), AddEditContact.class);
            launcher.launch(intent);
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        contactController = new ContactController(getContext());
        contacts = contactController.findAll(getContext());
        adapterContact = new AdapterContact(contacts);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recyclerViewContacts.setLayoutManager(lm);
        recyclerViewContacts.setAdapter(adapterContact);

        return rootView;
    }

    private void loadData() {
        RecyclerView.Adapter adapterContact = new AdapterContact(contactController.findAll(getContext()));
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recyclerViewContacts.setLayoutManager(lm);
        recyclerViewContacts.setAdapter(adapterContact);
    }

    private void filter(String text) {
        ArrayList<Contact> filteredList = new ArrayList<>();

        for (Contact contact : contacts) {
            if (String.format("%s %s", contact.getLastName(), contact.getFirstName()).toLowerCase().contains(text.toLowerCase()) ) {
                filteredList.add(contact);
            }
        }

        adapterContact.filterList(filteredList);
        recyclerViewContacts.setAdapter(adapterContact);
    }

}