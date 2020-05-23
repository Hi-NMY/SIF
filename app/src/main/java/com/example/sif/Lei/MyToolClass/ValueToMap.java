package com.example.sif.Lei.MyToolClass;

import java.util.Map;

public class ValueToMap {
    public static Object getKey(Map map,Object o){
        for (Object key:map.keySet()){
            if (map.get(key).equals(o)){
                return key;
            }
        }
        return -1;
    }
}
