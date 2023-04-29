package com.example.slowvf.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slowvf.Controller.chatController;
import com.example.slowvf.R;
import com.example.slowvf.View.Adapters.CustomAdapterSent;

import java.io.IOException;


public class SentFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public SentFragment(){}

    public static SentFragment newInstance() {
        return (new SentFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        chatController chatController = null;
        try {
            chatController = new chatController(getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        View view =  inflater.inflate(R.layout.fragment_sent, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewEmis);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        try {
            mAdapter = new CustomAdapterSent(chatController.getMessagesReceivedSentLocal());
        } catch (IOException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(mAdapter);

        return view;

    }
}