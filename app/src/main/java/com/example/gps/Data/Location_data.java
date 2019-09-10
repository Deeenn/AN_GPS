package com.example.gps.Data;

import java.text.SimpleDateFormat;

public class Location_data {

    private Location_data(){}
    /**
     * 唯一のインスタンスを返す
     * @return このクラスの唯一のインスタンス
     */
    public static Location_data getInstance() {
        return DataInstanceHolder.INSTANCE;
    }
    /**
     * Location_dataクラスの唯一のインスタンスを保持する内部クラス
     */
    public static class DataInstanceHolder {
        /** 唯一のインスタンス */
        private static final Location_data INSTANCE = new Location_data();
    }

    // 緯度フィールド
    public double _latitude;
    //経度フィールド
    public double _longitude;
    // 精度
    public  double _accuracy;
    // 高度
    public double _altitude;
    // 速度
    public double _speed;
    // 時刻フォーマット
    public SimpleDateFormat sdf;

}
