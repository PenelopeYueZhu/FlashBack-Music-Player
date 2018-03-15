package tests;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;

import com.gaparmar.mediaflashback.UI.MainActivity;

import com.gaparmar.mediaflashback.Song;
import com.gaparmar.mediaflashback.DataStorage.StorageHandler;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by lxyzh on 2/18/2018.
 */

public class StorageHanderUnitTest {

    /*Context context;
    Song testSong;
    double delta = 0.00001;

    @Rule
    public ActivityTestRule<MainActivity> mainActivity =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setup(){
        context = mainActivity.getActivity().getBaseContext();

        testSong = new Song( "TestSongTitle", "ParentAlbumTitle",
                "ArtistName", 9000, 2018, 9080706, new double[]{10.0, 120.2});
    }

    @Test
    public void testLocation(){
        double[] location = new double[]{10.0, 120.2};
        StorageHandler.storeSongLocation(context, testSong.getResID(),location);
        assertEquals( location[0], StorageHandler.getSongLocation(context, testSong.getResID())[0], delta);
        assertEquals( location[1], StorageHandler.getSongLocation(context, testSong.getResID())[1], delta);
    }

    @Test
    public void testDay(){
        String day = "Sunday";
        StorageHandler.storeSongDay(context, testSong.getResID(), day);
        assertEquals(day, StorageHandler.getSongDay(context, testSong.getResID()));
    }

    @Test
    public void testTime() {
        int time = 13;
        StorageHandler.storeSongTime(context, testSong.getResID(), time);
        assertEquals(time, StorageHandler.getSongTime(context, testSong.getResID()));
    }

    @Test
    public void testSongState(){
        int state = 1;
        StorageHandler.storeSongState(context, testSong.getResID(), state);
        assertEquals(state, StorageHandler.getSongState(context, testSong.getResID()));
    }*/
}
