package com.example.kushrastogi.represent;

import android.util.Log;

import java.util.HashMap;

public class AppUtils {
    static void printRepresentatives(HashMap<String, Representative> reps) {
        if (reps == null) {
            return;
        }
        for (String key: reps.keySet()) {
            Log.d("representatives", key + " " + reps.get(key));
        }
    }
}
