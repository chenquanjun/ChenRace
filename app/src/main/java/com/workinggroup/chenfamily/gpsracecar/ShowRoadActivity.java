package com.workinggroup.chenfamily.gpsracecar;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.workinggroup.chenfamily.gpsracecar.View.RaceRoadView;
import com.workinggroup.chenfamily.gpsracecar.util.FormatUtil;

import java.util.ArrayList;


public class ShowRoadActivity extends ActionBarActivity {

    RaceRoadView raceRoadView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_show_road);
        raceRoadView =new RaceRoadView(this);
        setContentView(raceRoadView);
        ArrayList raceRoadList =(ArrayList) getIntent().getSerializableExtra(FormatUtil.RACE_ROAD_LIST_KEY);
        raceRoadView.setRaceRoadList(raceRoadList);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_road, menu);
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
}
