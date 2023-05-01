package com.example.slowvf.View.Contact;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.example.slowvf.Controller.ContactController;
import com.example.slowvf.Model.Contact;
import com.example.slowvf.R;
import com.example.slowvf.View.Adapters.AdapterContact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Contacts extends AppCompatActivity {
    private RecyclerView recyclerViewContacts;
    private ArrayList<Contact> contacts;
    private AdapterContact adapterContact;
    private ContactController contactController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setTitle("Contacts");

        FloatingActionButton buttonAddContact = findViewById(R.id.buttonAddContact);
        TextView search = findViewById(R.id.search);
        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);

        recyclerViewContacts.setHasFixedSize(true);

        buttonAddContact.setOnClickListener(v -> {
            // move to new activity to add contact
            Intent intent = new Intent(Contacts.this, AddEditContact.class);
            startActivity(intent);
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

        contactController = new ContactController(Contacts.this);
        contacts = contactController.findAll(Contacts.this);
        adapterContact = new AdapterContact(contacts);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerViewContacts.setLayoutManager(lm);
        recyclerViewContacts.setAdapter(adapterContact);
    }

    private void loadData() {
        RecyclerView.Adapter adapterContact = new AdapterContact(contactController.findAll(Contacts.this));
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerViewContacts.setLayoutManager(lm);
        recyclerViewContacts.setAdapter(adapterContact);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
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