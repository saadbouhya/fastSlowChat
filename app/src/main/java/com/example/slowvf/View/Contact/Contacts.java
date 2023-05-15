package com.example.slowvf.View.Contact;

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

import com.example.slowvf.Model.Contact;
import com.example.slowvf.R;
import com.example.slowvf.View.Adapters.AdapterContact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Contacts extends Fragment {
    private RecyclerView recyclerViewContacts;
    private ArrayList<Contact> contacts;
    private AdapterContact adapterContact;

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
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recyclerViewContacts.setLayoutManager(lm);
        recyclerViewContacts.setAdapter(adapterContact);

        return rootView;
    }

    private void loadData() {
        RecyclerView.Adapter adapterContact = new AdapterContact(getData());
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recyclerViewContacts.setLayoutManager(lm);
        recyclerViewContacts.setAdapter(adapterContact);
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


        return contacts;
    }
}