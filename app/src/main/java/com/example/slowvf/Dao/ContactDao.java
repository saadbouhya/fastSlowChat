package com.example.slowvf.Dao;

import android.content.Context;

import com.example.slowvf.Model.Contact;

import java.util.ArrayList;

public interface ContactDao {
    public void create(Contact contact, Context context);
    public void update(Context context, Contact oldContact, Contact newContact);
    public void delete(Context context, Contact contactToDelete);
    public Contact find(Context context, String id);
    public ArrayList<Contact> findAll(Context context);
}
