package com.example.kushrastogi.represent;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FrontPage extends Activity {

    private FusedLocationProviderClient LocationProvider;
    RequestQueue queue;
    private Toast toast_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView((int)R.layout.activity_front_page);

        LocationProvider = LocationServices.getFusedLocationProviderClient(this);

        //Remove title bar

        Log.d("create", "created!");
        queue = Volley.newRequestQueue(this);
        queue.start();

    }

    public void getLocation(View v) {
        Log.d("Location", "getLocation function run");
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
        } else {
            // Permission has already been granted
            fetchLocation();
        }
    }

    public void onRequestPermissionResults(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
            }
        }
    }

    private void fetchLocation() {
        Log.d("Location", "ran fetchLocation");
                //do stuff here
                try {
                    LocationProvider.getLastLocation()
                            .addOnSuccessListener(FrontPage.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(final Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        new Thread(new Runnable() {
                                            public void run() {
                                                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                                                Log.d("hello", Boolean.toString(Looper.myLooper() == Looper.getMainLooper()));
                                                show_toast();
                                                // Logic to handle location object
                                                CompletableFuture<String> response = loc_request(location.getLatitude(), location.getLongitude());
                                                try {
                                                    String results = response.get();
                                                    boolean val = storeDataAndStartMain(results);
                                                    if (val) {
                                                        Log.d("process", "Successful Parsing");
                                                    }
                                                } catch (java.util.concurrent.ExecutionException je) {
                                                    je.printStackTrace();
                                                } catch (java.lang.InterruptedException ie) {
                                                    ie.printStackTrace();
                                                }
                                            }
                                        }).start();
                                    }
                                }
                            });
                } catch(SecurityException e) {
                    e.getStackTrace();
                }
    }

    public void processZip(View v) {
        new Thread(new Runnable(){
            public void run(){
                //do stuff here
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                EditText zip_edit = (EditText)findViewById(R.id.zip_code);
                String code = zip_edit.getText().toString();

                if (!code.equals("")) {
                    show_toast();
                    CompletableFuture<String> response = zip_request(code);
                    try {
                        String results = response.get();
                        boolean val = storeDataAndStartMain(results);
                        if (val) {
                            Log.d("process", "Successful Parsing");
                        }
                    } catch (java.util.concurrent.ExecutionException je) {
                        je.printStackTrace();
                    } catch (java.lang.InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void processRandom(View v) {
        new Thread(new Runnable(){
            public void run(){
                //do stuff here
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                while (true) {
                    show_toast();
                    SecureRandom random = new SecureRandom();
                    int num = random.nextInt(100000);
                    String formatted = String.format("%05d", num);
                    CompletableFuture<String> response = zip_request(formatted);
                    try {
                        String results = response.get();
                        boolean val = storeDataAndStartMain(results);
                        if (val) {
                            Log.d("process", "Successful Parsing");
                            break;
                        }
                    } catch (java.util.concurrent.ExecutionException je) {
                        je.printStackTrace();
                    } catch (java.lang.InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private CompletableFuture<String> loc_request(double lati, double longi) {
        String url = String.format("https://api.geocod.io/v1.3/reverse?q=%s,%s&fields=cd&api_key=%s", lati, longi, getString(R.string.geocodiokey));
        return geocode_request_handler(url);
    }

    private CompletableFuture<String> zip_request(String zip) {
        String url = String.format("https://api.geocod.io/v1.3/geocode?postal_code=%s&fields=cd&api_key=%s", zip, getString(R.string.geocodiokey));
        return geocode_request_handler(url);
    }

    private CompletableFuture<String> geocode_request_handler(String url) {
        Log.d("request", url);
        final CompletableFuture<String> resp = new CompletableFuture<String>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        resp.complete(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("geocode",error.networkResponse.toString());
                resp.complete(null);
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

        return resp;
    }

    private CompletableFuture<String> propublica_request_handler(String id) {
        String url = String.format("https://api.propublica.org/congress/v1/members/%s.json", id);
        Log.d("request", url);
        final CompletableFuture<String> resp = new CompletableFuture<String>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        resp.complete(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("geocode",error.toString());
                resp.complete(null);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-API-Key", getString(R.string.propublica));

                return params;
            }
        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);

        return resp;
    }

    LocationType parseResults(String resp) {
        try {
            JSONObject job = new JSONObject(resp);
            JSONArray results = job.getJSONArray("results");
            boolean has_input = job.has("input");
            String type;
            if (has_input) {
                type = "ZIP";
                String zip = job.getJSONObject("input").getJSONObject("address_components").getString("zip");
                if (results.length() > 0) {
                    return new LocationType(type, zip);
                }
            } else {
                type = "Location";
                if (results.length() > 0) {
                    JSONObject result = results.getJSONObject(0);
                    JSONObject address = result.getJSONObject("address_components");
                    String city = address.getString("city");
                    String state = address.getString("state");
                    return new LocationType("city", city, state);
                }
            }
        } catch (JSONException jse) {
            jse.printStackTrace();
        }

        return null;
    }

    HashMap<String, Representative> parseRepresentatives(String resp) {
        try {
            HashMap<String, Representative> representatives = new HashMap<>();
            JSONObject job = new JSONObject(resp);
            JSONArray results = job.getJSONArray("results");
            for (int k = 0; k < results.length(); k++) {
                JSONObject result = results.getJSONObject(k);
                JSONObject address = result.getJSONObject("address_components");
                String state = String.format("State: %s", address.getString("state"));
                JSONObject fields = result.getJSONObject("fields");
                JSONArray districts = fields.getJSONArray("congressional_districts");
                for (int i = 0; i < districts.length(); i++) {
                    JSONObject district = districts.getJSONObject(i);
                    String district_num = String.format("District %s", district.getString("district_number"));
                    JSONArray legislators = district.getJSONArray("current_legislators");
                    for (int j = 0; j < legislators.length(); j++) {
                        JSONObject legislator = legislators.getJSONObject(j);
                        JSONObject references = legislator.getJSONObject("references");
                        String id = references.getString("bioguide_id");
                        if (!representatives.containsKey(id)) {
                            JSONObject bio = legislator.getJSONObject("bio");
                            JSONObject contact = legislator.getJSONObject("contact");
                            String first_name = bio.getString("first_name");
                            String last_name = bio.getString("last_name");
                            String type = legislator.getString("type");
                            String district_str;
                            if (type.equals("senator")) {
                                district_str = state;
                            } else {
                                district_str = district_num;
                            }
                            String party = bio.getString("party");
                            Representative rep = new Representative(first_name, last_name, id, type, party, district_str);
                            representatives.put(id, rep);
                        }
                    }
                }
            }
            return representatives;
        } catch (JSONException jse) {
            jse.printStackTrace();
        }

        return null;
    }

    void enrichRepresentatives(HashMap<String, Representative> reps) {
        for (String id: reps.keySet()) {
            Representative rep = reps.get(id);
            CompletableFuture<String> propublica_request = propublica_request_handler(id);
            try {
                String propublica_resp = propublica_request.get();
                parsePropublica(propublica_resp, rep);
            } catch (java.util.concurrent.ExecutionException je) {
                je.printStackTrace();
            } catch (java.lang.InterruptedException je) {
                je.printStackTrace();
            }
        }
    }

    void parsePropublica(String resp, Representative representative) {
        try {
            JSONObject job = new JSONObject(resp);
            JSONArray results = job.getJSONArray("results");
            if (results.length() > 0) {
                JSONObject information = results.getJSONObject(0);
                representative.setWebsite(information.getString("url"));
                JSONArray roles = information.getJSONArray("roles");
                if (roles.length() > 0) {
                    JSONObject recent_role = roles.getJSONObject(0);
                    String end = recent_role.getString("end_date");
                    representative.setEmail(recent_role.getString("contact_form"));
                    ArrayList<String> committees = new ArrayList<>();
                    JSONArray committee_objects = recent_role.getJSONArray("committees");
                    for (int i = 0; i < committee_objects.length() && i < 5; i++) {
                        JSONObject committee = committee_objects.getJSONObject(i);
                        String name = committee.getString("name");
                        Log.d("committees", name);
                        committees.add(name);
                    }

                    representative.setCommittees(committees);
                    representative.setEndDate(end);
                }
            }
        } catch (org.json.JSONException je) {
            je.printStackTrace();
        }

    }

    boolean storeDataAndStartMain(String results) {
        if (results != null) {
            LocationType lt = parseResults(results);
            if (lt != null) {
                HashMap<String, Representative> reps = parseRepresentatives(results);
                enrichRepresentatives(reps);
                RepresentApplication app = (RepresentApplication)getApplication();
                app.setLocation(lt);
                app.setRepresentatives(reps);
                Log.d("location", lt.getVal());
                hide_toast();
                Intent intent = new Intent(this, MainPage.class);
                startActivity(intent);
                return true;
            }
        }
        return false;
    }

    void show_toast(){
        runOnUiThread(new Runnable() {
            public void run() {
                Context context = getApplicationContext();
                CharSequence text = "Loading data";
                int duration = Toast.LENGTH_LONG;

                if (toast_obj == null || toast_obj.getView().getWindowVisibility() != View.VISIBLE) {
                    toast_obj = Toast.makeText(context, text, duration);
                    toast_obj.show();
                }
            }
        });
    }

    void hide_toast(){
        runOnUiThread(new Runnable() {
            public void run() {
                Context context = getApplicationContext();

                if (toast_obj != null && toast_obj.getView().getWindowVisibility() == View.VISIBLE) {
                    toast_obj.cancel();
                }
            }
        });
    }
}
