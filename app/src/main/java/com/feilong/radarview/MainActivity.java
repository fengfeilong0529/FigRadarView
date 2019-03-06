package com.feilong.radarview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.feilong.radarviewlib.RadarView;

public class MainActivity extends AppCompatActivity {

    private RadarView mRadarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRadarView = (RadarView) findViewById(R.id.radarView);
    }

    public void randomArea(View view) {
        mRadarView.radomArea();
    }
}
