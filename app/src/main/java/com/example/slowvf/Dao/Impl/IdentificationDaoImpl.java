package com.example.slowvf.Dao.Impl;

import com.example.slowvf.Dao.IdentificationDao;

public class IdentificationDaoImpl implements IdentificationDao {

    public IdentificationDaoImpl() {
    }

    @Override
    public void createUser(String pseudo, String adresseMac) {
        System.out.println(pseudo + " " + adresseMac);
    }
}