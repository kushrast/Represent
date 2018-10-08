package com.example.kushrastogi.represent;

import android.app.Application;
import android.content.res.Configuration;
import android.location.Location;

import java.util.HashMap;

public class RepresentApplication extends Application {
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    LocationType lt;
    HashMap<String, Representative> representatives;

    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public void setLocation(LocationType lt) {
        this.lt = lt;
    }

    public LocationType getLt() {
        return lt;
    }

    public HashMap<String, Representative> getRepresentatives() {
        return representatives;
    }

    public void setRepresentatives(HashMap<String, Representative> representatives) {
        this.representatives = representatives;
    }
}