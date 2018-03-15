package tests;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;

import com.gaparmar.mediaflashback.UI.MainActivity;
import com.gaparmar.mediaflashback.Song;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by gauravparmar on 2/15/18.
 */

public class SongUnitTest {
   /* Context context;
    @Rule
    public ActivityTestRule<MainActivity> mainActivity =
            new ActivityTestRule<MainActivity>(MainActivity.class);
    private Song A, B;

    @Before
    public void initializeSongs() throws Exception{
        context = mainActivity.getActivity().getBaseContext();

        A = new Song("song A", "Album A", "Artist A",
                300, 1990, 0,new double[]{0,0});
        B = new Song("song B", "Album B", "Artist B",
                400, 1991, 1,new double[]{0,0});
    }

    @Test
    public void testSongTitle() throws Exception{
        assertEquals(A.getTitle(), "song A");
        assertEquals(B.getTitle(), "song B");
        assertEquals(B.getTitle().equals("song A"), false);

    }

    @Test
    public void testSongArtist() throws Exception{
        assertEquals(A.getArtistName(), "Artist A");
        assertEquals(B.getArtistName(), "Artist B");
        assertEquals(B.getArtistName().equals("Artist A"), false);
        assertEquals(B.getArtistName().equals(""), false);
    }

    @Test
    public void testSongAlbum() throws Exception{
        assertEquals(A.getParentAlbum(), "Album A");
        A.setParentAlbum("Album B");
        assertEquals(B.getParentAlbum().equals("Album A"), false);
        assertEquals(A.getParentAlbum().equals("Album A"), false);
        assertEquals(B.getParentAlbum().equals("Album B"), true);
    }

    @Test
    public void testSongState() throws Exception{
        assertEquals(A.getCurrentState(), 0);
        assertEquals(B.getCurrentState(), 0);
        A.setState(1);
        assertEquals(A.getCurrentState(), 1);
    }*/


}
