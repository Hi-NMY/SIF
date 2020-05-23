package com.example.sif.NeiBuLei;

import org.litepal.crud.LitePalSupport;

public class MyThumb extends LitePalSupport {
    private int id;
    private String dynamic_id;
    private int thumb;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(String dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }
}
