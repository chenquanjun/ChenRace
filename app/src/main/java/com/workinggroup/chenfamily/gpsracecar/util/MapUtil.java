package com.workinggroup.chenfamily.gpsracecar.util;

/**
 * Created by cheneven on 5/23/15.
 */
/**
 * 地图工具
 *
 * @author chen
 *
 */
public class MapUtil {

    // 参考点
    private double[] d34 = null;
    private double[] d33 = null;
    private double[] gs34 = null;
    private double[] gs33 = null;

    // 参考点位置
    private double x34, y34;

    // 计算参数
    private double a, b, c, bl;

    /**
     * 构造器
     *
     * @param ds34 参考点经纬度
     * @param ds33 参考点经纬度
     * @param length 两个参考点在本坐标系长度
     */
    public MapUtil(String[] ds34, String[] ds33, double x34, double y34, double length) {

        this.x34 = x34;
        this.y34 = y34;

        d34 = convertGPS(ds34[0], ds34[1]);
        d33 = convertGPS(ds33[0], ds33[1]);
        gs34 = BLToGauss(d34[0], d34[1]);
        gs33 = BLToGauss(d33[0], d33[1]);

        // 点33到点34的高斯横向距离
        a = gs34[0] - gs33[0];

        // 点33到点34的高斯纵向距离
        b = gs34[1] - gs33[1];

        // 点33到点34的高斯直线距离
        c = Math.sqrt(a * a + b * b);

        // 高斯距离与本坐标系的比例
        bl = length / c;
    }

    public MapUtil(double[] ds34, double[] ds33, double x34, double y34, double length) {

        this.x34 = x34;
        this.y34 = y34;

        d34 =  ds34;
        d33 =  ds33;
        gs34 = BLToGauss(d34[0], d34[1]);
        gs33 = BLToGauss(d33[0], d33[1]);

        // 点33到点34的高斯横向距离
        a = gs34[0] - gs33[0];

        // 点33到点34的高斯纵向距离
        b = gs34[1] - gs33[1];

        // 点33到点34的高斯直线距离
        c = Math.sqrt(a * a + b * b);

        // 高斯距离与本坐标系的比例
        bl = length / c;
    }
    /**
     * 根据经纬度算XY
     *
     * @param lng
     * @param lat
     * @return
     */
    public int[] getXY(double lng, double lat) {

        // 与参考点比较经度计算坐标
        if (lng > d34[0]) {
            return getXY1(lng, lat);
        } else {
            return getXY2(lng, lat);
        }
    }

    /**
     * 已有定点为基准算XY坐标
     */
    private int[] getXY1(double lng, double lat) {

        // 将经纬度转换为高斯投影坐标
        double[] gs = BLToGauss(lng, lat);

        // 与点34的横向高斯距离
        double d = Math.abs(gs[0] - gs34[0]);

        double e = d * c / b;

        double f = a * e / c;

        // 与点34的纵向高斯距离
        double gf = Math.abs(gs[1] - gs34[1]);

        double g = gf - f;

        double h = a * g / c;

        double i = b * g / c;

        double x = x34 - i * bl;
        double y = y34 + (h + e) * bl;
        return new int[] { (int) Math.rint(x), (int) Math.rint(y) };
    }

    /**
     * 已有定点为基准算XY坐标
     */
    private int[] getXY2(double lng, double lat) {

        // 将经纬度转换为高斯投影坐标
        double[] gs = BLToGauss(lng, lat);

        // 与点34的横向高斯距离
        double d = Math.abs(gs[0] - gs34[0]);

        double e = d * c / a;

        double f = e * b / c;

        // 与点34的纵向高斯距离
        double gf = Math.abs(gs[1] - gs34[1]);

        double g = gf - f;

        double h = a * g / c;

        double i = b * g / c;

        double x = x34 - (e + i) * bl;
        double y = y34 + h * bl;
        return new int[] { (int) Math.rint(x), (int) Math.rint(y) };
    }

    /**
     * 经纬度度分秒转带小数点的度
     *
     * @param jw
     * @return
     */
    private double[] convertGPS(String lng, String lat) {

        lng = lng.replaceAll("N", "");
        lng = lng.replaceAll("S", "");
        lng = lng.replaceAll("E", "");
        lng = lng.replaceAll("W", "");

        lat = lat.replaceAll("N", "");
        lat = lat.replaceAll("S", "");
        lat = lat.replaceAll("E", "");
        lat = lat.replaceAll("W", "");

        double lngD = 0;
        String[] fs = lng.split("°");
        if (fs.length > 0) {
            lngD = Double.parseDouble(fs[0]);
        }

        fs = fs[1].split("′");
        if (fs.length > 0) {
            double fen = Double.parseDouble(fs[0]);
            fen = fen / 60;
            lngD += fen;
        }

        fs = fs[1].split("″");
        if (fs.length > 0) {
            double mi = Double.parseDouble(fs[0]);
            mi = mi / (60 * 60);
            lngD += mi;
        }

        double latD = 0;
        fs = lat.split("°");
        if (fs.length > 0) {
            latD = Double.parseDouble(fs[0]);
        }

        fs = fs[1].split("′");
        if (fs.length > 0) {
            double fen = Double.parseDouble(fs[0]);
            fen = fen / 60;
            latD += fen;
        }

        fs = fs[1].split("″");
        if (fs.length > 0) {
            double mi = Double.parseDouble(fs[0]);
            mi = mi / (60 * 60);
            latD += mi;
        }

        return new double[] { lngD, latD };
    }

