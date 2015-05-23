package com.workinggroup.chenfamily.gpsracecar.gps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.workinggroup.chenfamily.gpsracecar.model.LocationModel;
import com.workinggroup.chenfamily.gpsracecar.util.FormatUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by 80240062 on 2015/5/19.
 */
public class GpsApi {
    /**
     * min change distance 10m
     */
    private static final  float defaultMinDistance =10;
    private static final String TAG = "GpsApi";

    /**
     * 200ms
     */
    private static final long defaultMinTime = 200;

    /**
     * record min distance
     */
    private float minDistance;

    /**
     * record min time
     */
    private long minTime;


    private float minAccuracyMeters;

    private int gpsStatus;
    /**
     *
     */
    private LocationManager locationManager;


    /**
     *
     */
    private Location location = null;



    //*********************************************************/
    /**
     *
     */
    static public interface UIHandler{
        public LocationManager getLocationManager();
        public void openGpsSetting();
        public void updateNumSatellite(int numOfSatellite);

        public void onLocationChanged(Location location);

        public void recordLocationInfo(LocationModel model);

        public void showRecordInMap(LocationModel model);

        public void showMessage(String message);
    }

    UIHandler sExternalHandler;

    public void setUIHandler(UIHandler anHandler){
        sExternalHandler = anHandler;
    }

    private LocationManager getLocationManagerLocal(){
        return  sExternalHandler.getLocationManager();
    }

    private void openGpsSettingLocal(){
         sExternalHandler.openGpsSetting();
    }

    private void  updateNumSatelliteLocal(int numOfSatellite){
        sExternalHandler.updateNumSatellite(numOfSatellite);
    }
    private void onLocationChangedLocal(Location location){
         sExternalHandler.onLocationChanged(location);
    }

    private void recordLocationInfoLocal(LocationModel model){
        sExternalHandler.recordLocationInfo(model);
    }

    private void showRecordInMapLocal(LocationModel model){
        sExternalHandler.showRecordInMap(model);
    }

    private void showMessageLocal(String msg){
        sExternalHandler.showMessage(msg);
    }

    //*********************************************************/

    /**
     * GpsApi
     * @param minDistance
     * @param minTime
     * @param minAccuracyMeters
     */
    public void setDistanceAndTime(float minDistance,long minTime,float minAccuracyMeters){
        if(minDistance==0){
            this.minDistance = defaultMinDistance;
        }else{
            this.minDistance = minDistance;
        }

        if(minTime==0){
            this.minTime = defaultMinTime;
        }else{
            this.minTime = minTime;
        }
        this.minAccuracyMeters =minAccuracyMeters;
    }


    public  LocationManager getLocationManagerApi(){
        if(locationManager==null){
            locationManager = getLocationManagerLocal();
        }
        return locationManager;
    }
    /**
     * is Gps open
     */
    public boolean isGpsProviderOpen(){

       locationManager = getLocationManagerApi();

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     *
     */
    public void initGps(){

          //转到手机设置界面，用户设置GPS
        if(!isGpsProviderOpen()){
            openGpsSettingLocal();
        }
    }


    /**
     *
     * @param distance
     */
    public void setMinDistance(float distance){
        minDistance = distance;
    }

    // 获取Location Provider
    public String getProvider() {
        // 构建位置查询条件
        Criteria criteria = new Criteria();
        // 查询精度：高
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 是否查询海拨：否
        criteria.setAltitudeRequired(false);
        // 是否查询方位角 : 否
        criteria.setBearingRequired(false);
        // 是否允许付费：是
        criteria.setCostAllowed(true);
        // 电量要求：低
        //criteria.setPowerRequirement(Criteria.POWER_LOW);
        // 返回最合适的符合条件的provider，第2个参数为true说明 , 如果只有一个provider是有效的,则返回当前provider
        return locationManager.getBestProvider(criteria, true);
    }

   public void openLocation(){
       if (locationManager==null){
           showMessageLocal("打开定位失败");
           return;
       }
       showMessageLocal("打开定位成功");
       location = locationManager.getLastKnownLocation(getProvider());
       locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, locationListener);
       locationManager.addGpsStatusListener(statusListener);
   }


    public void closeLocation(){
        if(locationManager!=null){
            showMessageLocal("关闭定位记录成功");
            locationManager.removeUpdates(locationListener);
            locationManager.removeGpsStatusListener(statusListener);
                //locationListener=null;

            locationManager=null;
        }
    }


