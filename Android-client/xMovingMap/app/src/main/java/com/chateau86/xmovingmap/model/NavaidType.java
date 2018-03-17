package com.chateau86.xmovingmap.model;

import android.support.annotation.Nullable;
import android.util.SparseArray;

/**
 * Created by chateau86 on 16-Mar-18.
 */

public enum NavaidType {
    NDB(2),
    VOR(3),
    ILS_LOC(4),
    LOC(5),
    ILS_GS(6),
    ILS_OM(7),
    ILS_MM(8),
    ILS_IM(9),
    DME_PART(12),
    DME(13);

    public final int xpID;
    NavaidType(int id) {
        this.xpID = id;
    }

    @Nullable
    public static NavaidType fromXPID(int id){
        for(NavaidType t: values()){
            if(t.xpID == id){
                return t;
            }
        }
        return null;
    }
}
