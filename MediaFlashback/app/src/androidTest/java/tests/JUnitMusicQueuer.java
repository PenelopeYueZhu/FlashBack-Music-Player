package tests;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;

import com.gaparmar.mediaflashback.Album;
import com.gaparmar.mediaflashback.UI.MainActivity;
import com.gaparmar.mediaflashback.MusicQueuer;
import com.gaparmar.mediaflashback.Song;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by lxyzh on 2/14/2018.
 */

public class JUnitMusicQueuer {
/*
    MusicQueuer mq;
    final static int numOfSongs = 18;
    final static int numOfAlbums = 5;
    Context context;

    @Rule
    public ActivityTestRule<MainActivity> mainActivity =
                           new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp(){
        context = mainActivity.getActivity().getBaseContext();
        mq = new MusicQueuer(context);
        mq.readSongs();
        mq.readAlbums();
    }

    @Test
    public void testReadSongs(){
        // Check if we read everything
        assertEquals(  numOfSongs, mq.getNumSongs());
    }

    @Test
    public void testReadAlbum() {
        assertEquals(  numOfAlbums, mq.getNumAlbums());
    }

    @Test
    public void testGetter() {
        ArrayList<String> albumList = mq.getEntireAlbumList();
        assertEquals( numOfAlbums, albumList.size());

        ArrayList<Integer> songList = mq.getEntireSongList();
        assertEquals(  numOfSongs, songList.size());
    }

    @Test
    public void testSetter(){
        ArrayList<Integer> list = mq.getEntireSongList();

        assertEquals( numOfSongs, list.size() );

        // Check if each song is set correctly and get correctly
        for( int i = 0 ; i < list.size() ; i++ ){
            Song song = mq.getSong( list.get(i));

            assertEquals( list.get(i), (Integer)song.getResID());

            Album album = mq.getAlbum( song.getParentAlbum() );

            assertEquals( album.getAlbumTitle(), song.getParentAlbum());

            assertEquals( true, album.hasSong(song));
        }
    }*/
}
