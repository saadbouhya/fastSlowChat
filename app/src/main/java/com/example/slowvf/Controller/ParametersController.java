package com.example.slowvf.Controller;

import android.content.Context;

import com.example.slowvf.Dao.Impl.ParametersDaoImpl;
import com.example.slowvf.Dao.ParametersDao;

public class ParametersController {

    private ParametersDao parametersDao;

    public ParametersController(Context context) {
        this.parametersDao = new ParametersDaoImpl(context);
    }

    public String getUserId() {
        return parametersDao.getUserId();

    }
}
