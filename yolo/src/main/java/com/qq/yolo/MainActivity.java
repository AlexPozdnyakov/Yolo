package com.qq.yolo;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends FragmentActivity {
    LocationManager locationManager;


    LocationListener locationListener;
    private SupportMapFragment fragment;
    private GoogleMap map;
    Singleton sender = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);

        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
        map = fragment.getMap();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        sendBroadcast(intent);

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);


    }

    public void sendData(String params) throws Exception {
        sender.login("q","q");
        sender.sendData(params);
    }

    @Override
    protected void onPause() {
        //Disable GPS
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", false);
        sendBroadcast(intent);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        sendBroadcast(intent);
        if (map == null) {
            map = fragment.getMap();
            map.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
        }
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            JSONObject coords = new JSONObject();
            JSONObject jsonwhen = new JSONObject();
            try{
                jsonwhen.put("when", getDatetime());
                coords.put("accuracy", loc.getAccuracy());
                coords.put("latitude", loc.getLatitude());
                coords.put("longitude", loc.getLongitude());
            }
            catch (Exception e){
            }
            try {
                sendData(jsonwhen.toString() + coords.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
         }

    private String getDatetime(){
        DateFormat df = new SimpleDateFormat("dd.mm.yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }
    public void onClickOn(View view) {
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        Button off = (Button)findViewById(R.id.off);
        Button on = (Button)findViewById(R.id.on);
        on.setClickable(false);
        off.setClickable(true);
    }
    public void onClickOff(View view){
        map.setMapType(GoogleMap.MAP_TYPE_NONE);
        Button off = (Button)findViewById(R.id.off);
        Button on = (Button)findViewById(R.id.on);
        on.setClickable(true);
        off.setClickable(false);
    }

//    public void changeServer(){
//        EditText et = (EditText)findViewById(R.id.et);
//        String x = et.getText().toString();
//        x = "http://" + x + "/api/device/";
//        sender.setUrl(x);
//    }

    public void sendYolo(View view){
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        double accuracy = location.getAccuracy();
        JSONObject coords = new JSONObject();
        JSONObject jsonwhen = new JSONObject();

        try{
            jsonwhen.put("when", getDatetime());
            coords.put("accuracy", accuracy);
            coords.put("latitude", latitude);
            coords.put("longitude", longitude);
            jsonwhen.put("coords", coords.toString());
            sendData(jsonwhen.toString());
        }
        catch (Exception e){
        }
        LatLng ll = new LatLng(latitude,longitude);
        map.clear();
        map.addMarker(new MarkerOptions().position(ll));
    }




}
