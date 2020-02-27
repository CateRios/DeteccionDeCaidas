package com.example.detecciondecaidas.presentador;

import android.content.Context;

import com.example.detecciondecaidas.AppMediator;
import com.example.detecciondecaidas.modelo.Model;
import com.example.detecciondecaidas.modelo.ModelInterface;

public class MainPresenter implements MainPresenterInterface {
    private AppMediator appMediator;
    private ModelInterface model;

    public MainPresenter() {
        appMediator = AppMediator.getInstance();
        model = Model.getInstance();
    }

    @Override
    public void passDataToModel(String id, String movimiento){
        model.getMovementData(id, movimiento);
    }

    @Override
    public String generateFile(Context context) {

        model.generateFile(context);
        return model.getLocation();
    }

    @Override
    public void initSensorDataRecollection(Context context){
        model.initSensorDataRecollection(context);
    }

    @Override
    public void endSensorDataRecollection() {
        model.endSensorDataRecollection();
    }

}
