package tests;


import android.support.test.rule.ActivityTestRule;

import com.gaparmar.mediaflashback.Album;
import com.gaparmar.mediaflashback.MainActivity;
import com.gaparmar.mediaflashback.MusicPlayer;
import com.gaparmar.mediaflashback.MusicQueuer;
import com.gaparmar.mediaflashback.R;
import com.gaparmar.mediaflashback.Song;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JUnitMusicPlayerTest {
    MusicPlayer mp;
    int songOne = R.raw.back_east;
    int songTwo = R.raw.tightrope_walker;
    int songThree = R.raw.after_the_storm;
    int songFour = R.raw.america_religious;
    final Song s1 = new Song( "Back East", "I Will Not Be Afraid", "Unknown Artist",
            0, 0, songOne, new double[]{34, -117});
    final Song s2 = new Song( "Tightrope Walker", "I Will Not Be Afraid", "Unknown Artist",
            0, 0, songTwo,null);
    final Song s3 = new Song( "After the Storm", "I Will Not Be Afraid", "Unknown Artist",
            0, 0, songThree,null);
    final Song s4 = new Song( "America Religious", "I Will Not Be Afraid", "Unknown Artist",
            0, 0, songFour,null);

    private ArrayList<Integer> list;
    private Album a;

    @Rule
    public ActivityTestRule<MainActivity> mainActivity =
            new ActivityTestRule<MainActivity>(MainActivity.class);
    @Before
    public void setUp(){
        mp = MainActivity.getMusicPlayer();
        list.add(songOne);
        list.add(songTwo);
        list.add(songThree);
        list.add(songFour);

        a = new Album(list, "I Will Not Be Afraid");

    }

    @Test
    public void testLoadSong() {
        mp.loadNewSong(songOne);
        assertEquals(s1, mp.getCurrSong());
        assertEquals(songOne, mp.getCurrentSongId());
    }

    @Test
    public void testPlaySong() {
        mp.loadNewSong(songOne);
        mp.playSong();
        assertEquals(true, mp.isPlaying());
    }

    @Test
    public void testPauseSong() {
        mp.loadNewSong(songTwo);
        mp.pauseSong();
        assertEquals(false, mp.isPlaying());
    }

    @Test
    public void testAlbum() {
        mp.loadAlbum(a);
        mp.playSong();

        assertEquals("I Will Not Be Afraid", mp.getCurrSong().getParentAlbum());
    }

    @Test
    public void testNext() {
        mp.loadAlbum(a);
        mp.playSong();

        assertEquals(songOne, mp.getCurrSong());
        mp.nextSong();
        assertEquals(songTwo, mp.getCurrSong());
        mp.nextSong();
        assertEquals(songThree, mp.getCurrSong());
    }

    @Test
    public void testPrev() {
        mp.loadAlbum(a);
        mp.playSong();

        assertEquals(songOne, mp.getCurrSong());
        mp.nextSong();
        assertEquals(songTwo, mp.getCurrSong());
        mp.previousSong();
        assertEquals(songOne, mp.getCurrSong());
    }
}
