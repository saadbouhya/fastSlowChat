package com.example.slowvf.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slowvf.Controller.ChatController;
import com.example.slowvf.View.Adapters.CustomAdapterChat;
import com.example.slowvf.R;

import java.io.IOException;


public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private CustomAdapterChat mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public ChatFragment(){}
   // private String[] myDataset = {"Théo", "Pierre", "Louis","Saad", "Walid", "Baptiste","Mouad", "Pierre", "Louis","Théo", "Pierre", "Louis","Théo", "Pierre", "Louis","Théo", "Pierre", "Louis","Théo", "Pierre", "Louis","Théo", "Pierre", "Louis"};

    public static ChatFragment newInstance() {
        return (new ChatFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ChatController chatController = null;
        try {
            chatController = ChatController.getInstance(getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewChat);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        try {
            mAdapter = new CustomAdapterChat(chatController.getLastMessagesForUniqueSendersAndReceivers());
        } catch (IOException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(mAdapter);

        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        ChatController chatController = null;
        try {
            chatController = ChatController.getInstance(getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mAdapter.updateData(chatController.getLastMessagesForUniqueSendersAndReceivers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}