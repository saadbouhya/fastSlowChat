package com.example.slowvf.Dao;

import android.content.Context;

public interface IdentificationDao {

    void createUser(String pseudo, String macAdresse, String actualDate);
    void deleteFile(String fileName, Context context);
}
