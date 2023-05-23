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

public class CustomAdapterReceived extends RecyclerView.Adapter<CustomAdapterReceived.ViewHolder> {

    private Local localDataSet;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView id;
        private final TextView message;
        private final TextView dateWriting;
        private final TextView dateReceived;

        public TextView getName() {
            return name;
        }

        public TextView getId() {
            return id;
        }

        public TextView getMessage() {
            return message;
        }

        public TextView getDateWriting() {
            return dateWriting;
        }

        public TextView getDateReceived() {
            return dateReceived;
        }

        public ViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.textView);
            id = (TextView) view.findViewById(R.id.textView2);
            message = (TextView) view.findViewById(R.id.textView3);
            dateWriting = (TextView) view.findViewById(R.id.textView7);
            dateReceived = (TextView) view.findViewById(R.id.textView4);
        }

    }

    public CustomAdapterReceived(Local dataSet) {
        localDataSet = dataSet;
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
                LocalForMessage local = new LocalForMessage(nom.getText().toString(),id.getText().toString(),texte.getText().toString(),dateWriting.getText().toString(),dateReceived.getText().toString());
                Intent myIntent = new Intent(view.getContext(), MessageDetailActivity.class);
                myIntent.putExtra("keyString", "received");
                myIntent.putExtra("key", local);
                view.getContext().startActivity(myIntent);
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String text = localDataSet.getReceivedMessages().get(position).getTexte();
        String id = localDataSet.getReceivedMessages().get(position).getIdSender();
        String date_writing = localDataSet.getReceivedMessages().get(position).getDateWriting();
        String date_received = localDataSet.getReceivedMessages().get(position).getDateReceived();
        //ContactController contactController = new ContactController();
        String pseudo = "Pseudo(voir contact)";
        ContactController contactController = new ContactController(context);
        Contact contact = contactController.find(id,context);
        if (contact!= null){
            viewHolder.getName().setText(contact.getFirstName()+" "+contact.getLastName());
        } else viewHolder.getName().setText(pseudo);
        viewHolder.getMessage().setText(text);
        viewHolder.getId().setText(id);
        viewHolder.getDateWriting().setText(date_writing.substring(0, 10));
        viewHolder.getDateReceived().setText(date_received.substring(0, 10));
    }

    @Override
    public int getItemCount() {
        return localDataSet.getReceivedMessages().size();
    }
}