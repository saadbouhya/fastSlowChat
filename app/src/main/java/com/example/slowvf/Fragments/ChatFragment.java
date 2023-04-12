package com.example.slowvf.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slowvf.Adapters.CustomAdapterChat;
import com.example.slowvf.R;


public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public ChatFragment(){}
    private String[] myDataset = {"Théo", "Pierre", "Louis","Saad", "Walid", "Baptiste","Mouad", "Pierre", "Louis","Théo", "Pierre", "Louis","Théo", "Pierre", "Louis","Théo", "Pierre", "Louis","Théo", "Pierre", "Louis","Théo", "Pierre", "Louis"};

    public static ChatFragment newInstance() {
        return (new ChatFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewChat);


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        mAdapter = new CustomAdapterChat(myDataset);
        recyclerView.setAdapter(mAdapter);

        return view;

    }
}