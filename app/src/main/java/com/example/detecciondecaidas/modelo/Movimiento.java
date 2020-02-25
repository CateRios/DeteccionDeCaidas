package com.example.detecciondecaidas.modelo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "movimientos")
public class Movimiento {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "id_user")
    public String idUser;

    @ColumnInfo(name = "tipo_movimiento")
    public String tipoMovimiento;

    @ColumnInfo(name = "fecha")
    public Date fecha;
}
