package com.workinggroup.chenfamily.gpsracecar;

import android.content.ComponentName;
import android.content.Context;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.ServiceConnection;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

import android.widget.Toast;


import com.workinggroup.chenfamily.gpsracecar.gps.GpsApi;
import com.workinggroup.chenfamily.gpsracecar.model.LocationModel;
import com.workinggroup.chenfamily.gpsracecar.service.GPSService;
import com.workinggroup.chenfamily.gpsracecar.util.FormatUtil;
import com.workinggroup.chenfamily.gpsracecar.util.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.InjectView;
import butterknife.OnClick;

import butterknife.ButterKnife;

public class RaceCarActivity extends ActionBarActivity {

    String TAG = "RaceCarActivity";
    MapUtil mapUtil = null;
    ArrayList planeList = new ArrayList();
    /**
     * race road temp record information
     */

    ArrayList raceRoadList = new ArrayList();
    /**
     *
     */
    @InjectView(R.id.StartRaceRoadButton)
    Button startRaceRoadButton;

    @InjectView(R.id.StopRaceRoadButton)
    Button stopRaceRoadButton;

    @InjectView(R.id.StartRaceButton)
    Button startRaceButton;

    @InjectView(R.id.StartRaceButton)
    Button stopRaceButton;

    @OnClick(R.id.StartRaceRoadButton)
    void OnStartRaceRoadClick() {
        Toast.makeText(RaceCarActivity.this,"Start Race Road button",Toast.LENGTH_LONG).show();
        Log.i(TAG,"StartRaceRoadService");
        if(raceRoadList!=null && raceRoadList.size()!=0){
            raceRoadList.clear();
        }else if(raceRoadList == null){
            raceRoadList =  new ArrayList();
        }

        binderGPSService();
        startRaceRoadButton.setEnabled(false);
        stopRaceRoadButton.setEnabled(true);
    }





    @OnClick(R.id.StopRaceRoadButton)
    void OnStopRaceRoadClick() {
        Toast.makeText(RaceCarActivity.this,"Stop Race Road button",Toast.LENGTH_LONG).show();
        Log.i(TAG,"StartRaceRoadService");
        stopGPSService();
        startRaceRoadButton.setEnabled(true);
        stopRaceRoadButton.setEnabled(false);
        //record information to database
        LocationModel locationModel;

        int count = 0 ;
        if(raceRoadList!=null){
            for(Object o:raceRoadList){
                locationModel = (LocationModel) o;

            }
        }

    }


    @OnClick(R.id.ShowRaceRoadButton)
    void OnShowRaceRoadClick(){

        Intent mIntent = new Intent(this,ShowRoadActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(FormatUtil.RACE_ROAD_LIST_KEY,raceRoadList);
        mIntent.putExtras(mBundle);


        startActivity(mIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_car);
        //
        ButterKnife.inject(this);
        stopRaceRoadButton.setEnabled(false);
        Log.i(TAG,"OnCreate.............................");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_race_car, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    ServiceConnection conn = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            //调用bindService方法启动服务时候，如果服务需要与activity交互，
            //则通过onBind方法返回IBinder并返回当前本地服务
            localService=((GPSService.GPSServiceBinder)binder).getService();
            //这里可以提示用户,或者调用服务的某些方法
            Toast.makeText(RaceCarActivity.this,"OnServiceConnected",Toast.LENGTH_LONG).show();
            Log.i(TAG,"OnServiceConnected");
            if(localService!=null) {

                localService.gpsApi.setUIHandler(new GpsApi.UIHandler(){
                                        /**
                                         *
                                         */
                                        private LocationManager locationManager;

                                        @Override
                                        public LocationManager getLocationManager() {
                                            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                            return locationManager;
                                        }

                                        @Override
                                        public void openGpsSetting() {
                                            Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            //startActivity(callGPSSettingIntent);
                                            startActivityForResult(callGPSSettingIntent, 0);
                                        }

                                        @Override
                                        public void updateNumSatellite(int numOfSatellite) {
                                            //Toast.makeText(RaceCarActivity.this, "updateNumSatellite="+numOfSatellite, Toast.LENGTH_LONG).show();

                                        }

                                        @Override
                                        public void onLocationChanged(Location location) {

                                        }

                                        @Override
                                        public void recordLocationInfo(LocationModel model) {
                                            String temp = model==null?"":model.toStr();
                                            Toast.makeText(RaceCarActivity.this, temp, Toast.LENGTH_LONG).show();
                                            //FormatUtil.convertCurrentChinaDateToStr();
                                            if(startRaceRoadButton.isEnabled()){
                                                raceRoadList.add( model);
                                            }
                                        }

                                        @Override
                                        public void showRecordInMap(LocationModel model) {
                                            String temp = model==null?"":model.toStr();
                                            Toast.makeText(RaceCarActivity.this, temp, Toast.LENGTH_LONG).show();
                                        }

                                         @Override
                                         public void showMessage(String message) {
                                             Toast.makeText(RaceCarActivity.this, message==null?"":message, Toast.LENGTH_LONG).show();
                                         }
                             }
                );
                localService.startGPSService(0, 0, 0);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if(localService!=null) {
                localService.endGPSService();
            }
            localService=null;
            //这里可以提示用户
            Toast.makeText(RaceCarActivity.this,"OnServiceDisconnected",Toast.LENGTH_LONG).show();
            Log.i(TAG,"OnServiceDisconnected");
        }
    };

    GPSService localService=null;
    //用bindService方法启动服务
    private void binderGPSService(){
        Intent intent=new Intent(this,GPSService.class);

        bindService(intent, conn, Context.BIND_AUTO_CREATE);

    }

    private void startGPSService(){
        Intent intent=new Intent(this,GPSService.class);

        startService(intent);
    }

    private void stopGPSService(){
        unbindService(conn);
    }



}
