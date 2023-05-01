package com.example.slowvf.View.Chat.conversation;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slowvf.Controller.chatController;
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
        chatController chatController = null;
        try {
            chatController = new chatController(this);
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

        ConversationReader messageReader = new ConversationReader(value,this);
        try {
            List<LocalForConversation> localForConversations = chatController.getMessagesBySenderIdOrReceiverId(value);
            mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
            mMessageAdapter = new MessageListAdapter(this, localForConversations, value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
    }
}
