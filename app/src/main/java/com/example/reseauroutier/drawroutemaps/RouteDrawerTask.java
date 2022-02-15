package com.example.reseauroutier.drawroutemaps;

import android.os.AsyncTask;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.reseauroutier.MapsActivity;
import com.example.reseauroutier.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RouteDrawerTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    private final GoogleMap mMap;

    public RouteDrawerTask(GoogleMap mMap) {
        this.mMap = mMap;
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            Log.d("RouteDrawerTask", jsonData[0]);
            DataRouteParser parser = new DataRouteParser();
            Log.d("RouteDrawerTask", parser.toString());

            // Starts parsing data
            routes = parser.parse(jObject);
            Log.d("RouteDrawerTask", "Executing routes");
            Log.d("RouteDrawerTask", routes.toString());

        } catch (Exception e) {
            Log.d("RouteDrawerTask", e.toString());
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        if (result != null)
            drawPolyLine(result, MapsActivity.chantiers);
    }

    private void drawPolyLine(List<List<HashMap<String, String>>> result, ArrayList<LatLng> chantiers) {
        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;
        boolean isAvoided;
        for (int i = 0; i < result.size(); i++) {
            isAvoided = false;
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                if (isAvoided) {
                    continue;
                }
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(Objects.requireNonNull(point.get("lat")));
                double lng = Double.parseDouble(Objects.requireNonNull(point.get("lng")));
                LatLng position = new LatLng(lat, lng);
                for (LatLng item : chantiers) {
                    if (position.latitude == item.latitude && position.longitude == item.longitude) {
                        isAvoided = true;
                        break;
                    }
                }
                if (!isAvoided) {
                    points.add(position);
                }
            }
            // Adding all the points in the route to LineOptions
            if (!isAvoided) {
                lineOptions.addAll(points);
                lineOptions.width(9);
                int routeColor = ContextCompat.getColor(DrawRouteMaps.getContext(), R.color.colorRouteLine);
                if (routeColor == 0)
                    lineOptions.color(0xFF0A8F08);
                else
                    lineOptions.color(routeColor);
            }
        }

        // Drawing polyline in the Google Map for the i-th route
        if (mMap != null) {
            mMap.addPolyline(lineOptions);
        } else {
            Log.d("onPostExecute", "without Polylines draw");
        }
    }

}
