package com.example.slowvf.View.Exchange.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.slowvf.Controller.ChatController;
import com.example.slowvf.R;
import com.example.slowvf.View.Adapters.CustomAdapterReceived;

import java.io.IOException;


public class ReceivedFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public ReceivedFragment() throws IOException {}

    public static ReceivedFragment newInstance() throws IOException {
        return (new ReceivedFragment());
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

        View view = inflater.inflate(R.layout.fragment_received, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        try {
            mAdapter = new CustomAdapterReceived(chatController.getMessagesReceivedSentLocal());
            System.out.println(chatController.getUniqueIdSendersAndReceivers());
            chatController.getLastMessagesForUniqueSendersAndReceivers();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(mAdapter);

        return view;
    }
}