package com.example.detecciondecaidas.modelo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface CapturaDao {

    @Query("SELECT * from capturas")
    List<Captura> getAll();

    @Query("SELECT * from capturas WHERE idMov IN (:idMovimientos)")
    List<Captura> loadAllByMovimientoIds(int[] idMovimientos);

    @Query("DELETE from capturas")
    public int clearTable();

    @Insert
    long insert(Captura captura);

    @Delete
    void delete(Captura captura);
}
