package com.chateau86.xmovingmap.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;

/**
 * Created by chateau86 on 16-Mar-18.
 */
@Database(entities = {Navaid.class}, version = 1)
public abstract class NavaidDatabase extends RoomDatabase {
    public abstract NavaidDAO navaidDAO();

    public void initDatabase(BufferedReader in_txt) throws IOException{
        //First, empty the DB
        navaidDAO().clearDatabase();

        //get rid of header
        in_txt.readLine();
        in_txt.readLine();
        in_txt.readLine();
        int lineCount = 0;
        while(true){
            String line = in_txt.readLine();
            lineCount++;
            //Log.e("NavaidDatabase", "got line: "+line);
            if(line.length() == 0){
                break;
            }
            if("99".equals(line)){
                Log.e("NavaidDatabase", "End of earth_nav.dat");
                break;
            }
            int xpID = Integer.parseInt(line.substring(0,2).trim());
            //Log.e("NavaidDatabase", "\txpID: "+line.substring(0,2));
            if(xpID >= 14){ //Unsupported; Not shown on charts anyway.3
                continue;
            }
            double lat = Double.parseDouble(line.substring(2,16).trim());
            //Log.e("NavaidDatabase", "\tlat: "+line.substring(2,16));
            double lon = Double.parseDouble(line.substring(16,31).trim());
            //Log.e("NavaidDatabase", "\tlon: "+line.substring(16,31));
            int elev = Integer.parseInt(line.substring(31,40).trim());
            int freq = Integer.parseInt(line.substring(40,49).trim()); //(x0.1MHz)
            int maxRange = Integer.parseInt(line.substring(49,55).trim());; //nmi
            String ident = line.substring(66,71).trim();
            String termRegion = line.substring(71,76).trim();
            String ICAORegion = line.substring(76,79).trim();
            String name = line.substring(79).trim();
            Navaid n = new Navaid(xpID, lat, lon, elev, freq, maxRange, ident, termRegion, ICAORegion, name);
            //Log.e("NavaidDatabase", "\tNavaid: "+n);
            navaidDAO().insertAll(n);
            if (lineCount % 500 == 0){
                Log.e("NavaidDatabase", "At line: "+lineCount);
            }
        }
    }
}
