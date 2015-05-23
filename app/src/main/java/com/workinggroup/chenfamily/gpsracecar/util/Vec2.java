package com.workinggroup.chenfamily.gpsracecar.util;

import java.util.ArrayList;

/**
 * Created by cheneven on 5/23/15.
 */
public class Vec2 {
    public float x, y;
    public Vec2(float x, float y) {this.x = x; this.y = y;}

    //add
    public static Vec2 add(Vec2 s1, Vec2 s2)
    {
        Vec2 s = new Vec2(s1.x + s2.x,s1.y + s2.y);
        return s;
    }

    public static Vec2 add(Vec2 s1, Vec2 ...args)
    {
        Vec2 sum=s1;
        for(int i=0;i<args.length;i++){
            sum = add(sum, args[i]);
        }
        return sum;
    }

    //minus
    public static Vec2 minus(Vec2 s1, Vec2 s2)
    {
        Vec2 s = new Vec2(s1.x - s2.x,s1.y - s2.y);
        return s;
    }

    //multiply
    public static Vec2 multiply(Vec2 s1, float f)
    {
        Vec2 s = new Vec2(s1.x * f,s1.y * f);
        return s;
    }

    //divide
    public static Vec2 divide(Vec2 s1, float f)
    {
        Vec2 s = new Vec2(s1.x / f,s1.y / f);
        return s;
    }

    public static ArrayList interpolateBezier(Vec2 p0,Vec2 p1,Vec2 p2,Vec2 p3, int samples)
    {
        ArrayList pointList = new ArrayList();
        for (int i = 0; i < samples; i++)
        {
            float t = (i + 1) / (samples + 1.0f);
            Vec2 tmpP0 = multiply(p0, (1 - t) * (1 - t) * (1 - t));
            Vec2 tmpP1 = multiply(p1, (3 * (1 - t) * (1 - t) * t));
            Vec2 tmpP2 = multiply(p2, (3 * (1 - t) * t * t));
            Vec2 tmpP3 = multiply(p3, (t * t * t));
            Vec2 resultP = add(tmpP0, tmpP1, tmpP2, tmpP3) ;
            pointList.add(resultP);
        }
        return pointList;
    }

    public static ArrayList interpolateCardinalSpline(Vec2 p0, Vec2 p1, Vec2 p2, Vec2 p3, int samples)
    {
        final float tension = 0.5f;
        Vec2 u = add(multiply(minus(p2, p0), tension / 3), p1);
        Vec2 v = add(multiply(minus(p1, p3), tension / 3), p2);
        return interpolateBezier(p1, u, v, p2, samples);
    }

    public static ArrayList cardinalSpline(ArrayList points, int samplesInSegment)

    {
        ArrayList result = new ArrayList();
        for (int i = 0; i < points.size() - 1; i++)
        {
            result.add(points.get(i));
            Vec2 p0 = (Vec2)points.get(Math.max(i-1, 0));
            Vec2 p1 = (Vec2)points.get(i);
            Vec2 p2 = (Vec2)points.get(i+1);
            Vec2 p3 = (Vec2)points.get(Math.min(i+2, points.size()-1));
            ArrayList rangeList = interpolateCardinalSpline(p0,p1,p2,p3,samplesInSegment);

            for (int j = 0; j < rangeList.size() - 1; j++)
            {
                result.add(rangeList.get(j));
            }
        }

        result.add(points.get(points.size() - 1));
        return result;
    }

}
