package com.example.slowvf.View.Chat.conversation;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slowvf.Controller.ChatController;
import com.example.slowvf.Model.LocalForConversation;
import com.example.slowvf.R;
import com.example.slowvf.View.Adapters.MessageListAdapter;

import java.io.IOException;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ChatController chatController = null;
        try {
            chatController = new ChatController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Intent intent = getIntent();
        String value = intent.getStringExtra("key"); //if it's a string you stored.

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(value);


        try {
            List<LocalForConversation> localForConversations = chatController.getMessagesBySenderIdOrReceiverId(value);
            mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
            mMessageAdapter = new MessageListAdapter(this, localForConversations, value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);

        EditText messageEditText = findViewById(R.id.edit_gchat_message);
        ImageButton sendButton = findViewById(R.id.button_gchat_send);
        ChatController finalChatController = chatController;
        sendButton.setOnClickListener(view -> {
            // Récupération du texte tapé dans l'EditText
            String message = messageEditText.getText().toString();
            System.out.println(message);
            // Envoyer le message ici (utiliser chatController ou tout autre moyen nécessaire)
            try {
                finalChatController.addLocalSentMessage(value,message);
                List<LocalForConversation> localForConversations = finalChatController.getMessagesBySenderIdOrReceiverId(value);
                mMessageAdapter.setmLocalForConversationList(localForConversations);
                mMessageAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Effacer le texte dans l'EditText après l'envoi
            messageEditText.setText("");
        });

    }
}
