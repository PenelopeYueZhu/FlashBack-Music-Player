package com.gaparmar.mediaflashback;

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
    public void readSongs() {
        File f = new File("/");
        String[] allFiles = f.list();

    }

    public void getID() {


    }

    public Album getAlbum(String albumName) {
        return Album;
    }



}
