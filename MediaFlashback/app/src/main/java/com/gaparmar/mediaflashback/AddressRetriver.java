package com.gaparmar.mediaflashback;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * Created by lxyzh on 2/24/2018.
 */

public class AddressRetriver {

    /* Class AddressResultRecevier
     * The class that gets the result and gets that string
     */
    class AddressResultReceiver extends ResultReceiver implements Parcelable{
        String mAddressOutput;
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Override the function to receive the info intent service returns
         * @param resultCode the code to indicate if it is successful service
         * @param resultData the data intent returns
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constant.RESULT_DATA_KEY);

            // Show a toast message if an address was found.
            if (resultCode == Constant.SUCCESS_RESULT) {
                //Log.d("ARR:onReceiveResult", context.getString(R.string.address_found));
            }

        }

        /**
         * get the address string returned by IntentService
         * @return string represents the address
         */
        public String getAddress(){
            return mAddressOutput;
        }
    }

    Context context;

    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;

    /**
     * Constructor that gets the context that calls this function
     * @param context the context that calls this class
     */
    public AddressRetriver( Context context, Handler handler ){
        mResultReceiver = new AddressResultReceiver(handler);
        this.context = context;

    }



    protected void startIntentService() {
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        Log.d("AR:startIntentService", "PUtting extras");
        intent.putExtra(Constant.RECEIVER, mResultReceiver);
        intent.putExtra(Constant.LOCATION_DATA_EXTRA, mLastLocation);
        context.startService(intent);
    }

    /**
     * Returns the location address string
     * @return string of the address
     */
    public String getAddress() {
        startIntentService();
        return mResultReceiver.getAddress();
    }

    /**
     * SEt the location read from user
     * @param location the current Location of user
     */
    public void setLocation(Location location ){
        mLastLocation = location;
    }


    /**
     * Get the double[] of the location
     * @return double of the location
     */
    public double[] getLatLon() {
        double[] loc = new double[2];
        loc[0] = mLastLocation.getLatitude();
        loc[1] = mLastLocation.getLongitude();
        return loc;
    }


}
