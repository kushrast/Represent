package com.example.kushrastogi.represent;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

class Representative {
    String first_name;
    String last_name;
    String id;
    String party;

    String district;
    String repr_type;
    String website;
    String email;
    Date end;

    ArrayList<String> committees;

    Representative(String first, String last, String id, String type, String party, String district) {
        this.first_name = first;
        this.last_name = last;
        this.id = id;
        if (type != null) {
            this.repr_type = type.substring(0,1).toUpperCase() + type.substring(1);
        }
        this.party = party;
        this.district = district;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Representative{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", repr_type='" + repr_type + '\'' +
                '}';
    }

    public String getName() {
        return first_name + " " + last_name;
    }

    public String getRepr_type() {
        return repr_type;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getCommittees() {
        return committees;
    }

    public void setCommittees(ArrayList<String> committees) {
        this.committees = committees;
    }

    public String getDistrict() {
        return district;
    }

    public void setEndDate(String endDate) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            this.end = df.parse(endDate);
        } catch (java.text.ParseException p) {
            p.printStackTrace();
        }
    }

    public Date getTerm() {
        return this.end;
    }

    public String getTermString() {
        SimpleDateFormat sm = new SimpleDateFormat("MMMM yyyy", Locale.US);
        return sm.format(this.end);
    }
}
