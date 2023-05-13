package com.example.slowvf.View.Chat.conversation;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slowvf.Controller.ChatController;
import com.example.slowvf.Model.Local;
import com.example.slowvf.Model.LocalForConversation;
import com.example.slowvf.R;
import com.example.slowvf.View.Adapters.CustomAdapterChat;
import com.example.slowvf.View.Adapters.CustomAdapterSent;
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
            chatController = new ChatController(getApplicationContext());
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
            if (message.isEmpty()) {
                // Afficher un pop-up si le message est vide
                Toast.makeText(getApplicationContext(), "Le message ne peut pas être vide", Toast.LENGTH_SHORT).show();
                return;
            }

            // Envoyer le message ici (utiliser chatController ou tout autre moyen nécessaire)
            try {
                finalChatController.addLocalSentMessage(value,message);
                finalChatController.addEchangeMessage(value,message);
                List<LocalForConversation> localForConversations = finalChatController.getMessagesBySenderIdOrReceiverId(value);
                mMessageAdapter.setmLocalForConversationList(localForConversations);
                mMessageAdapter.notifyDataSetChanged();
                CustomAdapterChat customAdapterChat = new CustomAdapterChat();
                customAdapterChat.updateData(finalChatController.getLastMessagesForUniqueSendersAndReceivers());
                CustomAdapterSent customAdapterSent = new CustomAdapterSent();
                customAdapterSent.updateDataSent(finalChatController.getMessagesReceivedSentLocal());
                mMessageRecycler.smoothScrollToPosition(localForConversations.size() - 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Effacer le texte dans l'EditText après l'envoi
            messageEditText.setText("");
        });


    }
}
