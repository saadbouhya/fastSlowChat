package com.example.slowvf.Dao;

import android.content.Context;

import com.example.slowvf.Model.MessageEchange;

import java.util.List;

public interface ExchangeDao {

     void addMessage(Context context, MessageEchange message, boolean isLocal);
     void updateMessage(Context context, MessageEchange message, boolean isLocal);
     boolean messageExist(Context context, MessageEchange message, boolean isLocal);
     List<MessageEchange> getExchangeMessages(Context context);
     List<MessageEchange> getLocalMessages(Context context);
}
