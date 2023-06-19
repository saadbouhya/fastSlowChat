package com.example.slowvf.View.Contact;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.slowvf.Controller.ContactController;
import com.example.slowvf.Model.Contact;
import com.example.slowvf.R;
import com.example.slowvf.View.Adapters.AdapterContact;

public class ContactDetails extends AppCompatActivity {

    private TextView textViewId, textViewLastName, textViewFirstName, textViewEdit;
    private Button buttonDeleteContact;
    private String id, lastName, firstName;
    ActionBar actionBar;
    private ContactController contactController;
    private Contact contact;

    private ActivityResultLauncher<Intent> launcher2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                    Bundle data = result.getData().getExtras();
                    if (data != null) {
                        this.id = data.getString("id");
                        this.lastName = data.getString("lastName");
                        this.firstName = data.getString("firstName");
                        loadDataById();
                        // Set the result code and finish the activity
                        setResult(AppCompatActivity.RESULT_OK);
                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        // get data from intent contacts
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        lastName = intent.getStringExtra("lastName");
        firstName = intent.getStringExtra("firstName");
        contact = new Contact(id, lastName, firstName);
        contactController = new ContactController(ContactDetails.this);

        actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setTitle(firstName);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        textViewId = findViewById(R.id.id);
        textViewLastName = findViewById(R.id.lastName);
        textViewFirstName = findViewById(R.id.firstName);
        textViewEdit = findViewById(R.id.edit);
        buttonDeleteContact = findViewById(R.id.deleteContact);

        textViewEdit.setOnClickListener(v -> {

            // go to add edit contact  page
            Intent intentEditContact = new Intent(ContactDetails.this, AddEditContact.class);
            intentEditContact.putExtra("id", id);
            intentEditContact.putExtra("lastName", lastName);
            intentEditContact.putExtra("firstName", firstName);

            // Pass bool to define that it is for edit
            intentEditContact.putExtra("isEditMode", true);
            launcher2.launch(intentEditContact);
        });

        buttonDeleteContact.setOnClickListener(v -> {
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
                            contactController.delete(contact, ContactDetails.this);
                            setResult(AppCompatActivity.RESULT_OK, new Intent());
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