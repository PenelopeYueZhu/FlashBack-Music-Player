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
        editor.putFloat(song_id+"_0", (float) location[0]);
        editor.putFloat(song_id+"_1", (float)location[1]);
        editor.apply();
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
        editor.putInt(song_id+"_0", time);
        editor.apply();
    }


    /**
     * Stores the time when the song was last played
     * @param context The context of calling parent Activity
     * @param song_id The resource id of the song to be stored
     * @param state the state of the song entered by the user
     */
    public static void storeSongState(Context context, int song_id, Song.state state){
        System.out.println("Storing the song state information. SongID:\t"+song_id
                +"\t time:\t"+state.toString());
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
            case NEITHER:
                editor.putInt(Integer.toString(song_id), NEUTRAL);
                break;
            default:
                editor.putInt(Integer.toString(song_id), NEUTRAL);
        }
        editor.apply();
    }
}
