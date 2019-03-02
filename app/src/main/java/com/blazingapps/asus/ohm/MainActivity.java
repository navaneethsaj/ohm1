package com.blazingapps.asus.ohm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements PermissionsListener {
    MapboxMap mapboxmap;
    MapView mapView;
    String searchURL = "https://us-central1-ohm-sih-2019.cloudfunctions.net/getpump";
    JSONObject nearByPumpsJSON;
    JSONArray nearbyPumpList;
    private PermissionsManager permissionsManager;
    //TODO:
    private LocationComponent locationComponent;
    Style style;
    List<DirectionsRoute> directionsRoute;
    private String sdkKey="pk.eyJ1IjoibmF2YW5lZXRoc2FqIiwiYSI6ImNqc2ttMjQ2MjJsczE0M3RiZHlmb2IweHIifQ.GNxmx2b8Pq1Edb2b_QsAYA";
    FloatingActionButton startNavFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(getApplicationContext(),sdkKey);

        setContentView(R.layout.activity_main);
        startNavFab = findViewById(R.id.fabStartNAv);
        startNavFab.setEnabled(false);
        startNavFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    @SuppressLint("MissingPermission") double originlatitude = locationComponent.getLastKnownLocation().getLatitude();
                    @SuppressLint("MissingPermission") double originlongitude = locationComponent.getLastKnownLocation().getLongitude();
                    Point origin = Point.fromLngLat(originlongitude,originlatitude);
                    double destlatitude = nearbyPumpList.getJSONObject(0).getDouble("latitude");
                    double destlongitude = nearbyPumpList.getJSONObject(0).getDouble("longitude");
                    Point destination = Point.fromLngLat(destlongitude,destlatitude);
                    startNav(origin,destination);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {

                MainActivity.this.mapboxmap = mapboxMap;
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        MainActivity.this.style = style;
                        enableLocationComponent(style);
                        new SearchAsyncTask().execute(searchURL);

                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                    }
                });
            }
        });
    }

    public void markPumps(String name, double wait, double rate, double latitude, double longitude){
        mapboxmap.addMarker(new MarkerOptions()
                .title(name)
                .snippet(String.valueOf(rate)+"\n"+String.valueOf(wait))
                .position(new LatLng(latitude, longitude)));
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Activate the MapboxMap LocationComponent to show user location
            // Adding in LocationComponentOptions is also an optional parameter
            locationComponent = mapboxmap.getLocationComponent();
            locationComponent.activateLocationComponent(this, loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "Permission Needed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxmap.getStyle());
        } else {
            Toast.makeText(this, "Permission Not granted", Toast.LENGTH_LONG).show();
        }
    }


    class SearchAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("MissingPermission")
        @Override
        protected String doInBackground(String... strings) {
            MediaType JSON
                    = MediaType.parse("application/json; charset=utf-8");

            OkHttpClient client = new OkHttpClient();
            JSONObject reqobj = new JSONObject();
            try {
                reqobj.put("carid","carid");
                reqobj.put("latitude", locationComponent.getLastKnownLocation().getLatitude());
                reqobj.put("longitude", locationComponent.getLastKnownLocation().getLongitude());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, reqobj.toString());
            Request request = new Request.Builder()
                    .url(strings[0])
                    .post(body)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null){
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                try {
                    //TODO: SORT HERE
                    startNavFab.setEnabled(true);
                    nearByPumpsJSON = new JSONObject(s);
                    JSONArray pumps = nearByPumpsJSON.getJSONArray("pumps");
                    nearbyPumpList = pumps;
                     for (int i=0; i<pumps.length(); ++i){
                        markPumps(
                                pumps.getJSONObject(i).getString("name"),
                                pumps.getJSONObject(i).getDouble("wait"),
                                pumps.getJSONObject(i).getDouble("rate"),
                                pumps.getJSONObject(i).getDouble("latitude"),
                                pumps.getJSONObject(i).getDouble("longitude"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void startNav(Point origin,Point destination){

        NavigationRoute.builder(getApplicationContext())
                .accessToken(sdkKey)
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, retrofit2.Response<DirectionsResponse> response) {
                        DirectionsResponse directionsResponse = response.body();
                        if (directionsResponse !=null) {
                            directionsRoute = directionsResponse.routes();
                            NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                    .directionsRoute(directionsRoute.get(0))
                                    .shouldSimulateRoute(true)
                                    .build();

                            NavigationLauncher.startNavigation(MainActivity.this, options);
                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    //    onResume();
//    onPause();
//    onStop();
//    onSaveInstanceState();
//    onLowMemory();
//    onDestroy();
    public void gotoallpumps(View v){
        //startActivity(new Intent(getApplicationContext(),ListPumps.class));
        Intent intent = new Intent(getApplicationContext(),ListPumps.class);
        Bundle bundle = new Bundle();
        bundle.putString("nearby",nearByPumpsJSON.toString());

        intent.putExtras(bundle);
        startActivity(intent);

    }
}