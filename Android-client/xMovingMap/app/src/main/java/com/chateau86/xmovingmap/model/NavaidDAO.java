package com.chateau86.xmovingmap.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by chateau86 on 16-Mar-18.
 */
@Dao
public interface NavaidDAO {
    @Query("SELECT * FROM Navaid")
    List<Navaid> getAll();

    @Query("SELECT * FROM Navaid WHERE (lat_cell BETWEEN :lat_cell-1 AND :lat_cell+1) AND (lon_cell BETWEEN :lon_cell-1 AND :lon_cell+1)")
    List<Navaid> getAllNearby(int lat_cell, double lon_cell);

    @Insert
    void insertAll(Navaid... navaids);

    @Delete
    void delete(Navaid navaid);

    @Query("SELECT Count(*) FROM Navaid")
    int getRowCount();

    @Query("DELETE FROM Navaid")
    void clearDatabase();
}