    /**
     * 由高斯投影坐标反算成经纬度
     *
     * @param X
     * @param Y
     * @return
     */
    @SuppressWarnings("unused")
    private double[] GaussToBL(double X, double Y) {

        int ProjNo;
        int ZoneWide; // //带宽
        double[] output = new double[2];
        double longitude1, latitude1, longitude0, X0, Y0, xval, yval;// latitude0,
        double e1, e2, f, a, ee, NN, T, C, M, D, R, u, fai, iPI;
        iPI = 0.0174532925199433; // //3.1415926535898/180.0;
        // a = 6378245.0; f = 1.0/298.3; //54年北京坐标系参数
        a = 6378140.0;
        f = 1 / 298.257; // 80年西安坐标系参数
        ZoneWide = 6; // //6度带宽
        ProjNo = (int) (X / 1000000L); // 查找带号
        longitude0 = (ProjNo - 1) * ZoneWide + ZoneWide / 2;
        longitude0 = longitude0 * iPI; // 中央经线

        X0 = ProjNo * 1000000L + 500000L;
        Y0 = 0;
        xval = X - X0;
        yval = Y - Y0; // 带内大地坐标
        e2 = 2 * f - f * f;
        e1 = (1.0 - Math.sqrt(1 - e2)) / (1.0 + Math.sqrt(1 - e2));
        ee = e2 / (1 - e2);
        M = yval;
        u = M / (a * (1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256));
        fai = u + (3 * e1 / 2 - 27 * e1 * e1 * e1 / 32) * Math.sin(2 * u) + (21 * e1 * e1 / 16 - 55 * e1 * e1 * e1 * e1 / 32) * Math.sin(4 * u) + (151 * e1 * e1 * e1 / 96) * Math.sin(6 * u) + (1097 * e1 * e1 * e1 * e1 / 512) * Math.sin(8 * u);
        C = ee * Math.cos(fai) * Math.cos(fai);
        T = Math.tan(fai) * Math.tan(fai);
        NN = a / Math.sqrt(1.0 - e2 * Math.sin(fai) * Math.sin(fai));
        R = a * (1 - e2) / Math.sqrt((1 - e2 * Math.sin(fai) * Math.sin(fai)) * (1 - e2 * Math.sin(fai) * Math.sin(fai)) * (1 - e2 * Math.sin(fai) * Math.sin(fai)));
        D = xval / NN;
        // 计算经度(Longitude) 纬度(Latitude)
        longitude1 = longitude0 + (D - (1 + 2 * T + C) * D * D * D / 6 + (5 - 2 * C + 28 * T - 3 * C * C + 8 * ee + 24 * T * T) * D * D * D * D * D / 120) / Math.cos(fai);
        latitude1 = fai - (NN * Math.tan(fai) / R) * (D * D / 2 - (5 + 3 * T + 10 * C - 4 * C * C - 9 * ee) * D * D * D * D / 24 + (61 + 90 * T + 298 * C + 45 * T * T - 256 * ee - 3 * C * C) * D * D * D * D * D * D / 720);
        // 转换为度 DD
        output[0] = longitude1 / iPI;
        output[1] = latitude1 / iPI;
        return output;
    }

    /**
     * 由经纬度反算成高斯投影坐标
     *
     * @param longitude
     * @param latitude
     */
    private double[] BLToGauss(double longitude, double latitude) {

        int ProjNo = 0;

        // 带宽
        int ZoneWide = 6;

        double longitude1, latitude1, longitude0, X0, Y0, xval, yval;
        double a, f, e2, ee, NN, T, C, A, M, iPI;

        // 3.1415926535898/180.0;
        iPI = 0.0174532925199433;

        // 54年北京坐标系参数
        a = 6378245.0;
        f = 1.0 / 298.3;

        // 80年西安坐标系参数
        // a=6378140.0;
        // f=1/298.257;

        ProjNo = (int) (longitude / ZoneWide);
        longitude0 = ProjNo * ZoneWide + ZoneWide / 2;
        longitude0 = longitude0 * iPI;

        // 经度转换为弧度
        longitude1 = longitude * iPI;

        // 纬度转换为弧度
        latitude1 = latitude * iPI;

        e2 = 2 * f - f * f;
        ee = e2 * (1.0 - e2);
        NN = a / Math.sqrt(1.0 - e2 * Math.sin(latitude1) * Math.sin(latitude1));
        T = Math.tan(latitude1) * Math.tan(latitude1);
        C = ee * Math.cos(latitude1) * Math.cos(latitude1);
        A = (longitude1 - longitude0) * Math.cos(latitude1);
        M = a * ((1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256) * latitude1 - (3 * e2 / 8 + 3 * e2 * e2 / 32 + 45 * e2 * e2 * e2 / 1024) * Math.sin(2 * latitude1) + (15 * e2 * e2 / 256 + 45 * e2 * e2 * e2 / 1024) * Math.sin(4 * latitude1) - (35 * e2 * e2 * e2 / 3072) * Math.sin(6 * latitude1));
        xval = NN * (A + (1 - T + C) * A * A * A / 6 + (5 - 18 * T + T * T + 72 * C - 58 * ee) * A * A * A * A * A / 120);
        yval = M + NN * Math.tan(latitude1) * (A * A / 2 + (5 - T + 9 * C + 4 * C * C) * A * A * A * A / 24 + (61 - 58 * T + T * T + 600 * C - 330 * ee) * A * A * A * A * A * A / 720);
        X0 = 1000000L * (ProjNo + 1) + 500000L;
        Y0 = 0;
        xval = xval + X0;
        yval = yval + Y0;
        return new double[] { xval, yval };
    }

}
