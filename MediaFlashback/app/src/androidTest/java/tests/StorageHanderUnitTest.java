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

    Context context;
    Song testSong;
    double delta = 0.00001;

    @Rule
    public ActivityTestRule<MainActivity> mainActivity =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setup(){
        context = mainActivity.getActivity().getBaseContext();

        testSong = new Song( "filename" );
    }

    @Test
    public void testLocation(){
        StorageHandler.storeSongLocationString(context, testSong.getFileName(),"location");
        assertEquals( "location", StorageHandler.getSongLocationString(context, testSong.getFileName()));
    }

    @Test
    public void testDay(){
        String day = "Sunday";
        StorageHandler.storeSongDay(context, testSong.getFileName(), day);
        assertEquals(day, StorageHandler.getSongDay(context, testSong.getFileName()));
    }

    @Test
    public void testTime() {
        int time = 13;
        StorageHandler.storeSongTime(context, testSong.getFileName(), time);
        assertEquals(time, StorageHandler.getSongTime(context, testSong.getFileName()));
    }

    @Test
    public void testSongState(){
        int state = 1;
        StorageHandler.storeSongState(context, testSong.getFileName(), state);
        assertEquals(state, StorageHandler.getSongState(context, testSong.getFileName()));
    }
}
