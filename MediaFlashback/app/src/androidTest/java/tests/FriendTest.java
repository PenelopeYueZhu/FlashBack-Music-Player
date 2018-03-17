package tests;

import android.support.test.rule.ActivityTestRule;

import com.gaparmar.mediaflashback.Friend;
import com.gaparmar.mediaflashback.UI.MainActivity;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.gaparmar.mediaflashback.Album;
import com.gaparmar.mediaflashback.UI.MainActivity;
import com.gaparmar.mediaflashback.MusicQueuer;
import com.gaparmar.mediaflashback.Song;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static com.gaparmar.mediaflashback.UI.MainActivity.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by aapte on 3/15/2018.
 */

public class FriendTest {

    @Test
    public void testEquals()
    {
        Friend friend1 = new Friend("John Doe", "123", "JhnDe123");
        Friend friend2 = new Friend("John Doe", "123", "JhnDe123");

        assertTrue(friend1.equals(friend2));

        friend1 = new Friend("John Doe", "1234", "JhnDe123");

        assertFalse(friend1.equals(friend2));

        friend2 = new Friend("Jane Doe", "1234", "JnD125");

        assertTrue(friend1.equals(friend2));
    }

}
