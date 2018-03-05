package com.gaparmar.mediaflashback;

/**
 * Created by lxyzh on 2/24/2018.
 */

public final class Constant {

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;

    // For getting stuff from local folders
    public static final String PACKAGE_NAME =
            "com.gaparmar.mediaflashback";
    public static final String URI_PREFIX = "android.resource://com.gaparmar.mediaflashback/raw/";

    // For FirebaseHandler
    public static final String UNKNOWN = "Unknown";
    public static final int UNKNOWN_INT = 0;
    public static final String ADDRESS_FIELD = "address";
    public static final String USER_FIELD = "userName";
    public static final String WEEKDAY_FIELD = "dayOfWeek";
    public static final String TIME_FIELD = "time";
    public static final String COORD_FIELD = "location";
    public static final String STAMP_FIELD = "timeStamp";
    public static final String RATE_FIELD = "rate";
    public static final int LIKED = 1;
    public static final int NEUTRAL = 0;
    public static final int DISPLIKED = -1;

    // For Probability calculation
    public static final int MORNING_DIVIDER = 5;
    public static final int NOON_DIVIVER = 11;
    public static final int EVENING_DIVIDER = 17;
    public static final String MORNING = "Morning";
    public static final String AFTERNOON = "Afternoon";
    public static final String EVENING = "Evening";

    // For locatoin functions
    public static final int MAX_ADDRESS_ACURACITY = 1;
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";


}
