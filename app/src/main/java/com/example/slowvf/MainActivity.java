package com.example.slowvf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtenir les références des vues dans le fichier de mise en page
        EditText nameInput = findViewById(R.id.name_input);
        Button signupButton = findViewById(R.id.signup_button);
        Button otherButton = findViewById(R.id.other_button);

        // Ajouter un écouteur de clic pour le bouton d'inscription
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code à exécuter lorsque l'utilisateur clique sur le bouton "Inscription"
            }
        });

        // Ajouter un écouteur de clic pour le bouton en bas de la page
        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code à exécuter lorsque l'utilisateur clique sur le bouton en bas de la page
            }
        });
    }
}