package com.example.slowvf;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Identification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Créer un layout principal pour l'activité
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(getResources().getColor(android.R.color.black));

        // Créer un TextView pour le titre
        TextView titleTextView = new TextView(this);
        titleTextView.setText("Inscription");
        titleTextView.setGravity(Gravity.CENTER);
        titleTextView.setTextColor(getResources().getColor(android.R.color.white));
        titleTextView.setTextSize(24);

        // Ajouter le titre au layout principal
        mainLayout.addView(titleTextView);

        // Créer un EditText pour la saisie
        EditText inputEditText = new EditText(this);
        inputEditText.setHint("Entrez votre nom");
        inputEditText.setTextColor(getResources().getColor(android.R.color.white));
        inputEditText.setHintTextColor(getResources().getColor(android.R.color.white));

        // Ajouter l'EditText au layout principal
        mainLayout.addView(inputEditText);

        // Créer un bouton "Inscription"
        Button inscriptionButton = new Button(this);
        inscriptionButton.setText("Inscription");
        inscriptionButton.setTextColor(getResources().getColor(android.R.color.white));
        inscriptionButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code à exécuter lorsque l'utilisateur clique sur le bouton "Inscription"
            }
        });

        // Ajouter le bouton "Inscription" au layout principal
        mainLayout.addView(inscriptionButton);

        // Créer un bouton en bas de la page
        Button bottomButton = new Button(this);
        bottomButton.setText("Autre bouton");
        bottomButton.setTextColor(getResources().getColor(android.R.color.white));
        bottomButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
        bottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code à exécuter lorsque l'utilisateur clique sur le bouton en bas de la page
            }
        });

        // Ajouter le bouton en bas de la page au layout principal
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.BOTTOM;
        mainLayout.addView(bottomButton, params);

        // Afficher le layout principal dans l'activité
        setContentView(mainLayout);
    }
}