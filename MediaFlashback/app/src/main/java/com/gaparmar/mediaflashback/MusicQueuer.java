package com.gaparmar.mediaflashback;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by veronica.lin1218 on 2/12/2018.
 */

public class MusicQueuer {
    private HashMap<Song, Integer> allTracks = new HashMap();
    private HashMap<Album, Integer> allAlbums = new HashMap();
    public MusicQueuer() {

    }
    public void readSongs(Context context) {
        // "MediaFlashback/app/src/main/res/"
        String s = "~/Users/cheng/Desktop/CSE100/cse-110-team-project-team-31/MediaFlashback/app/src/main/res/raw";
        File f = new File(s);
        System.out.println("file path: " + s);
        if (f != null) {
            String[] allFiles = f.list();

            for (int i = 0; i < allFiles.length; i++) {
                System.out.println(allFiles[i]);
            }
        } else {
            System.out.println("file is null");
        }

    }



}
