package com.gaparmar.mediaflashback;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by gauravparmar on 2/17/18.
 */

public class StorageHandler {

    public static final int LIKED = 1;
    public static final int DISLIKED = -1;
    public static final int NEUTRAL = 0;

    public static final int TIME_MORNING = 0;
    public static final int TIME_AFTERNOON = 1;
    public static final int TIME_NIGHT = 2;

    public static final int REGULAR = 0;
    public static final int FLASHBACK = 1;

    /**
     * Stores the Location of the Song as a Shared Preference
     * @param context The context of calling parent Activity
     * @param song_id The resource id of the song to be stored
     * @param location Double array containing the location coords
     */
    public static void storeSongLocation(Context context, int song_id, double [] location){
        System.out.println("Storing the song location songID\t"+song_id+
                "\tlocation:"+location[0]+"\t:"+location[1]);
        SharedPreferences sharedPreferences = context.getSharedPreferences("location",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(song_id+"_0", (float)location[0]);
        editor.putFloat(song_id+"_1", (float)location[1]);
        editor.apply();
    }

    /**
     * Gets the Location of the Song stored in a Shared Preference
     * @param context The context of calling parent Activity
     * @param song_id The resource id of the song to be retrieved
     */
    public static double[] getSongLocation(Context context, int song_id){
        SharedPreferences sharedPreferences = context.getSharedPreferences("location",
                MODE_PRIVATE);
        double[] latLong = new double[2];
        latLong[0] = sharedPreferences.getFloat(song_id+"_0", (float)-1.0);
        latLong[1] = sharedPreferences.getFloat(song_id+"_1", (float)-1.0);

        return latLong;
    }

    /**
     * Stores the Day when the Song was last played
     * @param context The context of calling parent Activity
     * @param song_id The resource id of the song to be stored
     * @param day The string representation of the day
     */
    public static void storeSongDay(Context context, int song_id, String day){
        System.out.println("Storing the song day information songID\t"+song_id+"\tday:\t"+day);
        SharedPreferences sharedPreferences = context.getSharedPreferences("day",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Integer.toString(song_id), day);
        editor.apply();
    }

    /**
     * Gets the Day when the Song was last played
     * @param context The context of calling parent Activity
     * @param song_id The resource id of the song to be stored
     */
    public static String getSongDay(Context context, int song_id){
        SharedPreferences sharedPreferences = context.getSharedPreferences("day",
                MODE_PRIVATE);
        return sharedPreferences.getString(Integer.toString(song_id), "");
    }


    /**
     * Stores the time when the song was last played
     * @param context The context of calling parent Activity
     * @param song_id The resource id of the song to be stored
     * @param time the time the song was last played
     */
    public static void storeSongTime(Context context, int song_id, int time){
        System.out.println("Storing the song time information. SongID:\t"+song_id
                +"\t time:\t"+time);
        SharedPreferences sharedPreferences = context.getSharedPreferences("time",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Integer.toString(song_id)/*+"_0"*/, time);
        editor.apply();
    }

    /**
     * Stores the time when the song was last played
     * @param context The context of calling parent Activity
     * @param song_id The resource id of the song to be stored
     */
    public static int getSongTime(Context context, int song_id){
        SharedPreferences sharedPreferences = context.getSharedPreferences("time",
                MODE_PRIVATE);
        return sharedPreferences.getInt(Integer.toString(song_id), 0);
    }

    /**
     * Stores the entire time stamp of when the song is lastly played
     * @param context the context of calling parent activity
     * @param song_id the resource id of the song
     * @param stamp the time stamp string
     */
    public static void storeSongBigTimeStamp(Context context, int song_id, String stamp){
        System.out.println("Storing the entire time stamp for the song. SongID:\t" +
        song_id + " \t timeStamp: \t" + stamp);
        SharedPreferences sharedPreferences = context.getSharedPreferences("bigTimeStamp",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Integer.toString(song_id), stamp);
        editor.apply();
    }

    /**
     * Get the stored big stamp stored for the song when it was last played
     * @param context the calling context (activity)
     * @param song_id the the song's resource id
     * @return the big time stamp that represents the time the song is lastly played
     */
    public static String getSongBigTimeStamp(Context context, int song_id){
        SharedPreferences sharedPreferences = context.getSharedPreferences("bigTimeStamp",
                MODE_PRIVATE);
        return sharedPreferences.getString(Integer.toString(song_id), "");
    }

    /**
     * Stores the time when the song was last played
     * @param context The context of calling parent Activity
     * @param song_id The resource id of the song to be stored
     * @param state the state of the song entered by the user
     */
    public static void storeSongState(Context context, int song_id, int state){
        System.out.println("Storing the song state information. SongID:\t"+song_id
                +"\t time:\t"+state);
        SharedPreferences sharedPreferences = context.getSharedPreferences("state",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (state){
            case LIKED:
                editor.putInt(Integer.toString(song_id), LIKED);
                break;
            case DISLIKED:
                editor.putInt(Integer.toString(song_id), DISLIKED);
                break;
            case NEUTRAL:
                editor.putInt(Integer.toString(song_id), NEUTRAL);
                break;
            default:
                editor.putInt(Integer.toString(song_id), NEUTRAL);
        }
        editor.apply();
    }

    /**
     * Stores the time when the song was last played
     * @param context The context of calling parent Activity
     * @param song_id The resource id of the song to be stored
     */
    public static int getSongState(Context context, int song_id){
        SharedPreferences sharedPreferences = context.getSharedPreferences("state",
                MODE_PRIVATE);
        return sharedPreferences.getInt(Integer.toString(song_id), 0);
    }


    /**
     * Stores the mode that the app was last in
     * @param context The context of calling parent Activity
     * @param mode the mode the app was last in
     */
    public static void storeLastMode(Context context, int mode){
        SharedPreferences sharedPreferences = context.getSharedPreferences("mode",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("currMode", mode);
        editor.apply();
    }

    /**
     * Gets the last mode that the song was in.
     * @param context
     * @return
     */
    public static int getLastMode(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("mode",
                MODE_PRIVATE);
        return sharedPreferences.getInt("currMode", 0);
    }


}
