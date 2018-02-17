package com.gaparmar.mediaflashback;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Gordee on 2/17/2018.
 */

public class UserLocation {
    public static double lat;
    public static double lon;
    private static LocationManager mLocationManager;


    public static LocationManager locationUpdate(Context context) {

         mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
         try {
         mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100,
                0, mLocationListener);
        }catch(SecurityException e) {
         e.printStackTrace();
         }

         return mLocationManager;
    }

    public static double[] getLoc(){
        return new double[]{lat, lon};
    }



    private static LocationListener mLocationListener = new LocationListener() {
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
}
