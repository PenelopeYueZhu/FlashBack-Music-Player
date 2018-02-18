package com.gaparmar.mediaflashback;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Gordee on 2/17/2018.
 */

public class UserLocation {
    public static double lat;
    public static double lon;
    public static Criteria gpsCrit;
    public static LocationListener mLocationListener;
    private static LocationManager mLocationManager;
    public static boolean hasPermission = true;
    static Geocoder geocoder;
    static List<Address> addressList;
    final Looper looper = null;
    private static Context context;
    private static Activity activity;

    public UserLocation(Context context) {

        this.context = context;
        geocoder = new Geocoder(context, Locale.getDefault());

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };

        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100,
                    0, mLocationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private static void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
        } else {

        }
    }
    public static double[] getLoc() {
        //

        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        }catch (SecurityException e){
            hasPermission = false;
        }
        return new double[]{lat, lon};
    }


    public static String getCity(double latitude, double longitude) {

        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressList.get(0).getLocality();

    }

    public static String getState(double latitude, double longitude){
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressList.get(0).getAdminArea();

    }

}
