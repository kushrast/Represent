package com.example.kushrastogi.represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RepresentativeAdapter extends ArrayAdapter<Representative> {
    private Context mContext;
    private List<Representative> representatives = new ArrayList<>();

    public RepresentativeAdapter(@NonNull Context context, @NonNull ArrayList<Representative> representatives) {
        super(context, 0, representatives);
        mContext = context;
        this.representatives = representatives;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.representative_component,parent,false);

        final Representative representative = representatives.get(position);

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

        Glide.with(mContext)
                .load(image_url)
                .into(image);

        listItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //Do your task here
                Intent intent = new Intent(getContext(), DetailsPage.class);
                intent.putExtra("id", representative.id);
                getContext().startActivity(intent);
            }
        });

        return listItem;
    }

//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//        String party;
//
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//    }
}
