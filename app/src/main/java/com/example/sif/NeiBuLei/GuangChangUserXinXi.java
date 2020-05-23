package com.example.sif.NeiBuLei;

import org.litepal.crud.LitePalSupport;

public class GuangChangUserXinXi extends LitePalSupport{
    private int id;
    private String gc_user_name;
    private String gc_user_shijian;
    private String gc_user_xinxi;
    private String gc_user_xuehao;
    private String gc_user_image_url;
    private String gc_user_dynamic_id;
    private int gc_user_dynamic_thumb;
    private int gc_user_dynamic_comment;
    private String block;
    private String gc_user_ip;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGc_user_name() {
        return gc_user_name;
    }

    public void setGc_user_name(String gc_user_name) {
        this.gc_user_name = gc_user_name;
    }

    public String getGc_user_shijian() {
        return gc_user_shijian;
    }

    public void setGc_user_shijian(String gc_user_shijian) {
        this.gc_user_shijian = gc_user_shijian;
    }

    public String getGc_user_xinxi() {
        return gc_user_xinxi;
    }

    public void setGc_user_xinxi(String gc_user_xinxi) {
        this.gc_user_xinxi = gc_user_xinxi;
    }

    public String getGc_user_xuehao() {
        return gc_user_xuehao;
    }

    public void setGc_user_xuehao(String gc_user_xuehao) {
        this.gc_user_xuehao = gc_user_xuehao;
    }

    public String getGc_user_image_url() {
        return gc_user_image_url;
    }

    public void setGc_user_image_url(String gc_user_image_url) {
        this.gc_user_image_url = gc_user_image_url;
    }

    public String getGc_user_dynamic_id() {
        return gc_user_dynamic_id;
    }

    public void setGc_user_dynamic_id(String gc_user_dynamic_id) {
        this.gc_user_dynamic_id = gc_user_dynamic_id;
    }

    public int getGc_user_dynamic_thumb() {
        return gc_user_dynamic_thumb;
    }

    public void setGc_user_dynamic_thumb(int gc_user_dynamic_thumb) {
        this.gc_user_dynamic_thumb = gc_user_dynamic_thumb;
    }

    public String getGc_user_ip() {
        return gc_user_ip;
    }

    public void setGc_user_ip(String gc_user_ip) {
        this.gc_user_ip = gc_user_ip;
    }

    public int getGc_user_dynamic_comment() {
        return gc_user_dynamic_comment;
    }

    public void setGc_user_dynamic_comment(int gc_user_dynamic_comment) {
        this.gc_user_dynamic_comment = gc_user_dynamic_comment;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }
}
