package com.example.slowvf;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Contacts extends AppCompatActivity {
    private RecyclerView recyclerViewContacts;
    private ArrayList<Contact> contacts;
    private AdapterContact adapterContact;

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

        contacts = getData();
        adapterContact = new AdapterContact(contacts);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerViewContacts.setLayoutManager(lm);
        recyclerViewContacts.setAdapter(adapterContact);
        Log.i("Info", "Data loaded!");
    }

    private void loadData() {
        RecyclerView.Adapter adapterContact = new AdapterContact(getData());
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerViewContacts.setLayoutManager(lm);
        recyclerViewContacts.setAdapter(adapterContact);
        Log.i("Info", "Data loaded!");
    }

    //@Override
    //protected void onResume() {
    //    super.onResume();
    //    loadData();
    //}

    private void filter(String text) {
       ArrayList<Contact> filteredList = new ArrayList<>();

       for (Contact contact : contacts) {
           if (String.format("%s %s", contact.getLastName(), contact.getFirstName()).toLowerCase().contains(text.toLowerCase()) ) {
               filteredList.add(contact);
           }
       }

       adapterContact.filterList(filteredList);
    }

    private ArrayList<Contact> getData() {
        ArrayList<Contact> contacts = new ArrayList<>();

        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Sadik", "Mouad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Duchamps", "Théo"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bortolloti", "Bastite"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Doe", "John"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));
        contacts.add(new Contact("00-B0-D0-63-C2-26glitch26032023", "Bouhya", "Saad"));

        //Log.i("Info", "Data sent");

        return contacts;
    }
}