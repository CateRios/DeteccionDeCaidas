package com.example.detecciondecaidas.presentador;

import android.content.Context;

public interface MainPresenterInterface {

    public void initSensorDataRecollection(Context context);

    public void endSensorDataRecollection();

    void passDataToModel(String id, String movimiento, int indice, int periodo);

    String generateFile(Context context);

    void deleteDBData();
}
