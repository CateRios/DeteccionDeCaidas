package com.example.detecciondecaidas.modelo;

import android.content.Context;

public interface ModelInterface {

    public void initSensorDataRecollection(Context context);

    public void endSensorDataRecollection();

    void getMovementData(String id, String movimiento);

    void insertMovToDatabase();

    void generateFile(Context context);

    String getLocation();
}
