package com.example.reseauroutier.drawroutemaps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class DrawMarker {

    public static DrawMarker INSTANCE;

    public static DrawMarker getInstance(Context context) {
        INSTANCE = new DrawMarker(context);
        return INSTANCE;
    }

    private final Context context;

    DrawMarker(Context context) {
        this.context = context;
    }

    public void draw(GoogleMap googleMap, LatLng location, int resDrawable, String title) {
        Drawable circleDrawable = ContextCompat.getDrawable(context, resDrawable);
        assert circleDrawable != null;
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);

        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title(title)
                .icon(markerIcon)
        );
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
