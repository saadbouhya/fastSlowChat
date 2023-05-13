package com.example.slowvf.View.Chat.conversation;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slowvf.Controller.ChatController;
import com.example.slowvf.Model.LocalForConversation;
import com.example.slowvf.Model.LocalForMessage;
import com.example.slowvf.R;
import com.example.slowvf.View.Adapters.MessageListAdapter;

import java.io.IOException;
import java.util.List;

public class MessageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_message);

        Intent intent = getIntent();

        String keyString = intent.getStringExtra("keyString");
        Button button = findViewById(R.id.reply_button);

        if (keyString != null) {
            if (keyString.equals("sent")) {
                // rendre le bouton invisible
                button.setVisibility(View.INVISIBLE);
            }
        }


        LocalForMessage local = (LocalForMessage)intent.getSerializableExtra("key");

        if (local.getId().equals("Inconnu")){
            button.setText("Ajouter aux contacts");
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Message");

        TextView nom = findViewById(R.id.message_author);
        TextView id = findViewById(R.id.message_id);
        TextView texte = findViewById(R.id.message_content);

        nom.setText(local.getName2());
        id.setText(local.getId());
        texte.setText(local.getMessage());


    }
}
