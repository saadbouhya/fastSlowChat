package com.example.slowvf.View.Contact;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slowvf.Controller.ContactController;
import com.example.slowvf.Model.Contact;
import com.example.slowvf.R;

public class AddEditContact extends AppCompatActivity {
    private EditText EditTextId, EditTextLastName, EditTextFirstName;
    private Button buttonSaveContact;
    private String id, lastName, firstName;
    ActionBar actionBar;
    private ContactController contactController;
    private Contact newContact;
    private Contact oldContact;
    Boolean isEditMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setTitle("Ajouter un contact");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

       // Init view
       EditTextId = findViewById(R.id.EditTextId);
       EditTextLastName = findViewById(R.id.EditTextLastName);
       EditTextFirstName = findViewById(R.id.EditTextFirstName);
       buttonSaveContact = findViewById(R.id.ButtonSaveContact);

       // Get intent data
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode", false);

        if (isEditMode) {
            actionBar.setTitle("Modifier Contact");

            id = intent.getStringExtra("id");
            lastName = intent.getStringExtra("lastName");
            firstName = intent.getStringExtra("firstName");
            oldContact = new Contact(id, lastName, firstName);

            EditTextId.setText(id);
            EditTextLastName.setText(lastName);
            EditTextFirstName.setText(firstName);
        }

       buttonSaveContact.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               saveData();
               Intent intent = new Intent(AddEditContact.this, Contacts.class);
               startActivity(intent);
           }
       });

       contactController = new ContactController(AddEditContact.this);
    }

    private void saveData() {
        String id = EditTextId.getText().toString();
        String lastName = EditTextLastName.getText().toString();
        String firstName = EditTextFirstName.getText().toString();
        newContact = new Contact(id, lastName, firstName);


        if (!id.isEmpty() || !lastName.isEmpty() || !firstName.isEmpty()) {
            // save
            if (!isEditMode)
                contactController.create(newContact, AddEditContact.this);
            else
                contactController.update(oldContact, newContact, AddEditContact.this);
        } else {
            Toast.makeText(getApplicationContext(), "Champs non complétés", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}