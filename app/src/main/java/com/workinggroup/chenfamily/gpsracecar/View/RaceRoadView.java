package com.workinggroup.chenfamily.gpsracecar.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.workinggroup.chenfamily.gpsracecar.model.LocationModel;
import com.workinggroup.chenfamily.gpsracecar.util.Vec2;

import java.util.ArrayList;
import java.util.Objects;


/**
 * Created by cheneven on 5/23/15.
 */
public class RaceRoadView extends View{
    String TAG = "RaceRoadView";
    ArrayList raceRoadList = null;
    LocationModel locationModel=null;
    int m_screenHeight = 0;
    int m_screenWidth = 0;
    public RaceRoadView(Context context) {
        super(context);

        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        m_screenHeight = dm.heightPixels;
        m_screenWidth = dm.widthPixels;
    }

    public void setRaceRoadList(ArrayList list){
        raceRoadList = list;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();

        //test data
        int testPointNum = 50;
        float testWidth = 300;
        double step = 2 * Math.PI / testPointNum;
        double baseX = m_screenWidth * 0.5;
        double baseY = m_screenHeight * 0.5;
        float offset = 20;
        for (int i = 0;i < testPointNum;i++){
            double randomNumX = Math.random();
            double randomNumY = Math.random();
            LocationModel model = new LocationModel();
            double x = i * step;
            double latitude = Math.sin(x) * testWidth + baseX + randomNumX * offset - 0.5 * offset;
            double longitude = Math.cos(x) * testWidth + baseY + randomNumY * offset - 0.5 * offset;
            model.setLatitude(latitude);
            model.setLongitude(longitude);
            //test data
            raceRoadList.add(model);
        }

        if(raceRoadList!=null){
            p.setColor(Color.BLUE);// 设置蓝色

            float lastLatitude = 0;
            float lastLongitude = 0;

            ArrayList vecList = new ArrayList();

            for (int i = 0;i<raceRoadList.size();i++){
                LocationModel tmpModel = (LocationModel) raceRoadList.get(i);
                float latitude = (float)tmpModel.getLatitude();
                float longitude = (float)tmpModel.getLongitude();

                vecList.add(tmpModel.getVec2());

                if (i!=0){
                    canvas.drawLine(lastLatitude, lastLongitude, latitude, longitude, p);// 画线
                }
                lastLatitude = latitude;
                lastLongitude = longitude;
            }

            LocationModel firstModel = (LocationModel) raceRoadList.get(1);
            float firstLatitude = (float)firstModel.getLatitude();
            float firstLongitude = (float)firstModel.getLongitude();

            canvas.drawLine(lastLatitude, lastLongitude, firstLatitude, firstLongitude, p);// 画线

            vecList.add(firstModel.getVec2());


            float lastX = 0;
            float lastY = 0;

            int samples = 5;
            ArrayList resultList =  Vec2.cardinalSpline(vecList, samples);

            p.setColor(Color.RED);// 设置红色

            for (int i = 0;i<resultList.size();i++){
                Vec2 tmpPoint = (Vec2) resultList.get(i);
                float x = tmpPoint.x;
                float y = tmpPoint.y;

                if (i!=0){
                    canvas.drawLine(lastX, lastY - 100, x, y - 100, p);// 画线
                }
                lastX = x;
                lastY = y;
            }

        }



//        p.setColor(Color.RED);// 设置红色
//
//        canvas.drawText("画圆：", 10, 20, p);// 画文本
//
//        canvas.drawCircle(10, 20, 10, p);
//
//        canvas.drawCircle(60, 20, 10, p);// 小圆


        //*************************/



//        p.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
//        canvas.drawCircle(120, 20, 20, p);// 大圆
//
//        canvas.drawText("画线及弧线：", 10, 60, p);
//        p.setColor(Color.GREEN);// 设置绿色
//        canvas.drawLine(60, 40, 100, 40, p);// 画线
//        canvas.drawLine(110, 40, 190, 80, p);// 斜线


//        canvas.drawText("画矩形：", 10, 80, p);
//        p.setColor(Color.GRAY);// 设置灰色
//        p.setStyle(Paint.Style.FILL);//设置填满
//        canvas.drawRect(60, 60, 80, 80, p);// 正方形
//        canvas.drawRect(60, 90, 160, 100, p);// 长方形
    }
}
