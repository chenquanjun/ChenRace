package com.workinggroup.chenfamily.gpsracecar.model;

import com.workinggroup.chenfamily.gpsracecar.util.Vec2;

/**
 * Created by cheneven on 5/18/15.
 */
public class LocationModel {

    private double latitude;

    private double longitude;

    private long recordTime;

    private String logTime;

    private double speed;

    private int numSatelliteSize;

    public Vec2 getVec2(){
        return new Vec2((float)latitude,(float)longitude);
    }

    public int getNumSatelliteSize() {
        return numSatelliteSize;
    }

    public void setNumSatelliteSize(int numSatelliteSize) {
        this.numSatelliteSize = numSatelliteSize;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String toStr(){
        return new StringBuffer().append("经度=").append(latitude).
                append(",纬度=").append(longitude).
                append(",卫星个数=").append(numSatelliteSize).append(",记录时间=").append(logTime).toString();
    }
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }


}
