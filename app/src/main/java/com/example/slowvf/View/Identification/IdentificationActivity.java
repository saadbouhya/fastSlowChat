package com.example.slowvf.View.Identification;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
            // Ajouter un écouteur de clic pour le bouton d'inscription
            signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    identificationController.createUser(pseudo.getText().toString());
                    startActivity(intent);
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