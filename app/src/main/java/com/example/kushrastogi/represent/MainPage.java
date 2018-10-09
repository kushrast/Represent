package com.example.kushrastogi.represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainPage extends AppCompatActivity {

    LocationType lt;
    HashMap<String, Representative> reps;
    ArrayList<Representative> senators;
    ArrayList<Representative> housereps;

    private LinearLayout senate_grid;
    private LinearLayout rep_grid;

    RepresentApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_page);
        app = (RepresentApplication)getApplication();

        senators = new ArrayList<>();
        housereps = new ArrayList<>();

        senate_grid = (LinearLayout) findViewById(R.id.senate_layout);
        rep_grid = (LinearLayout) findViewById(R.id.representative_layout);

        refresh();

        addRepresentatives(housereps, rep_grid);
        addRepresentatives(senators, senate_grid);
    }

    void addRepresentatives(List<Representative> reps, LinearLayout grid) {
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (final Representative representative: reps) {
            View listItem = vi.inflate(R.layout.representative_component, null);

            TextView rep_district = (TextView) listItem.findViewById(R.id.representative_district);
            rep_district.setText(representative.getDistrict());

            TextView rep_name = (TextView) listItem.findViewById(R.id.representative_name);
            rep_name.setText(representative.getName());

            TextView rep_email = (TextView) listItem.findViewById(R.id.representative_email);
            if (representative.getEmail() == null || representative.getEmail().equals("null")) {
                rep_email.setText("Unavailable");
                rep_email.setTypeface(rep_email.getTypeface(), Typeface.ITALIC);
            } else {
                rep_email.setText(representative.getEmail());
            }

            TextView rep_web = (TextView) listItem.findViewById(R.id.representative_website);
            if (representative.getWebsite() == null || representative.getWebsite().equals("null")) {
                rep_web.setText("Unavailable");
                rep_web.setTextColor(0xff112e51);
                rep_web.setTypeface(rep_email.getTypeface(), Typeface.ITALIC);
            } else {
                rep_web.setText(representative.getWebsite());
                rep_web.setMovementMethod(LinkMovementMethod.getInstance());
            }

            TextView party = (TextView) listItem.findViewById(R.id.representative_party);
            party.setText(representative.party);
            if (representative.party.equals("Republican")) {
                party.setTextColor(0xff981b1e);
            } else if (representative.party.equals("Democrat")) {
                party.setTextColor(0xff205493);
            } else {
                party.setTextColor(0xff4c2c92);
            }

            ImageView image = (ImageView) listItem.findViewById(R.id.representative_image);
            String image_url = String.format("http://bioguide.congress.gov/bioguide/photo/%s/%s.jpg", representative.id.charAt(0), representative.id);
            Log.d("image url", image_url);

            Glide.with(this)
                    .load(image_url)
                    .into(image);

            listItem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    //Do your task here
                    Intent intent = new Intent(MainPage.this, DetailsPage.class);
                    intent.putExtra("id", representative.id);
                    MainPage.this.startActivity(intent);
                }
            });

            try{

                grid.addView(listItem);
            }catch(Throwable e) {
                Log.e("BurgerClub", "MEX: " + e.getMessage());
                e.printStackTrace();
            }
        }
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
            if (rep.getRepr_type().equals("Senator")) {
                senators.add(rep);
            } else {
                housereps.add(rep);
            }
        }
    }
}
