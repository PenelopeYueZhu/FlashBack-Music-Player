package com.gaparmar.mediaflashback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

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
    Geocoder geocoder;
    List<Address> addressList;
    final Looper looper = null;

    public UserLocation(Context context){

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
        }catch(SecurityException e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("MissingPermission")
    public static double[] getLoc(){
        //
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        return new double[]{lat, lon};
    }

    public String getCity(double latitude, double longitude) {

        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressList.get(0).getLocality();

    }

    public String getState(double latitude, double longitude){
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressList.get(0).getAdminArea();

    }

}
