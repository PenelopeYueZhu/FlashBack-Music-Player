package com.gaparmar.mediaflashback;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by gauravparmar on 2/15/18.
 */
public class AlbumUnitTest {
    private Song A, B;
    @Before
    public void initializeAlbums() throws Exception{
        A = new Song("song A", "Album A", "Artist A",
                300, 1990, 0);
        B = new Song("song B", "Album B", "Artist B",
                400, 1991, 1);

    }

    @Test
    public void testAlbums() throws Exception{
        Song [] songs = new Song[2];
        songs[0] = A;
        songs[1] = B;
        //Album test = new Album(songs);
        //assertEquals(test.hasSong(A), true);
        //assertEquals(test.hasSong(B), true);

    }
}
