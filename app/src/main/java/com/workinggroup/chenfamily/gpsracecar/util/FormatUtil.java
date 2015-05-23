package com.workinggroup.chenfamily.gpsracecar.util;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.media.MediaPlayer;
import android.util.Log;
import android.webkit.MimeTypeMap;

public class FormatUtil {
    public static final String RACE_ROAD_LIST = "raceRoadList";
    public  final static String RACE_ROAD_LIST_KEY = "com.tutor.objecttran.par";
    public static int MapRate = 16;
	public static String convertDurationSecondsToTimeStr(long durationSeconds){
		
		String durationTimeString = "";
		String strSeconds ="";
		String strMinutes ="";
		String strHours ="";
		
		int seconds = (int) durationSeconds % 60 ;
		int minutes = (int) ((durationSeconds / 60) % 60);
		int hours   = (int) ((durationSeconds / (60*60)) % 24);
		
		strSeconds =Integer.toString(seconds);
		strMinutes =Integer.toString(minutes);
		strHours =Integer.toString(hours);
		
		if (seconds < 10){
			strSeconds = "0"+strSeconds;
		}
		if (minutes < 10){
			strMinutes = "0"+strMinutes;
		}
		if (hours < 10){
			strHours = "0"+strHours;
		}
		durationTimeString = strHours +":" + strMinutes+":"+strSeconds;
		return durationTimeString;
	}
	
	
	public static String convertLongToStr(long dt){
		
		Date date = new Date(dt);
    	SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	df2.setTimeZone(TimeZone.getTimeZone("GMT+0000"));
    	String dateText = df2.format(date);
		return dateText;
	}
	
	public static String convertLongToCalendar(long dt){
		
		Date date = new Date(dt);
    	SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmssSS");
    	String dateText = df2.format(date);
		return dateText;
	}
	
	public static String convertDateToCalendarStr(Date date){
    	SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmssSS");
    	String dateText = df2.format(date);
		return dateText;
	}

	/**
	 * Convert Date to String in HK TimeZone
	 * @param date
	 * @param format Format of the output string (e.g. "yyyy-MM-dd HH:mm:ss", "HH:mm:ss")
	 * @return output string
	 */
	public static String convertDateToStr(Date date, String format){
	    SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
	    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0000"));
		String dateStr = "";
		if (date != null){
			dateStr = dateFormat.format(date);
		} else {
			dateStr = dateFormat.format(new Date());
		}
		return dateStr;
	}
	
	/**
	 * Convert Date to String in Phone TimeZone
	 * @param date
	 * @param format Format of the output string (e.g. "yyyy-MM-dd HH:mm:ss", "HH:mm:ss")
	 * @return output string
	 */
	public static String convertDateToStrOnPhoneTimeZone(Date date, String format){
	    SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
	    dateFormat.setTimeZone(TimeZone.getDefault());
		String dateStr = "";
		if (date != null){
			dateStr = dateFormat.format(date);
		} else {
			dateStr = dateFormat.format(new Date());
		}
		return dateStr;
	}
	
	/**
	 * Convert Date to String - For Audio Record FileName ONLY
	 * @param date
	 * @param format
	 * @return
	 */
	public static String convertDateToStrForAudioFileName(){
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
	    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0000"));
		String dateStr = dateFormat.format(new Date());
		return dateStr;
	}


    public static String convertCurrentChinaDateToStr(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS", Locale.CHINA);
       // dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0000"));
        String dateStr = dateFormat.format(new Date());
        return dateStr;
    }

	public static Date convertStrToDate(String dateStr){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0000"));
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (Exception e){
			date = null;
		}
		return date;
	}
	
	// url = file path or whatever suitable URL you want.
	public static String getMimeType(String url){
	    String type = null;
	    String extension = MimeTypeMap.getFileExtensionFromUrl(url);
	    if (extension != null) {
	        MimeTypeMap mime = MimeTypeMap.getSingleton();
	        type = mime.getMimeTypeFromExtension(extension);
	    }
	    return type;
	}
	
	/**
	 * To add hack to .3gp file
	 * @param url
	 * @return
	 */
	public static String getAudioMimeType(String url){
	    String type = null;
	    String extension = MimeTypeMap.getFileExtensionFromUrl(url);
	    Log.w("KKIM", " Sending File Extension = " + extension);
	    if (extension != null) {
	    	if (extension.equalsIgnoreCase("3gp") || extension.equalsIgnoreCase("3gpp")) {
	    		if (is3gpFileAudio(url)) {
	    			type = "audio/3gpp";
	    		} else {
	    			MimeTypeMap mime = MimeTypeMap.getSingleton();
			        type = mime.getMimeTypeFromExtension(extension);
	    		}
	    	} else {
		        MimeTypeMap mime = MimeTypeMap.getSingleton();
		        type = mime.getMimeTypeFromExtension(extension);
	    	}
	    	
	    	if (type.equalsIgnoreCase("application/ogg")) {
	    		type = "audio/ogg";
            	Log.d("KKIM", "Formatting Audio File Type from application/ogg to audio/ogg");
            }
	    }
	    return type;
	}
	
	public static boolean is3gpFileAudio(String url) { 
        int height = 0;
        File mediaFile = new File(url);
        try {
            MediaPlayer mp = new MediaPlayer();
            FileInputStream fs;
            FileDescriptor fd;
            fs = new FileInputStream(mediaFile);
            fd = fs.getFD();
            mp.setDataSource(fd);
            mp.prepare(); 
            height = mp.getVideoHeight();
            mp.release();
        } catch (Exception e) {
            Log.e("KKIM", "Exception trying to determine if 3gp file is video.", e);
        }
        Log.i("KKIM", "The height of the file is " + height);
        return height == 0;
    }
}
