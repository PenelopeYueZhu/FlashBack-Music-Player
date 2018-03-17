package tests;

import com.gaparmar.mediaflashback.Album;
import com.gaparmar.mediaflashback.Song;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by gauravparmar on 2/15/18.
 */
public class    AlbumUnitTest {
    Album testAlbum;
    Song song1;
    Song song2;
    Song song3;
    String title = "T";
    String artist = "T";
    String album = "T";
    String year = "1234";
    @Before
    public void initializeAlbums() throws Exception{
        song1 = new Song("song A");
        song1.setMetadata( title, artist, album, year);
        song2 = new Song("song B");
        song2.setMetadata(title, artist, album, year);
        song3 = new Song( "song C");
        song3.setMetadata(title, album, artist, year);
        ArrayList<String> songlist = new ArrayList<>();
        songlist.add(song1.getFileName());
        songlist.add(song2.getFileName());
        testAlbum = new Album(songlist, "T");
    }

    @Test
    public void testAlbumContainingSong() {
        assertEquals(true, testAlbum.hasSong(song1));
        assertEquals(false, testAlbum.hasSong(song3));
    }

    @Test
    public void testAlbumAddSong() {
        testAlbum.addSong(song3);
        assertEquals(true, testAlbum.hasSong(song3));
    }

}
