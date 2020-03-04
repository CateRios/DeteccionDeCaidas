package com.example.detecciondecaidas.modelo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "capturas")
public class Captura {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ForeignKey(entity = Movimiento.class,
                parentColumns = "id",
                childColumns = "id_mov",
                onDelete = CASCADE)
    public long idMov;

    @ColumnInfo (name = "indice_mov")
    public int indiceMov;


    @ColumnInfo(name = "x")
    public float x;

    @ColumnInfo(name = "y")
    public float y;

    @ColumnInfo(name = "z")
    public float z;

    @ColumnInfo(name = "roll")
    public float roll;

    @ColumnInfo(name = "pitch")
    public float pitch;

    @ColumnInfo(name = "yaw")
    public float yaw;

    @ColumnInfo(name = "compass")
    public float compass;

    @ColumnInfo(name = "time")
    public String time;
}
