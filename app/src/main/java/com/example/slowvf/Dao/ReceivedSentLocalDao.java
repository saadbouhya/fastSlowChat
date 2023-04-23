package com.example.slowvf.Dao;

import android.content.Context;
import com.example.slowvf.model.Local;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReceivedSentLocalDao {

    public static Local localfile(Context context) throws IOException {
        InputStream inputStream = context.getAssets().open("Local.json");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        Gson gson = new Gson();
        Local messageInfo = gson.fromJson(inputStreamReader, Local.class);

        return messageInfo;
    }
}
