package com.example.reseauroutier;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomMarkerInfoWindow implements GoogleMap.InfoWindowAdapter {

    private final Activity context;

    public CustomMarkerInfoWindow(Activity context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        @SuppressLint("InflateParams") View view = context.getLayoutInflater().inflate(R.layout.markerinfowindow, null);

        TextView title = view.findViewById(R.id.title);
        TextView snippet = view.findViewById(R.id.snippet);

        title.setText(marker.getTitle());
        snippet.setText(marker.getSnippet());
        return view;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        @SuppressLint("InflateParams") View view = context.getLayoutInflater().inflate(R.layout.markerinfowindow, null);

        TextView title = view.findViewById(R.id.title);
        TextView snippet = view.findViewById(R.id.snippet);

        title.setText(marker.getTitle());
        snippet.setText(marker.getSnippet());
        return view;
    }
}
