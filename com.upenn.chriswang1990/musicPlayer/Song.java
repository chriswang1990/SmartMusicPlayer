//Project authors: Minquan Wang & Guanqing Hao

package musicPlayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Song {

    private String title;
    private String artist;
    private int numberofNotes;
    private Note[] notes;
    private double totalDuration;

    /**
     * read from file and get all the information
     *
     * @param filename file name in directory
     */
    public Song(String filename) {

        File file = new File(filename);
        try {
            Scanner sc = new Scanner(file);
            this.title = sc.nextLine();
            this.artist = sc.nextLine();
            String dur = sc.nextLine();
            this.numberofNotes = Integer.parseInt(dur);
            notes = new Note[numberofNotes];

            int i = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                line = line.trim();
                String[] noteStr = line.split(" ");
                if (noteStr.length == 3) {
                    double duration = Double.parseDouble(noteStr[0]);
                    boolean repeat = Boolean.parseBoolean(noteStr[2]);
                    Note note = new Note(duration, repeat);
                    notes[i] = note;
                    i++;
                } else {
                    double duration = Double.parseDouble(noteStr[0]);
                    Pitch pitch = Pitch.getValueOf(noteStr[1]);
                    int octave = Integer.parseInt(noteStr[2]);
                    Accidental accidental = Accidental.getValueOf(noteStr[3]);
                    boolean repeat = Boolean.parseBoolean(noteStr[4]);

                    Note note = new Note(duration, pitch, octave, accidental, repeat);
                    notes[i] = note;
                    i++;
                }//end if-else
            }//end while
            sc.close();
        }//end try
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * get the title of the song
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * get the artist name
     *
     * @return artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * calculate and return the total duration of the song
     *
     * @return double totalDuration
     */
    public double getTotalDuration() {
        this.totalDuration = 0;
        boolean repeatSection = false;
        for (Note note : notes) {
            if (note.isRepeat()) {
                repeatSection = !repeatSection;
                totalDuration += 2 * note.getDuration();
            } else if (repeatSection) {
                this.totalDuration += 2 * note.getDuration();
            } else {
                this.totalDuration += note.getDuration();
            }
        }
        return totalDuration;
    }

    /**
     * play the song with the repeatable section
     */
    public void play() {
        boolean repeatSection = false;
        ArrayList<Note> repeatNotes = new ArrayList<>();
        for (Note note : notes) {
            if (note.isRepeat() && !repeatSection) {
                note.play();
                repeatNotes.add(note);
                repeatSection = true;
            } else if (note.isRepeat() && repeatSection) {
                note.play();
                repeatNotes.add(note);
                for (Note repeatNote : repeatNotes) {
                    repeatNote.play();
                }
                repeatSection = false;
                repeatNotes.clear();
            } else if (repeatSection) {
                note.play();
                repeatNotes.add(note);
            } else {
                note.play();
            }
        }
    }

    /**
     * decrease all octaves by 1
     * return false if reaches the lower limit (1)
     *
     * @return boolean
     */
    public boolean octaveDown() {
        for (Note i : notes) {
            if (!i.isRest()) {
                int octave = i.getOctave();
                if (octave == 1) {
                    return false;
                }
            }
        }
        for (Note i : notes) {
            if (!i.isRest()) {
                int octave = i.getOctave();
                i.setOctave(octave - 1);
            }
        }
        return true;
    }

    /**
     * increase all octaves by 1
     * return false if reaches the upper limit (10)
     *
     * @return boolean
     */
    public boolean octaveUp() {
        for (Note i : notes) {
            if (!i.isRest()) {
                int octave = i.getOctave();
                if (octave == 10) {
                    return false;
                }
            }
        }
        for (Note i : notes) {
            if (!i.isRest()) {
                int octave = i.getOctave();
                i.setOctave(octave + 1);
            }
        }
        return true;
    }

    /**
     * change the tempo of the song with given ratio
     *
     * @param ratio the ratio to multiply on current note duration to change play speed
     */
    public void changeTempo(double ratio) {
        for (Note i : notes) {
            double dur = i.getDuration();
            i.setDuration(dur * ratio);
        }
    }

    /**
     * reverse the song by exchanging notes
     */
    public void reverse() {
        Note note;
        int len = notes.length;
        for (int i = 0; i < len / 2; i++) {
            note = notes[i];
            notes[i] = notes[len - 1 - i];
            notes[len - 1 - i] = note;
        }
    }

    /**
     * Override the toString method for the musicPlayer.Song class for debugging and for the purposes of also being able to write some kind of unit test
     */
    @Override
    public String toString() {
        String songInfo = "Title: " + this.title;
        songInfo += "\nArtist: " + this.artist;
        songInfo += "\nTotal duration: " + this.getTotalDuration();
        songInfo += "\nScientific pitch notation:";
        for (Note note : notes) {
            songInfo += "\n" + note.toString();
        }
        //songInfo += "\n" + Arrays.toString(notes);
        return songInfo;
    }

}
