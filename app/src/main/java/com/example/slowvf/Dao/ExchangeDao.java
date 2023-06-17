package com.example.slowvf.Dao;

import android.content.Context;

import com.example.slowvf.Model.MessageEchange;

import java.util.List;

public interface ExchangeDao {

     void addUpdateMessage(Context context, MessageEchange message);
     void updateMessage(Context context, MessageEchange message);
     boolean messageExist(Context context, MessageEchange message);
     List<MessageEchange> getExchangeMessages(Context context);
     List<MessageEchange> getLocalMessages(Context context);
}
