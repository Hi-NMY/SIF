package com.example.sif.NeiBuLei;

import org.litepal.crud.LitePalSupport;

public class NoticeClass extends LitePalSupport {

    private int id;
    private int fun;
    private String sendXueHao;
    private String dynamicId;
    private int see;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFun() {
        return fun;
    }

    public void setFun(int fun) {
        this.fun = fun;
    }

    public String getSendXueHao() {
        return sendXueHao;
    }

    public void setSendXueHao(String sendXueHao) {
        this.sendXueHao = sendXueHao;
    }

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public int getSee() {
        return see;
    }

    public void setSee(int see) {
        this.see = see;
    }
}
