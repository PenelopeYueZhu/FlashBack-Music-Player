package tests;

/**
 * Created by Aaron on 2/17/2018.
 */

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.View;

import com.gaparmar.mediaflashback.Album;
import com.gaparmar.mediaflashback.FlashbackPlayer;
import com.gaparmar.mediaflashback.MainActivity;
import com.gaparmar.mediaflashback.MusicQueuer;
import com.gaparmar.mediaflashback.R;
import com.gaparmar.mediaflashback.Song;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FlashbackUnitTest {

    int songOne = R.raw.bleed;
    int songTwo = R.raw.tightrope_walker;
    int songThree = R.raw.after_the_storm;
    int songFour = R.raw.america_religious;
    final Song s1 = new Song( "Bleed", "I Will Not Be Afraid", "Unknown Artist",
            0, 0, songOne, new double[]{34, -117});
    final Song s2 = new Song( "Tightrope Walker", "I Will Not Be Afraid", "Unknown Artist",
            0, 0, songTwo,null);
    final Song s3 = new Song( "After the Storm", "I Will Not Be Afraid", "Unknown Artist",
            0, 0, songThree,null);
    final Song s4 = new Song( "America Religious", "I Will Not Be Afraid", "Unknown Artist",
            0, 0, songFour,null);

    private FlashbackPlayer flashbackPlayer;
    private List<Song> list;

    @Rule
    public ActivityTestRule<MainActivity> mainActivity =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp()
    {
        flashbackPlayer = new FlashbackPlayer(list, mainActivity.getActivity());
    }

    @Test
    public void testProbability()
    {
        s1.setProbability(1);
        s2.setProbability(2);
        s3.setProbability(3);
        s4.setProbability(4);

        list = new ArrayList<>();

        list.add(s1);
        list.add(s2);
        list.add(s3);
        list.add(s4);

        List<Song> newList = flashbackPlayer.getSongsToPlay();

        assertEquals(newList.get(0).getProbability(), list.get(3).getProbability());
        assertEquals(newList.get(1).getProbability(), list.get(2).getProbability());
        assertEquals(newList.get(2).getProbability(), list.get(1).getProbability());
        assertEquals(newList.get(1).getProbability(), list.get(0).getProbability());
    }

}
