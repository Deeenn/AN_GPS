package com.example.gps;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.example.gps.Data.Location_data;

import java.text.SimpleDateFormat;

public class LocationManager implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {
        Location_data data = Location_data.getInstance();

        data._latitude = location.getLatitude();
        data._longitude = location.getLongitude();
        data._accuracy = location.getAccuracy();
        data._altitude = location.getAltitude();
        data._speed = location.getSpeed();

        // 表示形式を設定
        data.sdf = new SimpleDateFormat("yyyy'年'MM'月'dd'日'　kk'時'mm'分'ss'秒'");

        Log.d("debug","onLocationChanged");

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

}
