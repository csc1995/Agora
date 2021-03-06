package edu.upc.pes.agora.Presentation;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.R;

public class AddLocationActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private double lat;
    private double lng;
    private Button savePos;
    private Button cancelPos;
    Polyline pol;
    Marker marker;
    int zone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        savePos = (Button) findViewById(R.id.btnSavePos);
        cancelPos = (Button) findViewById(R.id.btnCancelPos);


        savePos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                if (getIntent().hasExtra("CallingActivity")) {
                    if (getIntent().getStringExtra("CallingActivity").equals("Create")) {
                        i = new Intent(getApplicationContext(), CreateProposalActivity.class);
                    }else{
                        i = new Intent(getApplicationContext(), EditProposalActivity.class);
                    }
                }

                if (getIntent().hasExtra("Title")){
                    i.putExtra("Title", getIntent().getStringExtra("Title"));
                }
                if (getIntent().hasExtra("Description")){
                    i.putExtra("Description", getIntent().getStringExtra("Description"));
                }
                if (getIntent().hasExtra("Category")){
                    Log.i("asdaasd", "entraaqui");
                    i.putExtra("Category", getIntent().getIntExtra("Category",0));
                }
                if (getIntent().hasExtra("id")){
                    i.putExtra("id", getIntent().getIntExtra("id",0));
                }
                i.putExtra("lat", lat);
                i.putExtra("lng", lng);
                startActivity(i);

            }
        });

        cancelPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                if (getIntent().hasExtra("CallingActivity")) {
                    if (getIntent().getStringExtra("CallingActivity").equals("Create")) {
                        i = new Intent(getApplicationContext(), CreateProposalActivity.class);
                    }else{
                        i = new Intent(getApplicationContext(), EditProposalActivity.class);
                    }
                }

                if (getIntent().hasExtra("Title")){
                    i.putExtra("Title", getIntent().getStringExtra("Title"));
                }
                if (getIntent().hasExtra("Description")){
                    i.putExtra("Description", getIntent().getStringExtra("Description"));
                }
                if (getIntent().hasExtra("Category")){
                    Log.i("asdaasd", "entraaqui");
                    i.putExtra("Category", getIntent().getIntExtra("Category",0));
                }
                if (getIntent().hasExtra("id")){
                    i.putExtra("id", getIntent().getIntExtra("id",0));
                }
                startActivity(i);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        zone = Constants.zone;
        if(zone >= 0 && zone <= 9) {
            try {
                setMapBorders(zone);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!(getIntent().hasExtra("lat") && getIntent().hasExtra("lng"))) {
            if (gpsAvailable()) {
                //If current location is available show in map
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

                //Else show default marker in Barcelona
                if (lat == 0 && lng == 0) {
                    lat = 41.3828939;
                    lng = 2.1774322;
                    LatLng defaultPos = new LatLng(lat, lng);
                    marker = mMap.addMarker(new MarkerOptions().position(defaultPos).title("Barcelona"));
                    moveCamera(defaultPos);
                }
            } else {
                //If GPS unavailable show neighbourhood center
                LatLng center;

                switch (zone) {
                    case 0:
                        lat = 41.37496195;
                        lng = 2.17326530371345;
                        center = new LatLng(lat, lng);
                        marker = mMap.addMarker(new MarkerOptions().position(center).title("Ciutat Vella"));
                        moveCamera(center);
                        break;
                    case 1:
                        lat = 41.393351;
                        lng = 2.16597050963639;
                        center = new LatLng(lat, lng);
                        marker = mMap.addMarker(new MarkerOptions().position(center).title("Eixample"));
                        moveCamera(center);
                        break;
                    case 2:
                        lat = 41.35098635;
                        lng = 2.15256063238678;
                        center = new LatLng(lat, lng);
                        marker = mMap.addMarker(new MarkerOptions().position(center).title("Sants-Montjuic"));
                        moveCamera(center);
                        break;
                    case 3:
                        lat = 41.3884524;
                        lng = 2.12171825426451;
                        center = new LatLng(lat, lng);
                        marker = mMap.addMarker(new MarkerOptions().position(center).title("Les Corts"));
                        moveCamera(center);
                        break;
                    case 4:
                        lat = 41.393351;
                        lng = 2.16597050963639;
                        center = new LatLng(lat, lng);
                        marker = mMap.addMarker(new MarkerOptions().position(center).title("Sarrià-Sant Gervasi"));
                        moveCamera(center);
                        break;
                    case 5:
                        lat = 41.4101737;
                        lng = 2.15514217010802;
                        center = new LatLng(lat, lng);
                        marker = mMap.addMarker(new MarkerOptions().position(center).title("Gracia"));
                        moveCamera(center);
                        break;
                    case 6:
                        lat = 41.42853965;
                        lng = 2.14359654810671;
                        center = new LatLng(lat, lng);
                        marker = mMap.addMarker(new MarkerOptions().position(center).title("Horta-Guinardó"));
                        moveCamera(center);
                        break;
                    case 7:
                        lat = 41.44689075;
                        lng = 2.17256689935991;
                        center = new LatLng(lat, lng);
                        marker = mMap.addMarker(new MarkerOptions().position(center).title("Nou Barris"));
                        moveCamera(center);
                        break;
                    case 8:
                        lat = 41.43743905;
                        lng = 2.19685944900109;
                        center = new LatLng(lat, lng);
                        marker = mMap.addMarker(new MarkerOptions().position(center).title("San Andreu"));
                        moveCamera(center);
                        break;
                    case 9:
                        lat = 41.4067585;
                        lng = 2.20368795208359;
                        center = new LatLng(lat, lng);
                        marker = mMap.addMarker(new MarkerOptions().position(center).title("San Martí"));
                        moveCamera(center);
                        break;
                    default:
                        lat = 41.3828939;
                        lng = 2.1774322;
                        center = new LatLng(lat, lng);
                        marker = mMap.addMarker(new MarkerOptions().position(center).title("Barcelona"));
                        moveCamera(center);
                        break;

                }

            }
        }else{
            lat = getIntent().getDoubleExtra("lat",0);
            lng = getIntent().getDoubleExtra("lng",0);
            LatLng savedPos = new LatLng(lat,lng);
            marker = mMap.addMarker(new MarkerOptions().position(savedPos).title("Proposal's place"));
            moveCamera(savedPos);
        }


            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    lat = latLng.latitude;
                    lng = latLng.longitude;
                    marker.remove();
                    marker = mMap.addMarker(new MarkerOptions().position(latLng));
                    moveCamera(latLng);
                }
            });
    }


    /**
     * Moves Camera zu chosen position and zooms in
     * @param pos position marked
     */
    private void moveCamera (LatLng pos){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,13));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
    }

    /**
     * Checks if GPS is available for finding the users position
     * @return true if GPS enabled, else false
     */
    private Boolean gpsAvailable() {
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
        return gpsStatus;
    }

    /**
     * Reads JSON file including map borders and transfers data into a polyline shown in the map
     * @throws JSONException
     */
    public void setMapBorders(int zone) throws JSONException {
        try {
            InputStream is = new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
            };
            switch(zone){
                case 0:
                    is = getAssets().open("Ciutat_Vella.json");
                    break;
                case 1:
                    is = getAssets().open("Eixample.json");
                    break;
                case 2:
                    is = getAssets().open("Sants-Montjuic.json");
                    break;
                case 3:
                    is = getAssets().open("Les_Corts.json");
                    break;
                case 4:
                    is = getAssets().open("Sarrià-Sant_Gervasi.json");
                    break;
                case 5:
                    is = getAssets().open("Gracia.json");
                    break;
                case 6:
                    is = getAssets().open("Horta-Guinardó.json");
                    break;
                case 7:
                    is = getAssets().open("Nou_Barris.json");
                    break;
                case 8:
                    is = getAssets().open("San_Andreu.json");
                    break;
                case 9:
                    is = getAssets().open("San_Martí.json");
                    break;
            }

            Scanner sc = new Scanner(is);
            String data = new String();

            while (sc.hasNextLine()) {
                data += (sc.nextLine());
            }
            sc.close();

            JSONObject obj = new JSONObject(data);
            if(zone != 4){
                JSONArray coordinates = obj.getJSONArray("geometries").getJSONObject(0).getJSONArray("coordinates").getJSONArray(0).getJSONArray(0);
                List<LatLng> coord = new ArrayList<>(coordinates.length());
                for (int i = 0; i < coordinates.length(); i++) {
                    coord.add(new LatLng(coordinates.getJSONArray(i).getDouble(1), coordinates.getJSONArray(i).getDouble(0)));
                }
                pol = mMap.addPolyline(new PolylineOptions()
                        .addAll(coord)
                        .width(2).color(Color.RED));

            }else{
                for (int j = 0; j < 3; j++){
                    JSONArray coordinates = obj.getJSONArray("geometries").getJSONObject(0).getJSONArray("coordinates").getJSONArray(j).getJSONArray(0);
                    List<LatLng> coord = new ArrayList<>(coordinates.length());
                    for (int k = 0; k < coordinates.length(); k++) {
                        coord.add(new LatLng(coordinates.getJSONArray(k).getDouble(1), coordinates.getJSONArray(k).getDouble(0)));
                    }
                    pol = mMap.addPolyline(new PolylineOptions()
                            .addAll(coord)
                            .width(2).color(Color.RED));
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.setLat(location.getLatitude());
        this.setLng(location.getLongitude());
        marker.remove();
        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("Your location"));
        moveCamera(new LatLng(lat,lng));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
