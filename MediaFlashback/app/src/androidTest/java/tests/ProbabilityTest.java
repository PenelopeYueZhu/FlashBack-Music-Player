package tests;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.gaparmar.mediaflashback.Constant;
import com.gaparmar.mediaflashback.DataStorage.FirebaseHandler;
import com.gaparmar.mediaflashback.DataStorage.LogInstance;
import com.gaparmar.mediaflashback.DataStorage.StorageHandler;
import com.gaparmar.mediaflashback.MusicQueuer;
import com.gaparmar.mediaflashback.Song;
import com.gaparmar.mediaflashback.UI.MainActivity;
import com.gaparmar.mediaflashback.UI.VibeActivity;
import com.gaparmar.mediaflashback.WhereAndWhen.AddressRetriver;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;
import java.util.Collections;

/**
 * Created by aapte on 3/15/2018.
 */

public class ProbabilityTest {

    Context context;
    int currState = 1;
    String currTimeStamp = "234";
    String currDay = "Monday";
    double[] currLocation = new double[]{1.4,1.4};
    int currTime = 1;

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup()
    {
        context = mainActivity.getActivity().getBaseContext();
    }

    @Test
    public void testState()
    {
        MusicQueuer musicQueuer = new MusicQueuer(context);

        StorageHandler.storeSongState(context, "8 Gorgeous.mp3", 1);
        StorageHandler.storeSongBigTimeStamp(context, "8 Gorgeous.mp3", "234");
        StorageHandler.storeSongDay(context, "8 Gorgeous.mp3", "Monday");
        StorageHandler.storeSongLocation(context, "8 Gorgeous.mp3", new double[]{1.4, 1.4});
        StorageHandler.storeSongTime(context, "8 Gorgeous.mp3", 1);

        assert(getProbability("8 Gorgeous.mp3") == 3);

    }

    public int getProbability( String filename ) {
        int probability = 1;
        if(currTimeStamp.equals(StorageHandler.getSongBigTimeStamp(context, "8 Gorgeous.mp3")))
        {
            probability++;
        }
        if(currDay.equals(StorageHandler.getSongDay(context, "8 Gorgeous.mp3")))
        {
            probability++;
        }
        if(StorageHandler.getSongTime(context, "8 Gorgeous.mp3") == currTime)
        {
            probability++;
        }
        if(StorageHandler.getSongState(context, "8 Gorgeous.mp3") == 1)
        {
            probability++;
        }
        if(StorageHandler.getSongState(context, "8 Gorgeous.mp3") == -1)
        {
            probability = 0;
        }
        return probability;
    }
}
