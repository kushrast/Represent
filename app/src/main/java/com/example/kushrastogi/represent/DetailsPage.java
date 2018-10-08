package com.example.kushrastogi.represent;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DetailsPage extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_details_page);

        Intent intent = getIntent();
        String rep_id = intent.getStringExtra("id");

        RepresentApplication app = (RepresentApplication) getApplication();

        Representative representative = app.getRepresentatives().get(rep_id);

        TextView rep_name = (TextView) findViewById(R.id.representative_name);
        rep_name.setText(representative.getName());

        TextView rep_title = (TextView) findViewById(R.id.representative_title);
        rep_title.setText(representative.getRepr_type());

        TextView rep_email = (TextView) findViewById(R.id.representative_email);
        if (representative.getEmail() == null || representative.getEmail().equals("null")) {
            rep_email.setText("Unavailable");
            rep_email.setTypeface(rep_email.getTypeface(), Typeface.ITALIC);
        } else {
            rep_email.setText(representative.getEmail());
        }

        TextView rep_web = (TextView) findViewById(R.id.representative_website);
        if (representative.getWebsite() == null || representative.getWebsite().equals("null")) {
            rep_web.setText("Unavailable");
            rep_web.setTextColor(0xff112e51);
            rep_web.setTypeface(rep_email.getTypeface(), Typeface.ITALIC);
        } else {
            rep_web.setText(representative.getWebsite());
        }

        TextView party = (TextView) findViewById(R.id.representative_party);
        party.setText(representative.party);
        if (representative.party.equals("Republican")) {
            party.setTextColor(0xff981b1e);
        } else if (representative.party.equals("Democrat")) {
            party.setTextColor(0xff205493);
        } else {
            party.setTextColor(0xff4c2c92);
        }

        ImageView image = (ImageView) findViewById(R.id.representative_image);
        String image_url = String.format("http://bioguide.congress.gov/bioguide/photo/%s/%s.jpg\n", representative.id.charAt(0), representative.id);
        Log.d("image url", image_url);

        TextView term_ends = (TextView) findViewById(R.id.representative_term);
        if (representative.getTerm() != null) {
            Date termEnd = representative.getTerm();
            term_ends.setText("Term ends: " + termEnd.toString());
            Date curr = new Date();
            if (termEnd.getYear() - curr.getYear() <= 1) {
                term_ends.setBackgroundColor(0xffFF8C00);
            }
        }

        new DownloadImageTask(image, representative.party)
                .execute(image_url);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        String party;

        public DownloadImageTask(ImageView bmImage, String party) {
            this.bmImage = bmImage;
            this.party = party;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
