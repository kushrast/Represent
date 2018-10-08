package com.example.kushrastogi.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

public class MainPage extends AppCompatActivity {

    LocationType lt;
    HashMap<String, Representative> reps;
    ArrayList<Representative> senators;
    ArrayList<Representative> housereps;

    private ListView senate_grid;
    private ListView rep_grid;
    private RepresentativeAdapter senate_adapter;
    private RepresentativeAdapter rep_adapter;

    RepresentApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_page);
        app = (RepresentApplication)getApplication();

        senators = new ArrayList<>();
        housereps = new ArrayList<>();

        senate_grid = (ListView) findViewById(R.id.senate_layout);
        rep_grid = (ListView) findViewById(R.id.representative_layout);

        senate_grid.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }

        });

        rep_grid.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }

        });

        refresh();


    }

    void changeLocation(View v) {
        Log.d("main", "ending activity");
        finish();
    }

    private void refresh() {
        senators.clear();
        housereps.clear();
        lt = app.getLt();
        reps = app.getRepresentatives();

        TextView location = (TextView) findViewById(R.id.location);
        location.setText(lt.getVal());

        for (Representative rep: reps.values()) {
            if (rep.getRepr_type().equals("senator")) {
                senators.add(rep);
            } else {
                housereps.add(rep);
            }
        }

        Log.d("senators", senators.toString());

        senate_adapter = new RepresentativeAdapter(this, senators);
        senate_grid.setAdapter(senate_adapter);

        rep_adapter = new RepresentativeAdapter(this, housereps);
        rep_grid.setAdapter(rep_adapter);
    }
}
