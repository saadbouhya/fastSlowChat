package com.example.slowvf.View.Chat.conversation;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.slowvf.Model.LocalForMessage;
import com.example.slowvf.R;
import com.example.slowvf.View.Contact.AddEditContact;

import java.util.concurrent.atomic.AtomicReference;

public class MessageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_message);

        AtomicReference<Intent> intent = new AtomicReference<>(getIntent());

        String keyString = intent.get().getStringExtra("keyString");
        Button button = findViewById(R.id.reply_button);

        if (keyString != null) {
            if (keyString.equals("sent")) {
                // rendre le bouton invisible
                button.setVisibility(View.INVISIBLE);
            }
        }


        LocalForMessage local = (LocalForMessage) intent.get().getSerializableExtra("key");

        if (local.getNameProfil().equals("Inconnu")){
            button.setText("Ajouter aux contacts");
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Message");

        TextView nom = findViewById(R.id.message_author);
        TextView id = findViewById(R.id.message_id);
        TextView texte = findViewById(R.id.message_content);

        nom.setText(local.getNameProfil());
        id.setText(local.getId());
        texte.setText(local.getMessage());
        final String myValue = "Ma valeur personnalisée";

        button.setOnClickListener(view -> {
            Intent intentNew;
            if (button.getText().equals("Ajouter aux contacts")) {
                intentNew = new Intent(view.getContext(), AddEditContact.class);
                intentNew.putExtra("idAdd", id.getText().toString());
            } else {
                intentNew = new Intent(view.getContext(), NewMessageActivity.class);
                intentNew.putExtra("nomContact", nom.getText().toString());
            }

            view.getContext().startActivity(intentNew);
        });

    }
}
