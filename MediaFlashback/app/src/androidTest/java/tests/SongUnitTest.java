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

    Song song = new Song("testSong");
    @Test
    public void testIsInRnage() {
        // In range
        double[] location1 = new double[] {10.0001, 10.0001};
        double[] location2 = new double[] {10.0002, 10.0002};
        assertEquals(true, song.isInRange(location1, location2));

        // Not in range
        double[] location3 = new double[] {13.0, 13.0};

        assertEquals( false, song.isInRange(location1, location3));
    }

    @Test
    public void testStripMP3(){
        String tooManyMP3 = "thisisatest.mp3.mp3.mp3";
        assertEquals("thisisatest", song.stripMP3(tooManyMP3));
    }

    @Test
    public void testReformat() {
        String special = "thisisatest.mp3";
        assertEquals("thisisatest mp3", song.reformatFileName(special));
    }
}
