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
 * This class is used to store and retrieve information about
 * the users location.
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

    /**
     * Constructs an instance of a UserLocation
     * Creates a locationListener that gets the users location
     * @param context The context that the userLocation is created in
     */
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

        // Requests the location from the user's phone
        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100,
                    0, mLocationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the GPS coordinates of the user
     * @return A double representing [latitude, longitude], or -1, -1 if
     *         the user does not have locations enabled.
     */
    public static double[] getLoc() {

        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        }catch (SecurityException e){
            return new double[]{-1.0, -1.0};
        }
        return new double[]{lat, lon};
    }


    /**
     * Returns the city that is located at the given location
     * @param latitude The latitude to check for
     * @param longitude The longitude to check for
     * @return The city name located at [latitude, longitude]
     */
    public static String getCity(double latitude, double longitude) {
        
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressList.get(0).getLocality();

    }

    /**
     * Gets the state of the given GPS coordinates
     * @param latitude The latitude to check for
     * @param longitude The longitude to check for
     * @return The state located at [latitude, longitude]
     */
    public static String getState(double latitude, double longitude){
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressList.get(0).getAdminArea();

    }

}
