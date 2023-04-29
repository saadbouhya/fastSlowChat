package com.example.slowvf.Controller;

import android.content.Context;

import com.example.slowvf.Dao.Impl.ReceivedSentLocalDao;
import com.example.slowvf.Model.Local;

import java.io.IOException;

//   chatController chatController = new chatController(getApplicationContext()); pour l'appeler
public class chatController {
    private Context context;

    public chatController(Context context) throws IOException {
        this.context = context;
    }

public Local getMessagesReceivedSentLocal() throws IOException {

        return ReceivedSentLocalDao.localfile(context);
}


}
