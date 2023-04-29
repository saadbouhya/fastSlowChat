package com.example.slowvf.View.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.slowvf.Model.Local;
import com.example.slowvf.R;

public class CustomAdapterReceived extends RecyclerView.Adapter<CustomAdapterReceived.ViewHolder> {

    private Local localDataSet;

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

    public CustomAdapterReceived(Local dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.received_sent_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String text = localDataSet.getReceived_messages().get(position).getTexte();
        String id = localDataSet.getReceived_messages().get(position).getId_sender();
        String date_writing = localDataSet.getReceived_messages().get(position).getDate_writing();
        String date_received = localDataSet.getReceived_messages().get(position).getDate_received();

        String pseudo = "Pseudo(voir contact)";

        viewHolder.getMessage().setText(text);
        viewHolder.getId().setText(id);
        viewHolder.getDate_writing().setText(date_writing.substring(0, 10));
        viewHolder.getDate_received().setText(date_received.substring(0, 10));
        viewHolder.getName().setText(pseudo);
    }

    @Override
    public int getItemCount() {
        return localDataSet.getReceived_messages().size();
    }
}