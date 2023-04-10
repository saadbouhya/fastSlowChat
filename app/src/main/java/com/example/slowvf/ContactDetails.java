package com.example.slowvf;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class ContactDetails extends AppCompatActivity {

    private TextView textViewId, textViewLastName, textViewFirstName, textViewEdit;
    private Button buttonDeleteContact;
    private String id, lastName, firstName;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setTitle("Contact");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // get data from intent contacts
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        lastName = intent.getStringExtra("lastName");
        firstName = intent.getStringExtra("firstName");


        textViewId = findViewById(R.id.id);
        textViewLastName = findViewById(R.id.lastName);
        textViewFirstName = findViewById(R.id.firstName);
        textViewEdit = findViewById(R.id.edit);
        buttonDeleteContact = findViewById(R.id.deleteContact);

        textViewEdit.setOnClickListener(v -> {
            Log.i("Info", "Edit Button clicked");

            // go to add edit contact  page
            Log.i("Info", "Contact item view clicked");
            Intent intentEditContact = new Intent(ContactDetails.this, AddEditContact.class);
            intentEditContact.putExtra("id", id);
            intentEditContact.putExtra("lastName", lastName);
            intentEditContact.putExtra("firstName", firstName);

            // Pass bool to define that it is for edit
            intentEditContact.putExtra("isEditMode", true);

            startActivity(intentEditContact);
        });

        buttonDeleteContact.setOnClickListener(v -> {
            Log.i("Info", "Delete Contact button clicked");
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ContactDetails.this);

            // Title
            alertDialogBuilder.setTitle("Confirmez-vous la suppression de ce contact?");

            // Dialog msg
            alertDialogBuilder.setMessage("")
                    .setCancelable(false)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // if yes delete contact
                            Log.i("Info", "Delete contact");
                            ContactDetails.this.finish();
                        }
                    }).setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // if no close dialog
                            dialog.cancel();
                        }
                    });

            AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();
        });

        loadDataById();
    }

    private void loadDataById() {
        // Get data from data source

        // Assign values
        textViewId.setText(id);
        textViewLastName.setText(lastName);
        textViewFirstName.setText(firstName);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}