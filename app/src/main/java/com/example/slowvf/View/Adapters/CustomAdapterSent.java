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
import com.example.slowvf.Model.Local;
import com.example.slowvf.Model.LocalForMessage;
import com.example.slowvf.R;
import com.example.slowvf.View.Chat.conversation.MessageDetailActivity;

public class CustomAdapterSent extends RecyclerView.Adapter<CustomAdapterSent.ViewHolder> {

    private Local localDataSet;
    private Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView id;
        private final TextView message;
        private final TextView date_writing;
        private final TextView date_received;

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

        public TextView getDate_received() {
            return date_received;
        }

        public ViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.textView);
            id = (TextView) view.findViewById(R.id.textView2);
            message = (TextView) view.findViewById(R.id.textView3);
            date_writing = (TextView) view.findViewById(R.id.textView7);
            date_received = (TextView) view.findViewById(R.id.textView4);

        }


    }
    public CustomAdapterSent(Local dataSet) {
        localDataSet = dataSet;
    }
    public void updateDataSent(Local dataSet) {
        localDataSet = dataSet;
        notifyDataSetChanged();
    }

    public CustomAdapterSent() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.received_sent_item, viewGroup, false);
        LinearLayout linearLayout = view.findViewById(R.id.linear_layout_sent_received);
        context = viewGroup.getContext();
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView nom = view.findViewById(R.id.textView);
                TextView id = view.findViewById(R.id.textView2);
                TextView dateWriting = view.findViewById(R.id.textView7);
                TextView dateReceived = view.findViewById(R.id.textView4);
                TextView texte = view.findViewById(R.id.textView3);
                LocalForMessage informationsMessage = new LocalForMessage(nom.getText().toString(),id.getText().toString(),texte.getText().toString(),dateWriting.getText().toString(),dateReceived.getText().toString());
                Intent myIntent = new Intent(view.getContext(), MessageDetailActivity.class);
                myIntent.putExtra("keyString", "sent");
                myIntent.putExtra("key", informationsMessage); //Optional parameters
                view.getContext().startActivity(myIntent);
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        int index = localDataSet.getSentMessages().size() - 1 - position;
        String text = localDataSet.getSentMessages().get(index).getTexte();
        String id = localDataSet.getSentMessages().get(index).getIdReceiver();
        String date_writing = localDataSet.getSentMessages().get(index).getDateWriting();
        String date_received = localDataSet.getSentMessages().get(index).getDateReceived();

        String pseudo = "Pseudo(voir contact)";
        ContactController contactController = new ContactController(context);
        Contact contact = contactController.find(id,context);
        if (contact!= null){
            viewHolder.getName().setText(contact.getFirstName()+" "+contact.getLastName());
        } else viewHolder.getName().setText(pseudo);
        viewHolder.getMessage().setText(text);
        viewHolder.getId().setText(id);
        viewHolder.getDate_writing().setText(date_writing.substring(0, 10));
        if (date_received.equals("null")) {
            viewHolder.getDate_received().setText(date_received);
        }
        else {
            viewHolder.getDate_received().setText(date_received.substring(0, 10));
        }
    }


    @Override
    public int getItemCount() {
        return localDataSet.getSentMessages().size();
    }
}