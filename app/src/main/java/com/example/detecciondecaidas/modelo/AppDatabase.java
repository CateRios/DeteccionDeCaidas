package com.example.detecciondecaidas.modelo;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Movimiento.class, Captura.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract MovimientoDao movimientoDao();
    public abstract CapturaDao capturaDao();
}
