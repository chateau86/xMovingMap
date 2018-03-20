package com.chateau86.xmovingmap.model;

/**
 * Created by chateau86 on 16-Mar-18.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Navaid {
    @PrimaryKey
    @NonNull
    public String uniqueIdent;

    public int xpID;
    public double lat; //deg
    public double lon; //deg
    public double sin_lat;
    public double cos_lat;
    public double sin_lon;
    public double cos_lon;
    //public int lat_cell;
    //public int lon_cell;
    public int elev; //ft
    public int freq; //(x0.1MHz)
    public int maxRange; //nmi
    public String ident;
    public String termRegion;
    public String ICAORegion;
    public String name;

    Navaid(int xpID, double lat, double lon, int elev, int freq, int maxRange, String ident, String termRegion, String ICAORegion, String name){
        this.xpID = xpID;
        this.lat = lat;
        //this.lat_cell = (int) lat;
        this.lon = lon;
        //this.lon_cell = (int) lon;
        this.elev = elev;
        this.freq = freq;
        this.maxRange = maxRange;
        this.ident = ident;
        this.termRegion = termRegion;
        this.ICAORegion = ICAORegion;
        this.name = name;
        this.uniqueIdent = xpID + ICAORegion + termRegion + ident;

        //Because SQLite can't do trig: Need to keep sin(), cos() of lat/lon
        // https://github.com/sozialhelden/wheelmap-android/wiki/Sqlite,-Distance-calculations

        this.sin_lat = Math.sin(Math.toRadians(lat));
        this.cos_lat = Math.cos(Math.toRadians(lat));
        this.sin_lon = Math.sin(Math.toRadians(lon));
        this.cos_lon = Math.cos(Math.toRadians(lon));
    }

    public NavaidType getType(){
        return NavaidType.fromXPID(this.xpID);
    }

    public String toString(){
        if(freq < 10000) {
            return ident + " (" + freq + "): " + name;
        } else {
            return ident + " (" + String.format("%.02f", ((double)freq)/100) + "): " + name;
        }
    }

}
