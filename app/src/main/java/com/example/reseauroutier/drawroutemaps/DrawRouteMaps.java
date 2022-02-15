package com.example.reseauroutier.drawroutemaps;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class DrawRouteMaps {

    @SuppressLint("StaticFieldLeak")
    private static DrawRouteMaps instance;
    private Context context;

    public static DrawRouteMaps getInstance(Context context) {
        instance = new DrawRouteMaps();
        instance.context = context;
        return instance;
    }

    public void draw(LatLng origin, LatLng destination, GoogleMap googleMap){
        String url_route = FetchUrl.getUrl(origin, destination);
        System.out.println(url_route);
        DrawRoute drawRoute = new DrawRoute(googleMap);
        drawRoute.execute(url_route);
    }

    public static Context getContext() {
        return instance.context;
    }
}
