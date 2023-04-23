package com.example.slowvf.View.conversation;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slowvf.R;

import java.io.IOException;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Intent intent = getIntent();
        String value = intent.getStringExtra("key"); //if it's a string you stored.

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(value);

        ConversationReader messageReader = new ConversationReader(value,this);
        try {
            List<Message> messages = messageReader.lireMessages();
            mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
            mMessageAdapter = new MessageListAdapter(this, messages, value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
    }
}
