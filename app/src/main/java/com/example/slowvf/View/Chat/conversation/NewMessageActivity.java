package com.example.slowvf.View.Chat.conversation;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.slowvf.Controller.ChatController;
import com.example.slowvf.Controller.ContactController;
import com.example.slowvf.Model.Contact;
import com.example.slowvf.Model.LocalForConversation;
import com.example.slowvf.R;
import com.example.slowvf.View.Adapters.MessageListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewMessageActivity extends AppCompatActivity {
    private MessageListAdapter mMessageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ChatController chatController = null;
        try {
            chatController = ChatController.getInstance(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.fragment_new_message_contact);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        ContactController contactController = new ContactController(getApplicationContext());
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

        ArrayList<Contact> contacts = contactController.findAll(getApplicationContext());
        // System.out.println(contacts);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.search_bar);

        // Récupérez la liste des contacts (supposons que la variable "contacts" contienne la liste des contacts)
        ArrayList<String> contactNames = new ArrayList<>();
        for (Contact contact : contacts) {
            contactNames.add(contact.getFirstName().concat(" ").concat(contact.getLastName()));
        }

        // Créez un ArrayAdapter avec la liste des noms de contacts
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, contactNames);

        // Configurez l'AutoCompleteTextView avec l'ArrayAdapter
        autoCompleteTextView.setAdapter(adapter);

        EditText messageEditText = findViewById(R.id.contenu);
        AutoCompleteTextView searchBar = findViewById(R.id.search_bar);
        Button sendButton = findViewById(R.id.envoyer);
        Button annulerButton = findViewById(R.id.annuler);
        String nomContact = null;
        nomContact = intent.getStringExtra("nomContact");
        if (nomContact != null){
            searchBar.setText(nomContact);
            nomContact = null;
        }

        ChatController finalChatController = chatController;
        sendButton.setOnClickListener(view -> {
            // Récupération du texte tapé dans l'EditText
            String message = messageEditText.getText().toString();

            String value = null;
            for (Contact contact : contacts) {
                if (contact.getFirstName().concat(" ").concat(contact.getLastName()).equals(searchBar.getText().toString())) {
                    value = contact.getId();
                }
            }
            if (value == null ){
                Toast.makeText(getApplicationContext(), "Le contact n'existe pas", Toast.LENGTH_SHORT).show();
                return;
            }
            if (message.isEmpty()) {
                // Afficher un pop-up si le message est vide
                Toast.makeText(getApplicationContext(), "Le message ne peut pas être vide", Toast.LENGTH_SHORT).show();
                return;
            }

            // Envoyer le message ici (utiliser chatController ou tout autre moyen nécessaire)
            try {
                finalChatController.addLocalSentMessage(value, message);
                finalChatController.addEchangeMessage(value, message);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Effacer le texte dans l'EditText après l'envoi
            messageEditText.setText("");
            Toast.makeText(getApplicationContext(), "Le message est envoyé", Toast.LENGTH_SHORT).show();
        });

        annulerButton.setOnClickListener(view -> {
            messageEditText.setText("");
            searchBar.setText("");
        });
    }
}
