package com.example.slowvf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class IdentificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);

        // Obtenir les références des vues dans le fichier de mise en page
        Button signupButton = findViewById(R.id.signup_button);
        Button importData = findViewById(R.id.import_data);

        // Ajouter un écouteur de clic pour le bouton d'inscription
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code à exécuter lorsque l'utilisateur clique sur le bouton "Inscription"
                Intent intent = new Intent(IdentificationActivity.this, Contacts.class);
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