package com.example.slowvf.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slowvf.Adapters.CustomAdapter;
import com.example.slowvf.R;


public class SentFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String[] myDataset = {"Théo", "Pierre", "Louis","Théo", "Pierre", "Louis","Théo", "Pierre", "Louis","Théo", "Pierre", "Louis","Théo", "Pierre", "Louis","Théo", "Pierre", "Louis","Théo", "Pierre", "Louis","Théo", "Pierre", "Louis"};


    public SentFragment(){}

    public static SentFragment newInstance() {
        return (new SentFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_sent, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewEmis);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new CustomAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);

        return view;

    }
}