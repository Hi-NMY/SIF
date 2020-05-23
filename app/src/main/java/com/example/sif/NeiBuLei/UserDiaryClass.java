package com.example.sif.NeiBuLei;

public class UserDiaryClass{

    private int id;
    private int weathernum;
    private String messagediary;
    private String imagepath;
    private String diarydate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeathernum() {
        return weathernum;
    }

    public void setWeathernum(int weathernum) {
        this.weathernum = weathernum;
    }

    public String getMessagediary() {
        return messagediary;
    }

    public void setMessagediary(String messagediary) {
        this.messagediary = messagediary;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getDiarydate() {
        return diarydate;
    }

    public void setDiarydate(String diarydate) {
        this.diarydate = diarydate;
    }
}
