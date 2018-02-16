package com.gaparmar.mediaflashback;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
/**
 * Created by gauravparmar on 2/15/18.
 */
public class AlbumUnitTest {
    private Song A, B;
    private Album X;
    @Before
    public void initializeAlbums() throws Exception{
        A = new Song("song A", "Album A", "Artist A",
                300, 1990, 0);
        B = new Song("song B", "Album B", "Artist B",
                400, 1991, 1);
        X = new Album("Album X");
    }

    @Test
    public void testAlbumSong() throws Exception{
        assertEquals(X.hasSong(A), false);
        X.addSong(A);
        assertEquals(X.hasSong(A), true);
    }
}
