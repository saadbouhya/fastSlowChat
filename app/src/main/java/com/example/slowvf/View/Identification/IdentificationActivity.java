package com.example.slowvf.View.Identification;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.slowvf.Controller.IdentificationController;
import com.example.slowvf.R;
import com.example.slowvf.View.MainActivityNavigation;

public class IdentificationActivity extends AppCompatActivity {
    private IdentificationController identificationController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        identificationController = new IdentificationController(IdentificationActivity.this);
        Intent intent = new Intent(IdentificationActivity.this, MainActivityNavigation.class);
        if(identificationController.fileExists("Local.json",IdentificationActivity.this))
        {
            startActivity(intent);
        }
        else {
            setContentView(R.layout.activity_identification);

            // Obtenir les références des vues dans le fichier de mise en page
            Button signupButton = findViewById(R.id.signup_button);
            Button importData = findViewById(R.id.import_data);
            EditText pseudo = findViewById(R.id.editTextPseudo);
            signupButton.setEnabled(false);
            importData.setEnabled(false);
            // Ajouter un écouteur de clic pour le bouton d'inscription
            signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ActivityCompat.checkSelfPermission(IdentificationActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(IdentificationActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
                    }
                    identificationController.createUser(pseudo.getText().toString());
                    startActivity(intent);
                }
            });

            pseudo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() == 0)
                    {
                        signupButton.setEnabled(false);
                    }else
                    {
                        signupButton.setEnabled(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            // Ajouter un écouteur de clic pour le bouton en bas de la page
            importData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View popupView = LayoutInflater.from(IdentificationActivity.this).inflate(R.layout.old_account, null);

                    // Create the AlertDialog object and set the popup layout as its view
                    AlertDialog.Builder builder = new AlertDialog.Builder(IdentificationActivity.this);
                    builder.setView(popupView);

                    ImageView closeButton = popupView.findViewById(R.id.close_button);
                    // Create and show the AlertDialog
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Dismiss the popup when the Close button is clicked
                            alertDialog.dismiss();
                        }
                    });

                }
            });
        }
    }

}