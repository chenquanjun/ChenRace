package com.workinggroup.chenfamily.gpsracecar.databasehelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.workinggroup.chenfamily.gpsracecar.util.FileUtil;

public class DBHelper extends SQLiteOpenHelper {
	
		public String LOG_TAG = "db";
		//Database Name
		private final static String DB_NAME = "race.db";
		//Database Version
		private final static int DB_VERSION = 6;
			
		//TABLE NAME
		public static final String TABLE_RACE_ROAD = "raceroad";
        public static final String TABLE_RACE_TRACK = "racetrack";



		
		//TABLE raceroad - field name
		public static final String CHATRECORD_KEY_ID = "id";
		public static final String CHATRECORD_KEY_NAME = "name";
		public static final String CHATRECORD_KEY_LATITUDE = "latitude";
		public static final String CHATRECORD_KEY_LONGITUDE = "longitude";
		public static final String CHATRECORD_KEY_lastMessageTime = "lastMessageTime";
		


		
		public DBHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
			// TODO Auto-generated constructor stub
		}
		
		private static DBHelper dbhelper;
		public static synchronized DBHelper getInstance(Context ctx) {
			 if(dbhelper == null){  
				 dbhelper = new DBHelper(ctx);  
		     }  
		     return dbhelper;  
		}
		
		private static SQLiteDatabase db;
		public static synchronized SQLiteDatabase getDBInstance(Context ctx) {
			if(db == null || !db.isOpen() || !db.isDbLockedByCurrentThread()){  
				db = DBHelper.getInstance(ctx).getWritableDatabase();
		     }  
		     return db;  
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub

		    	Log.v(LOG_TAG, "DBHelper, onCreate, starts");
				
				String create_table_raceroad= "CREATE TABLE " + TABLE_RACE_ROAD + "("
						+ CHATRECORD_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
						+ "," + CHATRECORD_KEY_NAME + " TEXT NOT NULL"
						+ "," + CHATRECORD_KEY_LATITUDE + " TEXT NOT NULL"
						+ "," + CHATRECORD_KEY_LONGITUDE + " TEXT NOT NULL"
						+ "," + CHATRECORD_KEY_lastMessageTime + " DATETIME default CURRENT_TIMESTAMP"
						+ ")";
				db.execSQL(create_table_raceroad);
				
//				String create_table_userinfo = "CREATE TABLE " + TABLE_USERINFO + "("
//						+ USERINFO_KEY_USERNAME + " TEXT PRIMARY KEY"
//						+ "," + USERINFO_KEY_NICKNAME + " TEXT NOT NULL"
//						+ "," + USERINFO_KEY_PHOTO + " TEXT"
//						+ "," + USERINFO_KEY_STATUS + " TEXT NOT NULL"
//						+ "," + USERINFO_KEY_BLOCKED + " TEXT NOT NULL DEFAULT 'N'"
//						+ ")";
//				db.execSQL(create_table_userinfo);
				

	            
	            Log.i(LOG_TAG, "DBHelper onCreate completed");

		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(LOG_TAG, "DBHelper onUpgrade" + " oldVerions=" + oldVersion + " newVersion=" + newVersion);
			
			db.beginTransaction();  //Manual start transaction
			try { //The DB must be upgraded stepwise, e.g. v1 -> v2 -> v3, CANNOT upgrade like v1 -> v3
				int upgradeTo = oldVersion + 1;
		        while (upgradeTo <= newVersion)
		        {
		            switch (upgradeTo)
		            {
		                case 2: {
		                   // upgradeToVersion2(db);
		                    break;
		                }

		            }
		            upgradeTo++;
		        }
		        db.setTransactionSuccessful(); //transaction success, if not set, the transaction will roll back
				Log.w(LOG_TAG, "DBHelper onUpgrade success");
			} catch (Exception e) {
				e.printStackTrace();
				Log.w(LOG_TAG, "DBHelper onUpgrade failed");
			} finally {
				db.endTransaction(); //transaction finished
			}
		}
		
		/*Function not available below Api11*/
		/*@Override
		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			super.onDowngrade(db, oldVersion, newVersion);
		}*/
		
		/**
		 * Save Database to SDCard, for debug use.
		 */
		public static void saveDatabaseToSdcard() {
			try {

//		        File sd = Environment.getExternalStorageDirectory();
			    File sd = new File(FileUtil.EXTRACTED_DB_FILEPATH());
		        File data = Environment.getDataDirectory();

		        if (sd.canWrite()) {
		            String currentDBPath = "/data/com.workinggroup.chenfamily.gpsracecar/databases/" + DB_NAME;
		            String backupDBPath = FileUtil.getExtractDbFileName(DB_VERSION) + ".db";
		            File currentDB = new File(data, currentDBPath);
		            File backupDB = new File(sd, backupDBPath);

		            if (currentDB.exists()) {
		                FileChannel src = new FileInputStream(currentDB).getChannel();
		                FileChannel dst = new FileOutputStream(backupDB).getChannel();
		                dst.transferFrom(src, 0, src.size());
		                src.close();
		                dst.close();
		            }
		        }
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		}
		

		
		public static void deleteWholeDatabase(Context context) {
			context.deleteDatabase(DB_NAME);
		}
}
