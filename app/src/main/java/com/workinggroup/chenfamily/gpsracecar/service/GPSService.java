package com.workinggroup.chenfamily.gpsracecar.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.workinggroup.chenfamily.gpsracecar.gps.GpsApi;
import com.workinggroup.chenfamily.gpsracecar.model.LocationModel;

import java.util.HashMap;

public class GPSService extends Service {

    public class GPSServiceBinder extends Binder {
       public GPSService getService(){
            return GPSService.this;
        }
    }

    String tag = "RaceCarGPSService";



    public GpsApi gpsApi = new GpsApi();



    public HashMap cacheRaceRoad;



    /**
     *
     */
    private Location location = null;

    /**
     *
     */
    private final IBinder mBinder = new GPSService.GPSServiceBinder();



    public void startGPSService(float minDistance,long minTime,float minAccuracyMeters){
        Log.i(tag,"user start GPSService");
        Toast.makeText(GPSService.this, "用户启动GPS录入数据", Toast.LENGTH_LONG).show();
        gpsApi.initGps();
        gpsApi.openLocation();
    }


    public void endGPSService(){
        Toast.makeText(GPSService.this, "用户关闭GPS例如数据", Toast.LENGTH_LONG).show();
        Log.i(tag,"user end GPSService");
        gpsApi.closeLocation();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }


    public void onCreate() {
        //
       // startService();
        //Toast.makeText(GPSService.this, "GPSService onCreate", Toast.LENGTH_LONG).show();
        Log.v(tag, "GPSService onCreate.");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent ,int startId){
       // Toast.makeText(GPSService.this, "GPSService onstart", Toast.LENGTH_LONG).show();
        Log.i(tag,"GPSService onstart");
        super.onStart(intent,startId);
    }

    @Override
    public int onStartCommand(Intent intent ,int flags ,int startId){
        Toast.makeText(GPSService.this, "GPSService OnStartCommand", Toast.LENGTH_LONG).show();
        Log.i(tag,"GPSService OnStartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
       // endService();
        Toast.makeText(GPSService.this, "GPSService onDestroy", Toast.LENGTH_LONG).show();
        Log.v(tag, "GPSService destroy Ended.");
        super.onDestroy();
    }
}
