//Project authors: Minquan Wang & Guanqing Hao

package musicPlayer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SongTest {
    Song song, song1, song2;

    /**
     * set up method for the musicPlayer.Song class
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        song = new Song("testSong.txt");
        song1 = new Song("GameOfThronesTheme.txt");
        System.out.println(System.getProperty("user.dir"));
    }

    /**
     * test the constructor for musicPlayer.Song class {@link musicPlayer.Song#Song(String)}
     */
    @Test
    public void testSong() {
        song2 = new Song("birthday.txt");
        assertTrue(song instanceof Song);
        assertTrue(song1 instanceof Song);
        assertTrue(song2 instanceof Song);
    }


    /**
     * test method for {@link musicPlayer.Song#toString()}
     */
    @Test
    public void testToString() {
        String songInfo = "Title: Test musicPlayer.Song\nArtist: Guanqing\nTotal duration: 6.0\nScientific pitch notation:";
        songInfo += "\n0.5 C 4 NATURAL false";
        songInfo += "\n0.5 D 4 SHARP false";
        songInfo += "\n0.5 G 4 NATURAL false";
        songInfo += "\n0.5 R false";
        songInfo += "\n0.5 C 5 NATURAL true";
        songInfo += "\n0.5 E 5 FLAT false";
        songInfo += "\n0.5 G 5 NATURAL false";
        songInfo += "\n0.5 C 6 NATURAL true";
        assertEquals(songInfo, song.toString());
    }

    /**
     * test method for {@link musicPlayer.Song#getTitle()}
     */
    @Test
    public void testGetTitle() {
        assertEquals("Test musicPlayer.Song", song.getTitle());
        assertEquals("Game of Thrones", song1.getTitle());
    }

    /**
     * test method for {@link musicPlayer.Song#getArtist()}
     */
    @Test
    public void testGetArtist() {
        assertEquals("Guanqing", song.getArtist());
        assertEquals("Ramin Djawadi", song1.getArtist());
    }

    /**
     * test method for {@link musicPlayer.Song#getTotalDuration()}
     */
    @Test
    public void testGetTotalDuration() {
        assertEquals(6, song.getTotalDuration(), 0.0);
        assertEquals(25.2, song1.getTotalDuration(), 0.0);
    }

    /**
     * test method for {@link musicPlayer.Song#octaveDown()}
     */
    @Test
    public void testOctaveDown() {
        boolean down1 = song.octaveDown();
        assertTrue(down1);

        String songInfo = "Title: Test musicPlayer.Song\nArtist: Guanqing\nTotal duration: 6.0\nScientific pitch notation:";
        songInfo += "\n0.5 C 3 NATURAL false";
        songInfo += "\n0.5 D 3 SHARP false";
        songInfo += "\n0.5 G 3 NATURAL false";
        songInfo += "\n0.5 R false";
        songInfo += "\n0.5 C 4 NATURAL true";
        songInfo += "\n0.5 E 4 FLAT false";
        songInfo += "\n0.5 G 4 NATURAL false";
        songInfo += "\n0.5 C 5 NATURAL true";
        assertEquals(songInfo, song.toString());

        boolean down2 = song.octaveDown();
        assertTrue(down2);
        boolean down3 = song.octaveDown();
        assertTrue(down3);

        //now the lowest octave is 1
        //octaveDown should do nothing and return false
        boolean down4 = song.octaveDown();
        assertFalse(down4);

        String songInfo2 = "Title: Test musicPlayer.Song\nArtist: Guanqing\nTotal duration: 6.0\nScientific pitch notation:";
        songInfo2 += "\n0.5 C 1 NATURAL false";
        songInfo2 += "\n0.5 D 1 SHARP false";
        songInfo2 += "\n0.5 G 1 NATURAL false";
        songInfo2 += "\n0.5 R false";
        songInfo2 += "\n0.5 C 2 NATURAL true";
        songInfo2 += "\n0.5 E 2 FLAT false";
        songInfo2 += "\n0.5 G 2 NATURAL false";
        songInfo2 += "\n0.5 C 3 NATURAL true";
        assertEquals(songInfo2, song.toString());
    }

    /**
     * test method for {@link musicPlayer.Song#octaveUp()}
     */
    @Test
    public void testOctaveUp() {
        boolean up1 = song.octaveUp();
        assertTrue(up1);
        String songInfo = "Title: Test musicPlayer.Song\nArtist: Guanqing\nTotal duration: 6.0\nScientific pitch notation:";
        songInfo += "\n0.5 C 5 NATURAL false";
        songInfo += "\n0.5 D 5 SHARP false";
        songInfo += "\n0.5 G 5 NATURAL false";
        songInfo += "\n0.5 R false";
        songInfo += "\n0.5 C 6 NATURAL true";
        songInfo += "\n0.5 E 6 FLAT false";
        songInfo += "\n0.5 G 6 NATURAL false";
        songInfo += "\n0.5 C 7 NATURAL true";
        assertEquals(songInfo, song.toString());

        boolean up2 = song.octaveUp();
        assertTrue(up2);
        boolean up3 = song.octaveUp();
        assertTrue(up3);
        boolean up4 = song.octaveUp();
        assertTrue(up4);

        //now the highest octave is 10
        //octaveUp should do nothing and return false
        boolean up5 = song.octaveUp();
        assertFalse(up5);

        String songInfo2 = "Title: Test musicPlayer.Song\nArtist: Guanqing\nTotal duration: 6.0\nScientific pitch notation:";
        songInfo2 += "\n0.5 C 8 NATURAL false";
        songInfo2 += "\n0.5 D 8 SHARP false";
        songInfo2 += "\n0.5 G 8 NATURAL false";
        songInfo2 += "\n0.5 R false";
        songInfo2 += "\n0.5 C 9 NATURAL true";
        songInfo2 += "\n0.5 E 9 FLAT false";
        songInfo2 += "\n0.5 G 9 NATURAL false";
        songInfo2 += "\n0.5 C 10 NATURAL true";
        assertEquals(songInfo2, song.toString());
    }

    /**
     * test method for {@link musicPlayer.Song#changeTempo(double)}
     */
    @Test
    public void testChangeTempo() {
        String songInfo = "Title: Test musicPlayer.Song\nArtist: Guanqing\nTotal duration: 12.0\nScientific pitch notation:";
        songInfo += "\n1.0 C 4 NATURAL false";
        songInfo += "\n1.0 D 4 SHARP false";
        songInfo += "\n1.0 G 4 NATURAL false";
        songInfo += "\n1.0 R false";
        songInfo += "\n1.0 C 5 NATURAL true";
        songInfo += "\n1.0 E 5 FLAT false";
        songInfo += "\n1.0 G 5 NATURAL false";
        songInfo += "\n1.0 C 6 NATURAL true";
        song.changeTempo(2);
        assertEquals(songInfo, song.toString());

    }

    /**
     * test method for {@link musicPlayer.Song#reverse()}
     */
    @Test
    public void testReverse() {
        String songInfo = "Title: Test musicPlayer.Song\nArtist: Guanqing\nTotal duration: 6.0\nScientific pitch notation:";
        songInfo += "\n0.5 C 6 NATURAL true";
        songInfo += "\n0.5 G 5 NATURAL false";
        songInfo += "\n0.5 E 5 FLAT false";
        songInfo += "\n0.5 C 5 NATURAL true";
        songInfo += "\n0.5 R false";
        songInfo += "\n0.5 G 4 NATURAL false";
        songInfo += "\n0.5 D 4 SHARP false";
        songInfo += "\n0.5 C 4 NATURAL false";

        song.reverse();
        assertEquals(songInfo, song.toString());
    }

}
