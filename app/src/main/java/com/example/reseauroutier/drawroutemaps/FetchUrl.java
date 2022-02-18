package com.example.reseauroutier.drawroutemaps;

import com.example.reseauroutier.R;
import com.google.android.gms.maps.model.LatLng;

public class FetchUrl {

    public static String key = "YOUR GOOGLE MAPS API KEY VALUE";

    public static String getUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String apiKey = "key=" + key;
        String alts = "alternatives=" + true;   // Get more than one route
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + alts + "&" + apiKey;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }
}
