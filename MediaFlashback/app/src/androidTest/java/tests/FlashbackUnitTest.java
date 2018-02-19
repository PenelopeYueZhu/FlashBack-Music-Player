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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FlashbackUnitTest {


    int songOne = R.raw.tightrope_walker;
    int songTwo = R.raw.tightrope_walker;
    int songThree = R.raw.tightrope_walker;
    int songFour = R.raw.tightrope_walker;
    final Song s1 = new Song( "Back East", "I Will Not Be Afraid", "Unknown Artist",
            0, 0, songOne, new double[]{34, -117});
    final Song s2 = new Song( "Tightrope Walker", "I Will Not Be Afraid", "Unknown Artist",
            0, 0, songTwo,null);
    final Song s3 = new Song( "After the Storm", "I Will Not Be Afraid", "Unknown Artist",
            0, 0, songThree,null);
    final Song s4 = new Song( "America Religious", "I Will Not Be Afraid", "Unknown Artist",
            0, 0, songFour,null);

    private List<Song> list;

    private static class SongCompare implements Comparator<Song> {
        public int compare(Song s1, Song s2)
        {
            return s2.getProbability() - s1.getProbability();
        }
    }

    public void makeFlashbackPlaylist(List<Song> list)
    {
        Collections.sort(list, new SongCompare());
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

        List<Song> newList = new ArrayList<>(list);
        makeFlashbackPlaylist(newList);

        assertEquals(newList.get(0).getProbability(), list.get(3).getProbability());
        assertEquals(newList.get(1).getProbability(), list.get(2).getProbability());
        assertEquals(newList.get(2).getProbability(), list.get(1).getProbability());
        assertEquals(newList.get(3).getProbability(), list.get(0).getProbability());
    }

    @Test
    public void mockProbabilityTest()
    {
        Context mockContext = null;
        double[] currLocation = new double[] {1000, 1000};
        int dayOfWeek = Calendar.SUNDAY;
        int timeLastPlayed = 0;
        Song.state s = Song.state.LIKED;

        s1.setCurrDay("Sunday");
        s1.setCurrLocation(currLocation);
        s1.setCurrTime(0);

        s1.setLocation(new double[]{1000,1000},mockContext);

        s1.setCurrentState(1);
        s1.setTimeLastPlayed(0);
        s1.setDayOfWeek("Sunday");

        s1.updateProbability(new double[]{1000, 1000},mockContext);

        assertEquals(s1.getProbability(), 5);

        s1.setLocation(new double[]{1000.001, 1000.01}, mockContext);

        s1.updateProbability(s1.getLocation(mockContext), mockContext);

        assertEquals(s1.getProbability(), 5);

        s1.setCurrentState(-1);

        s1.updateProbability(s1.getLocation(mockContext), mockContext);

        assertEquals(s1.getProbability(), 0);

        s1.setCurrentState(0);

        s1.updateProbability(s1.getLocation(mockContext), mockContext);

        assertEquals(s1.getProbability(), 4);
    }


}
