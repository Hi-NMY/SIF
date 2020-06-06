package com.example.sif.NeiBuLei;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class UserSpace extends LitePalSupport implements Serializable {
    private int id;
    private String user_dynamic_id;
    private String user_place;
    private String user_shijian;
    private String user_xinxi;
    private String user_image_url;
    private int user_thumb;
    private int user_comment;
    private String user_xuehao;
    private String user_name;
    private String block;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_place() {
        return user_place;
    }

    public void setUser_place(String user_place) {
        this.user_place = user_place;
    }

    public String getUser_shijian() {
        return user_shijian;
    }

    public void setUser_shijian(String user_shijian) {
        this.user_shijian = user_shijian;
    }

    public String getUser_xinxi() {
        return user_xinxi;
    }

    public void setUser_xinxi(String user_xinxi) {
        this.user_xinxi = user_xinxi;
    }

    public String getUser_dynamic_id() {
        return user_dynamic_id;
    }

    public void setUser_dynamic_id(String user_dynamic_id) {
        this.user_dynamic_id = user_dynamic_id;
    }

    public String getUser_image_url() {
        return user_image_url;
    }

    public void setUser_image_url(String user_image_url) {
        this.user_image_url = user_image_url;
    }

    public int getUser_thumb() {
        return user_thumb;
    }

    public void setUser_thumb(int user_thumb) {
        this.user_thumb = user_thumb;
    }

    public int getUser_comment() {
        return user_comment;
    }

    public void setUser_comment(int user_comment) {
        this.user_comment = user_comment;
    }

    public String getUser_xuehao() {
        return user_xuehao;
    }

    public void setUser_xuehao(String user_xuehao) {
        this.user_xuehao = user_xuehao;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }
}
