package com.example.slowvf.Dao;

import com.example.slowvf.Model.InAppMessage;

import java.util.List;

public interface ExchangeDao {

     void addMessage(InAppMessage message);
     void updateMessage(InAppMessage message);
     boolean messageExist(InAppMessage message);
     List<InAppMessage> getExchangeMessages();
     List<InAppMessage> getLocalMessages();
}
