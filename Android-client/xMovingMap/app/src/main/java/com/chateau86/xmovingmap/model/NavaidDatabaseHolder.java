package com.chateau86.xmovingmap.model;

/**
 * Created by chateau86 on 16-Mar-18.
 */

public class NavaidDatabaseHolder {
    private static NavaidDatabase db;

    public static NavaidDatabase getInstance() {
        if(db == null){
            throw new IllegalStateException("Database not initialized");
        }
        return db;
    }

    public static void setInstance(NavaidDatabase new_db){
        db = new_db;
    }

}
