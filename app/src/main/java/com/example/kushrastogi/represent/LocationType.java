package com.example.kushrastogi.represent;

class LocationType {
    String type;
    String ZIP;
    String city;
    String state;

    LocationType(String type, String zip) {
        this.type = type;
        this.ZIP = zip;
    }

    LocationType(String type, String city, String state) {
        this.type = type;
        this.city = city;
        this.state = state;
    }

    String getVal() {
        if (type == "ZIP") {
            return "ZIP: " + ZIP;
        } else {
            return city + ", " + state;
        }
    }
}
