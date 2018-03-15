package tests;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;

import com.gaparmar.mediaflashback.DataStorage.StorageHandler;
import com.gaparmar.mediaflashback.UI.MainActivity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by aapte on 3/15/2018.
 */

public class StateTest {

    Context context;

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
        StorageHandler.storeSongState(context, "8 Gorgeous.mp3", 1);
        assertEquals(StorageHandler.getSongState(context, "8 Gorgeous.mp3"), 1);

        assertNotEquals(StorageHandler.getSongState(context, "8 Gorgeous.mp3"), 0);
    }
}
