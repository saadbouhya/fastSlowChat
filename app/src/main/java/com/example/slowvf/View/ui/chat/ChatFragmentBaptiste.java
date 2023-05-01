package com.example.slowvf.View.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.slowvf.Controller.IdentificationController;
import com.example.slowvf.R;
import com.example.slowvf.View.Chat.conversation.MessageListActivity;
import com.example.slowvf.databinding.FragmentChatBaptisteBinding;

public class ChatFragmentBaptiste extends Fragment {
    private IdentificationController identificationController;

    private FragmentChatBaptisteBinding binding;
    private Button Conversation1;
    private Button Conversation2;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChatViewModel chatViewModel =
                new ViewModelProvider(this).get(ChatViewModel.class);
        identificationController = new IdentificationController(getContext());

        binding = FragmentChatBaptisteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Conversation1 = root.findViewById(R.id.conversation_button);
        Conversation1.setText("Theo");
        // Set OnCLickListener() using anonymous class:
        Conversation1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                identificationController.deleteFile("Local.json",getContext());
                Intent myIntent = new Intent(v.getContext(), MessageListActivity.class);
                myIntent.putExtra("key", "Theo"); //Optional parameters
                v.getContext().startActivity(myIntent);
            }
        });

        Conversation2 = root.findViewById(R.id.conversation2_button);
        Conversation2.setText("Alice");
        // Set OnCLickListener() using anonymous class:
        Conversation2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent myIntent = new Intent(v.getContext(), MessageListActivity.class);
                myIntent.putExtra("key", "Alice"); //Optional parameters
                v.getContext().startActivity(myIntent);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}