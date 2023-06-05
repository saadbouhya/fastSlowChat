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
import com.example.slowvf.View.MainActivityNavigation;

public class AddEditContact extends AppCompatActivity {
    private EditText editTextId, editTextLastName, editTextFirstName;
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
        editTextId = findViewById(R.id.EditTextId);
        editTextLastName = findViewById(R.id.EditTextLastName);
        editTextFirstName = findViewById(R.id.EditTextFirstName);
        buttonSaveContact = findViewById(R.id.ButtonSaveContact);

        // Get intent data
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode", false);
        String idAdd = null;
        idAdd = intent.getStringExtra("idAdd");
        if (idAdd != null) {
            editTextId.setText(idAdd);
            idAdd = null;
        }

        if (isEditMode) {
            actionBar.setTitle("Modifier Contact");

            id = intent.getStringExtra("id");
            lastName = intent.getStringExtra("lastName");
            firstName = intent.getStringExtra("firstName");
            oldContact = new Contact(id, lastName, firstName);

            editTextId.setText(id);
            editTextLastName.setText(lastName);
            editTextFirstName.setText(firstName);
        }

        buttonSaveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextId.getText().toString().isEmpty()) {
                    editTextId.setError("Ce champ est obligatoire.");
                } else if (editTextLastName.getText().toString().isEmpty()) {
                    editTextLastName.setError("Ce champ est obligatoire.");
                } else if (editTextFirstName.getText().toString().isEmpty()) {
                    editTextFirstName.setError("Ce champ est obligatoire.");
                } else {
                    Contact newContact = saveData();

                    // Set the result code and finish the activity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("id",newContact.getId());
                    resultIntent.putExtra("lastName",newContact.getLastName());
                    resultIntent.putExtra("firstName",newContact.getFirstName());
                    setResult(AppCompatActivity.RESULT_OK, resultIntent);
                    finish();
                }
            }
        });

        contactController = new ContactController(AddEditContact.this);
    }

    private Contact saveData() {
        String id = editTextId.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String firstName = editTextFirstName.getText().toString();
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
        return newContact;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}