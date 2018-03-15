package com.gaparmar.mediaflashback.WhereAndWhen;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;

import com.gaparmar.mediaflashback.Constant;
import com.gaparmar.mediaflashback.R;

/**
 * Created by lxyzh on 2/24/2018.
 */

public class AddressRetriver {

    /* Class AddressResultRecevier
     * The class that gets the result and gets that string
     */
    class AddressResultReceiver extends ResultReceiver implements Parcelable{
        String mAddressOutput;
        double[] coordinates = new double[2];
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
            coordinates[0] = resultData.getDouble(Constant.LAT_DATA_KEY);
            coordinates[1] = resultData.getDouble(Constant.LON_DATA_KEY);
            // Show a toast message if an address was found.
            if (resultCode == Constant.SUCCESS_RESULT) {
                //Log.d("ARR:onReceiveResult", context.getString(R.string.address_found));
            }
            else Log.d("ARR:onReceiveResult", context.getString(R.string.no_address_found));

        }

        /**
         * get the address string returned by IntentService
         * @return string represents the address
         */
        public String getAddress(){
            Log.d("ARR:getAddress", "Getting the Address " + mAddressOutput);
            return mAddressOutput;
        }

        /**
         * Get the latitude and Longitude by the intentService
         * @return double string to represent coordiantes
         */
        public double[] getLatLon(){
            return coordinates;
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
        mLastLocation = new Location("");

    }



    protected void startIntentService() {
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        Log.d("AR:startIntentService", "Putting extras");
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
        //startIntentService();
        //System.out.println(mResultReceiver.getLatLon()[0]);
        //return mResultReceiver.getLatLon();

        return new double[]{mLastLocation.getLatitude(), mLastLocation.getLongitude()};
    }


}
