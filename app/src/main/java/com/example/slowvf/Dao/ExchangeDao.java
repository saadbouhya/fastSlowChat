package com.example.slowvf.Dao;

import android.content.Context;

import com.example.slowvf.Model.MessageEchange;
import com.example.slowvf.Model.ReceivedMessage;
import com.example.slowvf.Model.SentMessage;

import java.util.List;

public interface ExchangeDao {

     void addUpdateExchangeMessage(Context context, MessageEchange message);
     void updateMessage(Context context, MessageEchange updatedMessage);
     boolean messageExist(Context context, MessageEchange message);
     List<MessageEchange> getExchangeMessages(Context context);
     List<MessageEchange> getLocalMessages(Context context);
     void addReceivedMessage(Context context, ReceivedMessage message);
     void updateReceivedMessage(Context context, ReceivedMessage message);
     void addSentMessage(Context context, SentMessage message);
     void updateSentMessage(Context context, SentMessage message);
     List<ReceivedMessage> getReceivedMessages(Context context);
     List<SentMessage> getSentMessages(Context context);
     void saveSentMessages(Context context, List<SentMessage> messages);
     void saveReceivedMessages(Context context, List<ReceivedMessage> messages);
}
