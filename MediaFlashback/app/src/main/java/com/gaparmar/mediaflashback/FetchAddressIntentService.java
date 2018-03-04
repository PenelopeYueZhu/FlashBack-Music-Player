package com.gaparmar.mediaflashback;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by lxyzh on 2/24/2018.
 */

public class FetchAddressIntentService extends IntentService{

    protected ResultReceiver mReceiver;

    /**
     * This default constructor that creates an IntentService.
     * Name is only to name that thread.
     */
    public FetchAddressIntentService(){
        super("UserLocation");
    }

    /**
     * Send the data we get from geocoder back to whoever called this intent
     * @param resultCode the int to indicate success or failure
     * @param message the data we get and tried to pass by
     */
    private void deliverResultToReceiver( int resultCode, String message ){
        Bundle bundle = new Bundle();
        bundle.putString( Constant.RESULT_DATA_KEY, message );
        Log.d("FA:deliverResultToReceiver", "receiver sending messages");
        mReceiver.send( resultCode, bundle);
    }
    /**
     * Override onHandleIntent() method from superclass and get the Geocoder to get the address
     */
    @Override
    protected void onHandleIntent(Intent intent){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        String errorMessage = "";

        // Get the location passed into the service through an extra
        Location location = intent.getParcelableExtra( Constant.LOCATION_DATA_EXTRA);
        mReceiver = intent.getParcelableExtra(Constant.RECEIVER);

        // The list for the addresses
        List<Address> addresses = null;

        // Try to get the address of the user
        try {
            addresses = geocoder.getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(),
                            Constant.MAX_ADDRESS_ACURACITY);
        }catch(IOException ioException) {
            // Catch the network or other I/O exception
            errorMessage = getString( R.string.service_not_available);
            Log.e("FA:Location Error", errorMessage + " Latitude = " + location.getLatitude()+
                                             ", Longitutde = " + location.getLongitude());
        }

        //Handle cases where no address is found
        if( addresses == null || addresses.size() == 0 ){
            if( errorMessage.isEmpty() ){
                errorMessage = getString(R.string.no_address_found);
                Log.e("FA:Location Error", errorMessage+ " Latitude = " + location.getLatitude()+
                        ", Longitutde = " + location.getLongitude());
            }
            deliverResultToReceiver(Constant.FAILURE_RESULT, errorMessage);
        }
        else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLien
            for( int i = 0 ; i <= address.getMaxAddressLineIndex(); i++ ){
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i("FA:onHandleIntent", getString(R.string.address_found));
            deliverResultToReceiver(Constant.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments));
        }
    }

}
