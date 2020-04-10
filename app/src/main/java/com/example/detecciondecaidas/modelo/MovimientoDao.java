package com.example.detecciondecaidas.modelo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovimientoDao {

    @Query("SELECT * from movimientos")
    List<Movimiento> getAll();

    @Query("SELECT * from movimientos WHERE id_user IN (:idUsers)")
    List<Movimiento> loadAllByUserIds(int[] idUsers);

    @Query("DELETE from movimientos")
    public int clearTable();

    @Insert
    long insert(Movimiento movimiento);

    @Delete
    void delete(Movimiento movimiento);
}
