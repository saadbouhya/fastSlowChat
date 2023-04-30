package com.example.slowvf.Controller;

import com.example.slowvf.Dao.IdentificationDao;
import com.example.slowvf.Dao.Impl.IdentificationDaoImpl;

public class IdentificationController {
        private IdentificationDao identificationDao;

    public IdentificationController() {
        identificationDao = new IdentificationDaoImpl();
    }

    public void createUser(String pseudo){
            identificationDao.createUser(pseudo);
        }
}
