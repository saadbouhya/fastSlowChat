package com.example.slowvf.View.Adapters;

import static java.sql.DriverManager.println;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.slowvf.Controller.ContactController;
import com.example.slowvf.Model.Contact;
import com.example.slowvf.Model.LocalForConversation;
import com.example.slowvf.R;

import java.io.IOException;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<LocalForConversation> mLocalForConversationList;

    private String correspondant;

    public MessageListAdapter(Context context, List<LocalForConversation> localForConversationList, String inCorrespondant) throws IOException {
        mContext = context;
        mLocalForConversationList = localForConversationList;
        correspondant = inCorrespondant;
    }

    public void setmLocalForConversationList(List<LocalForConversation> mLocalForConversationList) {
        this.mLocalForConversationList = mLocalForConversationList;
    }

    @Override
    public int getItemCount() {
        return mLocalForConversationList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        LocalForConversation localForConversation = (LocalForConversation) mLocalForConversationList.get(position);

        println("auteur :" + localForConversation.getAuteur());
        println("correspondant :" + this.correspondant);

        if (localForConversation.getAuteur().equals(this.correspondant)) {
            // If the current user is the sender of the localForConversation
            return VIEW_TYPE_MESSAGE_RECEIVED;
        } else {
            // If some other user sent the localForConversation
            return VIEW_TYPE_MESSAGE_SENT;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.our_message, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.their_message, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LocalForConversation localForConversation = (LocalForConversation) mLocalForConversationList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(localForConversation);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(localForConversation);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_date_me);
        }

        void bind(LocalForConversation localForConversation) {
            messageText.setText(localForConversation.getContenu());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(localForConversation.getDateWriting());
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText,timeText2;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_other);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_other);
           timeText2 =  (TextView) itemView.findViewById(R.id.text_gchat_date_other);
            nameText = (TextView) itemView.findViewById(R.id.text_gchat_user_other);
        }

        void bind(LocalForConversation localForConversation) {
            ContactController contactController = new ContactController(mContext);
            Contact contact = contactController.find(localForConversation.getAuteur(),mContext);
            if (contact!= null){
                nameText.setText(contact.getFirstName()+" "+contact.getLastName());
            } else nameText.setText(localForConversation.getAuteur());
            messageText.setText(localForConversation.getContenu());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(localForConversation.getDateReceived());
            timeText2.setText(localForConversation.getDateWriting());
        }
    }
}