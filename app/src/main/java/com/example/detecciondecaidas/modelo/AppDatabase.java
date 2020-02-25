package com.example.detecciondecaidas.modelo;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Movimiento.class, Captura.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MovimientoDao movimientoDao();
    public abstract CapturaDao capturaDao();
}
