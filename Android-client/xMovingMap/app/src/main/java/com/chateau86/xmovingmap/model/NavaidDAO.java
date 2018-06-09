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

    @Query("SELECT * FROM Navaid " +
                "WHERE freq > 0 " +
                "AND (xpID != 4 AND xpID != 6) " +
                "AND (lat BETWEEN :lat-3 AND :lat+3) " +
                "AND (lon BETWEEN :lon-3 AND :lon+3)")
    List<Navaid> getAllNearby(double lat, double lon);

    @Query("SELECT * FROM Navaid " +
            "WHERE freq > 0 " +
            "AND (xpID != 4 AND xpID != 6) " +
            "AND (lat BETWEEN :lat_min AND :lat_max) " +
            "AND (lon BETWEEN :lon_min AND :lon_max)")
    List<Navaid> getWindowNearby(double lat_min, double lat_max, double lon_min, double lon_max);

    @Query("SELECT * FROM Navaid " +
                "WHERE freq > 0 " +
                "AND (xpID != 4 AND xpID != 6) " +
                "AND (lat BETWEEN :lat-3 AND :lat+3) " +
                "AND (lon BETWEEN :lon-3 AND :lon+3)" +
            "ORDER BY (sin_lat*:sin_lat+ cos_lat*:cos_lat*(sin_lon*:sin_lon + cos_lon*:cos_lon)) desc")
    List<Navaid> getAllNearby_sorted(double lat, double lon, double sin_lat, double cos_lat, double sin_lon, double cos_lon);

    @Query("SELECT * FROM Navaid " +
            "WHERE freq > 0 " +
            "AND (xpID != 4 AND xpID != 6) " +
            "ORDER BY (sin_lat*:sin_lat+ cos_lat*:cos_lat*(sin_lon*:sin_lon + cos_lon*:cos_lon)) desc LIMIT 50")
    List<Navaid> getAllNearby_sorted_nowindow(double sin_lat, double cos_lat, double sin_lon, double cos_lon);

    @Insert
    void insertAll(Navaid... navaids);

    @Delete
    void delete(Navaid navaid);

    @Query("SELECT Count(*) FROM Navaid")
    int getRowCount();

    @Query("DELETE FROM Navaid")
    void clearDatabase();
}