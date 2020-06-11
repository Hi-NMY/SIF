package com.example.sif.NeiBuLei;

import org.litepal.crud.LitePalSupport;

public class UserSignClass extends LitePalSupport{
    private int id;
    private int longday;
    private int signday;
    private int coin;
    private String xuehao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLongday() {
        return longday;
    }

    public void setLongday(int longday) {
        this.longday = longday;
    }

    public int getSignday() {
        return signday;
    }

    public void setSignday(int signday) {
        this.signday = signday;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getXuehao() {
        return xuehao;
    }

    public void setXuehao(String xuehao) {
        this.xuehao = xuehao;
    }
}
