package com.example.slowvf.Controller;

import android.content.Context;

import com.example.slowvf.Dao.ContactDao;
import com.example.slowvf.Dao.Impl.ContactDaoImpl;
import com.example.slowvf.Model.Contact;

import java.util.ArrayList;

public class ContactController {
    private ContactDao  contactDao;

    public ContactController(Context context) {
        this.contactDao = new ContactDaoImpl(context);
    }

    public void create(Contact contact, Context context) {
       contactDao.create(contact, context);
    }

    public void update(Contact oldContact, Contact newContact, Context context) {
        contactDao.update(context, oldContact, newContact);
    }

    public void delete(Contact contactToDelete, Context context) {
        contactDao.delete(context, contactToDelete);
    }

    public Contact find(Contact contactToFind, Context context) {
        String id = contactToFind.getId();

        return contactDao.find(context, id);
    }

    public ArrayList<Contact> findAll(Context context) {
        return contactDao.findAll(context);
    }
}
