package com.example.slowvf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdapterContact extends RecyclerView.Adapter<AdapterContact.ContactViewHolder> {

    private ArrayList<Contact> contacts;
    private Context context;


    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView id, fullName;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.i("Info", "ContactViewHolder contstructor");

            id = itemView.findViewById(R.id.id);
            fullName = itemView.findViewById(R.id.fullName);
        }
    }

    public AdapterContact(ArrayList<Contact> contacts) {
        this.contacts = contacts;
        Log.i("Info", "AdapterContact contstructor");
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("Info", "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact_item, parent, false);
        context = parent.getContext();

        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);

        // get data
        holder.fullName.setText(String.format("%s %s", contact.getLastName(), contact.getFirstName()));
        holder.id.setText(contact.getId());

        Log.i("Info", "OnBindViewHolder");

        holder.itemView.setOnClickListener(v -> {
            // go to contact details page
            Log.i("Info", "Contact item view clicked");
            Intent intent = new Intent(context, ContactDetails.class);
            intent.putExtra("id", contact.getId());
            intent.putExtra("lastName", contact.getLastName());
            intent.putExtra("firstName", contact.getFirstName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void filterList(ArrayList<Contact> filteredList) {
        contacts = filteredList;
        this.notifyDataSetChanged();
    }
}