    public Location getLocation(){
        return location;
    }

    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0];
    }

    //*****************************location Listener****************************/
    private final LocationListener locationListener = new LocationListener() {
        /**
         * Called when the location has changed.
         * <p/>
         * <p> There are no restrictions on the use of the supplied Location object.
         *
         * @param location The new location, as a Location object.
         */
        @Override
        public void onLocationChanged(Location location) {

            onLocationChangedLocal(location);
            if(location !=null){
                location = location;
                Log.i(TAG, "时间：" + location.getTime());
                Log.i(TAG, "经度："+location.getLongitude());
                Log.i(TAG, "纬度："+location.getLatitude());
                Log.i(TAG, "海拔："+location.getAltitude());





//                if(location.hasAccuracy() && location.getAccuracy()<=minAccuracyMeters){


//                    if(LocationProvider.AVAILABLE == gpsStatus){
                        LocationModel model;
                        model = new LocationModel();
                        model.setLatitude(location.getLatitude());
                        model.setLongitude(location.getLongitude());
                        model.setRecordTime(location.getTime());
                        model.setNumSatelliteSize(numSatelliteSize);
                        model.setLogTime(FormatUtil.convertLongToCalendar(location.getTime()));
                        showRecordInMapLocal(model);

                        if( numSatelliteSize>=6){
                            recordLocationInfoLocal(model);
                            showMessageLocal(model.toStr());
                        }

//                    }

//                GregorianCalendar greg = new GregorianCalendar();
//                TimeZone tz = greg.getTimeZone();
//                int ffset = tz.getOffset(System.currentTimeMillis());
//                greg.add(Calendar.SECOND, (offset / 1000) * -1);
//                }
            }
        }

        /**
         * Called when the provider status changes. This method is called when
         * a provider is unable to fetch a location or if the provider has recently
         * become available after a period of unavailability.
         *
         * @param provider the name of the location provider associated with this
         *                 update.
         * @param status   {@link android.location.LocationProvider#OUT_OF_SERVICE} if the
         *                 provider is out of service, and this is not expected to change in the
         *                 near future; {@link android.location.LocationProvider#TEMPORARILY_UNAVAILABLE} if
         *                 the provider is temporarily unavailable but is expected to be available
         *                 shortly; and {@link android.location.LocationProvider#AVAILABLE} if the
         *                 provider is currently available.
         * @param extras   an optional Bundle which will contain provider specific
         *                 status variables.
         *                 <p/>
         *                 <p> A number of common key/value pairs for the extras Bundle are listed
         *                 below. Providers that use any of the keys on this list must
         *                 provide the corresponding value as described below.
         *                 <p/>
         *                 <ul>
         *                 <li> satellites - the number of satellites used to derive the fix
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            gpsStatus =status;
            switch (status) {
                //GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    Log.i(TAG, "当前GPS状态为可见状态");
                    showMessageLocal("当前GPS状态为可见状态");
                    break;
                //GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    Log.i(TAG, "当前GPS状态为服务区外状态");
                    showMessageLocal("当前GPS状态为服务区外状态");
                    break;
                //GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.i(TAG, "当前GPS状态为暂停服务状态");
                    showMessageLocal("当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * Called when the provider is enabled by the user.
         *
         * @param provider the name of the location provider associated with this
         *                 update.
         */
        @Override
        public void onProviderEnabled(String provider) {

        }

        /**
         * Called when the provider is disabled by the user. If requestLocationUpdates
         * is called on an already disabled provider, this method is called
         * immediately.
         *
         * @param provider the name of the location provider associated with this
         *                 update.
         */
        @Override
        public void onProviderDisabled(String provider) {
            location = null;
        }
    };



    //*****************************GpsStatus Listener****************************/

    /**
    .* 卫星状态监听器
     */
      private List<GpsSatellite> numSatelliteList = new ArrayList<GpsSatellite>(); // 卫星信号

      private int numSatelliteSize = 0;
    /**
     * Called to report changes in the GPS status.
     * The event number is one of:
     * <ul>
     * <li> {@link android.location.GpsStatus#GPS_EVENT_STARTED}
     * <li> {@link android.location.GpsStatus#GPS_EVENT_STOPPED}
     * <li> {@link android.location.GpsStatus#GPS_EVENT_FIRST_FIX}
     * <li> {@link android.location.GpsStatus#GPS_EVENT_SATELLITE_STATUS}
     * </ul>
     * <p/>
     * When this method is called, the client should call
     * {@link LocationManager#getGpsStatus} to get additional
     * status information.
     *
     * @param event event number for this notification
     */
      private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
               public void onGpsStatusChanged(int event) { // GPS状态变化时的回调，如卫星数


                   switch (event) {
                       //第一次定位
                       case GpsStatus.GPS_EVENT_FIRST_FIX:

                           Log.i(TAG, "第一次定位");
                           showMessageLocal("第一次定位");
                           break;
                       //卫星状态改变
                       case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                           Log.i(TAG, "卫星状态改变");
                           //showMessageLocal("卫星状态改变");
                           //获取当前状态
                           LocationManager locationManager =  getLocationManagerLocal();
                           GpsStatus status = locationManager.getGpsStatus(null); //取当前状态
                           String satelliteInfo = updateGpsStatus(event, status);
                           Log.i(TAG, satelliteInfo);;
                           break;
                       //定位启动
                       case GpsStatus.GPS_EVENT_STARTED:
                           Log.i(TAG, "定位启动");
                           showMessageLocal("定位启动");
                           break;
                       //定位结束
                       case GpsStatus.GPS_EVENT_STOPPED:
                           Log.i(TAG, "定位结束");
                           showMessageLocal("定位结束");
                           break;
                   }
               }
      };

    /**
     *
     * @param event
     * @param status
     * @return
     */
      private String updateGpsStatus(int event, GpsStatus status) {
               StringBuilder sb2 = new StringBuilder("");
               if (status == null) {
                    sb2.append("搜索到卫星个数：" +0);
                   showMessageLocal("搜索到卫星个数0");
                   numSatelliteSize =0;
                    updateNumSatelliteLocal(0);
               } else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
                    int maxSatellites = status.getMaxSatellites();
                      Iterator<GpsSatellite> it = status.getSatellites().iterator();
                       numSatelliteList.clear();
                    int count = 0;
                    while (it.hasNext() && count <= maxSatellites) {
                            GpsSatellite s = it.next();
                            numSatelliteList.add(s);
                              count++;
                     }
                   if( numSatelliteSize !=numSatelliteList.size()){
                       showMessageLocal("搜索到卫星个数:"+ numSatelliteSize);
                   }
                   numSatelliteSize =numSatelliteList.size();
                    sb2.append("搜索到卫星个数：" + numSatelliteSize);

                    updateNumSatelliteLocal(numSatelliteSize);
              }

            return sb2.toString();
      }



}
