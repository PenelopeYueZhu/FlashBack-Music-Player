package com.gaparmar.mediaflashback.DataStorage;

import android.content.Context;
import android.content.SharedPreferences;

import com.gaparmar.mediaflashback.Constant;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by gauravparmar on 2/17/18.
 */

public class StorageHandler {

    public static final int LIKED = 1;
    public static final int DISLIKED = -1;
    public static final int NEUTRAL = 0;

    /**
     *
     */
    public static void storeSongUrl(Context context, String fileName, String URL ){
        SharedPreferences sharedPreferences = context.getSharedPreferences("URL",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(fileName, URL);
        System.err.println("Storing the URL " + URL);
        editor.apply();
    }

    /**
     *
     */
    public static void storeSingleUseUrl(Context context, String fileName, String URL ){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SingleUseURL",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(fileName, URL);
        editor.apply();
    }

    /**
     *
     */
    public static String getSingleUseUrl( Context context, String fileName ){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SingleUseURL", MODE_PRIVATE);
        String address = sharedPreferences.getString(fileName, "");
        return address;
    }

    /**
     *
     */
    public static String getSongUrl( Context context, String fileName ){
        SharedPreferences sharedPreferences = context.getSharedPreferences("URL", MODE_PRIVATE);
            String address = sharedPreferences.getString(fileName, "");
            System.err.println("getting the url " + address);
            return address;
    }

    /**
     * Gets the location string of the song stored in a shared preference
     * @param context The context of calling parent Activity
     * @param fileName The resource id of the song to be retrieved
     * @param address The address string
     */
    public static void storeSongLocationString( Context context, String fileName, String address ){
        SharedPreferences sharedPreferences = context.getSharedPreferences("LocationString", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(fileName, address);
        editor.apply();
    }

    public static String getSongLocationString( Context context, String filePath){
        SharedPreferences sharedPreferences = context.getSharedPreferences("LocationString", MODE_PRIVATE);
        String address = sharedPreferences.getString(filePath, "");
        return address;
    }

    /**
     * Stores the Day when the Song was last played
     * @param context The context of calling parent Activity
     * @param fileName The name of the song to be stored
     * @param day The string representation of the day
     */
    public static void storeSongDay(Context context, String fileName, String day){
        System.out.println("Storing the song day information songID\t"+fileName+"\tday:\t"+day);
        SharedPreferences sharedPreferences = context.getSharedPreferences("day",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(fileName, day);
        editor.apply();
    }

    /**
     * Gets the Day when the Song was last played
     * @param context The context of calling parent Activity
     * @param fileName name of song
     */
    public static String getSongDay(Context context, String fileName){
        SharedPreferences sharedPreferences = context.getSharedPreferences("day",
                MODE_PRIVATE);
        return sharedPreferences.getString(fileName, "");
    }


    /**
     * Stores the time when the song was last played
     * @param context The context of calling parent Activity
     * @param fileName The resource id of the song to be stored
     * @param time the time the song was last played
     */
    public static void storeSongTime(Context context, String fileName, int time){
        System.out.println("Storing the song time information. SongID:\t"+fileName
                +"\t time:\t"+time);
        SharedPreferences sharedPreferences = context.getSharedPreferences("time",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(fileName/*+"_0"*/, time);
        editor.apply();
    }

    /**
     * Stores the time when the song was last played
     * @param context The context of calling parent Activity
     * @param fileName The file name of the song to be stored
     */
    public static int getSongTime(Context context, String fileName){
        SharedPreferences sharedPreferences = context.getSharedPreferences("time",
                MODE_PRIVATE);
        return sharedPreferences.getInt(fileName, 0);
    }

     /**
     * Stores the time when the song was last played
     * @param context The context of calling parent Activity
     * @param fileName The file name of the song to be stored
     * @param state the state of the song entered by the user
     */
    public static void storeSongState(Context context, String fileName, int state){
        System.out.println("Storing the song state information. SongID:\t"+fileName
                +"\t time:\t"+state);
        SharedPreferences sharedPreferences = context.getSharedPreferences("state",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (state){
            case LIKED:
                editor.putInt(fileName, LIKED);
                break;
            case DISLIKED:
                editor.putInt(fileName, DISLIKED);
                break;
            case NEUTRAL:
                editor.putInt(fileName, NEUTRAL);
                break;
            default:
                editor.putInt(fileName, NEUTRAL);
        }
        editor.apply();
    }

    /**
     * Stores the time when the song was last played
     * @param context The context of calling parent Activity
     * @param fileName The resource id of the song to be stored
     */
    public static int getSongState(Context context, String fileName){
        SharedPreferences sharedPreferences = context.getSharedPreferences("state",
                MODE_PRIVATE);
        return sharedPreferences.getInt(fileName, 0);
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

    /**
     * Stores the last user that played this song
     * @param context The context of calling parent Activity
     * @param user the user that played the song last
     */
    public static void storeLastUser(Context context, String filename, String user){
        SharedPreferences sharedPreferences = context.getSharedPreferences("last_user",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(filename, user);
        editor.apply();
    }

    /**
     * Gets the last user that played this song
     * @param context
     * @return
     */
    public static String getLastUser(Context context, String filename){
        SharedPreferences sharedPreferences = context.getSharedPreferences("last_user",
                MODE_PRIVATE);
        return sharedPreferences.getString(filename, "");
    }
}
