package com.example.sif.Lei.GPSServer;

import android.content.Context;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.example.sif.MyApplication;

public class MyGpsClient {

    public LocationClient locationClient = null;
    public MyLocationListener locationListener = new MyLocationListener();

    public MyGpsClient(Context context){
        SDKInitializer.initialize(MyApplication.getContext());
        locationClient = new LocationClient(MyApplication.getContext());
        locationClient.registerLocationListener(locationListener);
        LocationClientOption locationClientOption = new LocationClientOption();
        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationClientOption.setCoorType("GCJ02");
        locationClientOption.setIsNeedAddress(true);
        locationClientOption.setNeedNewVersionRgc(true);
        locationClientOption.setIsNeedLocationPoiList(true);
        locationClientOption.setOpenGps(true);
        locationClient.setLocOption(locationClientOption);
        locationClient.start();
    }

    public MyLocationListener obtainListener(){
        return locationListener;
    }

}
