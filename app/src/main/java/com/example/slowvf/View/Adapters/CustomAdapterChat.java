package com.example.slowvf.View.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.slowvf.Controller.ContactController;
import com.example.slowvf.Model.Contact;
import com.example.slowvf.Model.LocalForMessage;
import com.example.slowvf.R;
import com.example.slowvf.View.Chat.conversation.ConversationActivity;

import java.util.List;

public class CustomAdapterChat extends RecyclerView.Adapter<CustomAdapterChat.ViewHolder> {

    private List<LocalForMessage> localDataSet;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView id;
        private final TextView message;
        private final TextView date_writing;

        public TextView getName() {
            return name;
        }

        public TextView getId() {
            return id;
        }

        public TextView getMessage() {
            return message;
        }

        public TextView getDate_writing() {
            return date_writing;
        }

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textView);
            id = (TextView) view.findViewById(R.id.textView4);
            message = (TextView) view.findViewById(R.id.textView52);
            date_writing = (TextView) view.findViewById(R.id.textView5);
        }
    }

    public CustomAdapterChat(List<LocalForMessage> dataSet) {
        localDataSet = dataSet;
    }

    public CustomAdapterChat() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_item, viewGroup, false);
        LinearLayout linearLayout = view.findViewById(R.id.linear_layout_chat);
        context = viewGroup.getContext();

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView myEditText = view.findViewById(R.id.textView4);
                String text = myEditText.getText().toString();

                Intent myIntent = new Intent(view.getContext(), ConversationActivity.class);
                myIntent.putExtra("key", text); //Optional parameters
                view.getContext().startActivity(myIntent);
            }
        });

        return new ViewHolder(view);
    }
    public void updateData(List<LocalForMessage> dataSet) {
        localDataSet = dataSet;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String text = localDataSet.get(position).getMessage();
        String id = localDataSet.get(position).getId();
        String date_writing = localDataSet.get(position).getDateWriting();

        String pseudo = "Inconnu";
        ContactController contactController = new ContactController(context);
        Contact contact = contactController.find(id,context);
        if (contact!= null){
            viewHolder.getName().setText(contact.getFirstName()+" "+contact.getLastName());
        } else viewHolder.getName().setText(pseudo);
        viewHolder.getMessage().setText(text);
        viewHolder.getId().setText(id);
        viewHolder.getDate_writing().setText(date_writing.substring(0, 10));

    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}