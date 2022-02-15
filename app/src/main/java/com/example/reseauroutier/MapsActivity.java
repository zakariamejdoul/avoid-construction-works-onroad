package com.example.reseauroutier;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.reseauroutier.databinding.ActivityMapsBinding;
import com.example.reseauroutier.drawroutemaps.DrawMarker;
import com.example.reseauroutier.drawroutemaps.DrawRouteMaps;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    public Database db;
    public static ArrayList<DataModel> data;
    protected LocationManager locationManager;
    public static Location currentCoord = null;
    public static ArrayList<LatLng> chantiers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.reseauroutier.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new Database(MapsActivity.this);

        if (db.select().getCount() == 0) {
            db.insert(1, "Avenue de la victoire", "Rabat", "34.0106232", "-6.8459628", "09/02/2022", "16/02/2022", "Travaux de réfection");
            db.insert(2, "Av. Oqba Ibn Naafi", "Rabat", "34.0033901", "-6.8514649", "07/02/2022", "10/02/2022", "Travaux d’entretien");
        }

//        db.delete(3);
//        if (db.select().getCount() == 2) {
//            db.insert(3, "Avenue Allal ElFassi", "Rabat", "33.99932", "-6.83507", "09/02/2022", "16/02/2022", "Travaux de construction");
//        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//             TODO: Consider calling
//                ActivityCompat#requestPermissions
//             here to request the missing permissions, and then overriding
//               public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                      int[] grantResults)
//             to handle the case where the user grants the permission. See the documentation
//             for ActivityCompat#requestPermissions for more details.
            return;
        }
        boolean gps_enabled;
        boolean network_enabled;

        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location net_loc = null, gps_loc = null;

        if (gps_enabled)
            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (network_enabled)
            net_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (gps_loc != null && net_loc != null) {

            //smaller the number more accurate result will
            if (gps_loc.getAccuracy() > net_loc.getAccuracy())
                currentCoord = net_loc;
            else
                currentCoord = gps_loc;

        } else {

            if (gps_loc != null) {
                currentCoord = gps_loc;
            } else if (net_loc != null) {
                currentCoord = net_loc;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentCoord = location;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    public static class DataModel {
        String id;
        String avenue;
        String ville;
        String lat;
        String lng;
        String date_debut;
        String date_fin;
        String observation;

        public DataModel(String id, String avenue, String ville, String lat, String lng, String date_debut, String date_fin, String observation) {
            this.id = id;
            this.avenue = avenue;
            this.ville = ville;
            this.lat = lat;
            this.lng = lng;
            this.date_debut = date_debut;
            this.date_fin = date_fin;
            this.observation = observation;
        }

        public String getId() {
            return id;
        }

        public String getAvenue() {
            return avenue;
        }

        public String getVille() {
            return ville;
        }

        public String getLat() {
            return lat;
        }

        public String getLng() {
            return lng;
        }

        public String getDate_debut() {
            return date_debut;
        }

        public String getDate_fin() {
            return date_fin;
        }

        public String getObservation() {
            return observation;
        }
    }

    public void fetchData() {
        Cursor cursor = db.select();
        data = new ArrayList<>();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Aucun chantier à localiser !", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                DataModel dataModel = new DataModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
                data.add(dataModel);
                chantiers.add(new LatLng(Double.parseDouble(cursor.getString(3)), Double.parseDouble(cursor.getString(4))));
            }
        }

        db.close();
        cursor.close();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        fetchData();   // Fetch location data from database
        googleMap.setInfoWindowAdapter(new CustomMarkerInfoWindow(MapsActivity.this));
        // Add markers fetched from Database to googleMap
        for (DataModel i : data) {
            LatLng chantier = new LatLng(Double.parseDouble(i.getLat()), Double.parseDouble(i.getLng()));
            Objects.requireNonNull(googleMap.addMarker(new MarkerOptions().position(chantier)
                    .title(i.getObservation())
                    .snippet(i.getAvenue() + ", " + i.getVille() + "\n" + i.getDate_debut() + " - " + i.getDate_fin())
                    .icon(BitmapFromVector(getApplicationContext(), R.mipmap.ic_construction))));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chantier, 12));
        }
        LatLng origin = new LatLng(currentCoord.getLatitude(), currentCoord.getLongitude());    // Origin Location
        LatLng destination = new LatLng(34.0439477, -6.8251222);    // Destination Location
        DrawRouteMaps.getInstance(this).draw(origin, destination, googleMap);
        DrawMarker.getInstance(this).draw(googleMap, origin, R.drawable.marker_a, "Localisation Origine");
        DrawMarker.getInstance(this).draw(googleMap, destination, R.drawable.marker_b, "Localisation Destination");

        // Display Size
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));
    }

    @NonNull
    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}