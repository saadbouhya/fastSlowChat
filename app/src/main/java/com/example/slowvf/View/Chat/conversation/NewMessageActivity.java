package com.example.slowvf.View.Chat.conversation;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.slowvf.R;

public class NewMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.fragment_message_received);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        Intent intent = getIntent();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Nouveau message");

        // Obtenez une référence à votre layout ConstraintLayout
        ConstraintLayout constraintLayout = findViewById(R.id.cardContainer);

        // Obtenez une référence à la racine du layout
        ViewGroup rootView = findViewById(android.R.id.content);

        // Créez une transition automatique avec une durée spécifiée
        AutoTransition autoTransition = new AutoTransition();
        autoTransition.setDuration(500); // Définissez la durée de l'animation en millisecondes

        // Écoutez les événements de changement de clavier
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();

            // Calculez la hauteur visible du contenu de l'écran
            int visibleHeight = screenHeight - r.bottom;

            // Effectuez la transition d'animation en fonction de la hauteur visible
            TransitionManager.beginDelayedTransition(constraintLayout, autoTransition);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);

            if (visibleHeight > screenHeight / 3) {
                // Le clavier est visible, ajustez la position de votre layout ici
                constraintSet.setVerticalBias(R.id.card_layout, 0.3f);
            } else {
                // Le clavier est caché, réinitialisez la position de votre layout ici
                constraintSet.setVerticalBias(R.id.card_layout, 0.5f);
            }

            constraintSet.applyTo(constraintLayout);
        });
    }
}