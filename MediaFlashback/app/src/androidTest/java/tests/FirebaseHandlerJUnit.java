package tests;

/**
 * Created by lxyzh on 3/4/2018.
 */
import android.provider.ContactsContract;

import com.gaparmar.mediaflashback.FirebaseHandler;
import com.gaparmar.mediaflashback.Song;
import com.gaparmar.mediaflashback.SongString;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FirebaseHandlerJUnit{
    double delta = 0.00001;

    SongString songString1;
    SongString songString2;

    @Before
    public void setup() {
        songString1 = new SongString("10.0", "12.2", "909090");
        songString2 = new SongString("11.0", "13.3", "909099");

        FirebaseHandler.saveSong(songString1);
      //  FirebaseHandler.saveSong(songString2);
    }

    @Test
    public void testLocation(){
        assertEquals(10.0, FirebaseHandler.getLocation("909090")[0], delta);
        assertEquals(12.2, FirebaseHandler.getLocation("909090")[1], delta);
    }

}
