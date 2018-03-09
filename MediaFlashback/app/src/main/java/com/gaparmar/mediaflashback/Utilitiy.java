package com.gaparmar.mediaflashback;

/**
 * Created by gauravparmar on 3/8/18.
 * A Utility class to hold all the general helper functions
 */
public class Utilitiy {

    /**
     * Computes the distance between two geographical points
     * @param l1 Double array representing point A on Earth
     * @param l2 Double array representing point B on Earth
     * @return
     */
    public static double getDistance(double[] l1, double[] l2){


        double userLatR = toRadians(l1[0]);
        double storedLatR = toRadians(l1[1]);

        double deltaLat = toRadians(l2[0]-l1[0]);
        double deltaLon = toRadians(l2[0]-l1[0]);

        // Raidus of earth in feet
        double radius = 3959 * 5280;

        // a = sin^2(deltaLat/2) + cos(lat1R) * cos(lat2R) * sin^2(deltaLon/2)
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(userLatR) * Math.cos(storedLatR) * Math.sin(deltaLon / 2)
                * Math.sin(deltaLon / 2);

        // c = 2 * atan2(sqrt(a) * sqrt(1-a))
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt((1-a)));

        // d = radius * c
        return radius*c;
    }


    /**
     * helper function that converts angle in degrees to radians
     * @param degrees the degree we are trying to convert
     * @return the corresponding angle in radians
     */
    public static double toRadians(double degrees){
        return degrees * Math.PI / 180;
    }
}
