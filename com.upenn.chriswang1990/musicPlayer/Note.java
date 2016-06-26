//Project authors: Minquan Wang & Guanqing Hao

package musicPlayer;/*

/**
 * Each musicPlayer.Note object represents a musical note or rest.
 * A musicPlayer.Note encapsulates a pitch (A-G), a duration in seconds, an octave,
 * an accidental (sharp, flat, or natural), and a flag of whether it is the
 * start/end of a repeated section or not.
 * A song can be thought of as a list or array of musicPlayer.Note objects.
 */

public class Note {
    /**
     * Constant for the minimum legal value that an octave can have.
     */
    public static final int OCTAVE_MIN = 1;

    /**
     * Constant for the maximum legal value that an octave can have.
     */
    public static final int OCTAVE_MAX = 10;

    /*
     * Whether this class should print messages to the console for debugging.
     */
    public static boolean DEBUG = true;

    // fields
    private double duration;         // note's duration in seconds
    private Pitch pitch;             // note's pitch from A-G or R for rest
    private int octave;              // note's octave from 1-10
    private Accidental accidental;   // note's accidental: sharp, flat, natural
    private boolean repeat;          // true if this note starts/ends a repeated section

    private StdAudio stdAudio = StdAudio.getInstance();

    /**
     * Constructs a musicPlayer.Note with the given information.
     *
     * @param duration   musicPlayer.Note's duration in seconds.
     * @param pitch      musicPlayer.Note's pitch from musicPlayer.Pitch.A through musicPlayer.Pitch.G, or musicPlayer.Pitch.R for a rest.
     * @param octave     musicPlayer.Note's octave from OCTAVE_MIN through OCTAVE_MAX inclusive.
     * @param accidental musicPlayer.Note's accidental from musicPlayer.Accidental.SHARP, FLAT, or NATURAL.
     * @param repeat     true if this note starts/ends a repeated section.
     * @throws NullPointerException     if pitch or accidental is null.
     * @throws IllegalArgumentException if duration is negative or octave is not
     *                                  between OCTAVE_MIN and OCTAVE_MAX inclusive.
     */
    public Note(double duration, Pitch pitch, int octave, Accidental accidental, boolean repeat) {
        setPitch(pitch);
        setAccidental(accidental);
        setDuration(duration);
        setOctave(octave);
        this.repeat = repeat;
    }

    /**
     * Constructs a new rest (musicPlayer.Pitch.R) of the given duration.
     *
     * @param duration musicPlayer.Note's duration in seconds.
     * @param repeat   true if this rest starts/ends a repeated section.
     * @throws NullPointerException     if accidental is null.
     * @throws IllegalArgumentException if duration is negative.
     */
    public Note(double duration, boolean repeat) {
        this(duration, Pitch.R, OCTAVE_MIN + 1, Accidental.NATURAL, repeat);
    }

    /**
     * Returns true if o refers to a musicPlayer.Note object with the same state
     * as this musicPlayer.Note object; otherwise false.
     *
     * @param o the object to compare against
     */
    public boolean equals(Object o) {
        if (o instanceof Note) {
            Note other = (Note) o;
            if (pitch == Pitch.R && other.pitch == Pitch.R) {
                // compare two rests
                return this.duration == other.duration
                      && this.repeat == other.repeat;
            } else {
                // compare two notes, or a note and a rest
                return this.duration == other.duration
                      && this.pitch == other.pitch
                      && this.octave == other.octave
                      && this.accidental == other.accidental
                      && this.repeat == other.repeat;
            }
        } else {
            return false;
        }
    }

    /**
     * Returns this musicPlayer.Note's accidental value of SHARP, FLAT, or NATURAL.
     * The accidental value is meaningless for a rest; this method will
     * return musicPlayer.Accidental.NATURAL by default if called on a rest.
     */
    public Accidental getAccidental() {
        return accidental;
    }

    /**
     * Returns this musicPlayer.Note's duration in seconds.
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Returns this musicPlayer.Note's octave.
     * The octave value is meaningless for a rest; this method will return
     * OCTAVE_MIN + 1 by default if called on a rest.
     */
    public int getOctave() {
        return octave;
    }

    /**
     * Returns this musicPlayer.Note's pitch value of A-G or R for a rest.
     */
    public Pitch getPitch() {
        return pitch;
    }

    /**
     * Returns true if this musicPlayer.Note is the start or end of a repeated section.
     */
    public boolean isRepeat() {
        return repeat;
    }

    /**
     * Returns true if this musicPlayer.Note is a rest.  Equivalent to checking whether
     * this note's pitch is musicPlayer.Pitch.R.  Provided for convenience.
     */
    public boolean isRest() {
        return pitch == Pitch.R;
    }

    /**
     * Plays this note through the underlying audio system.
     * Also prints a message to the system console for debugging.
     * If the audio system is muted or paused, the note may not play.
     */
    public void play() {
        if (DEBUG) {
            System.out.println("  - playing " + duration + " " + pitch
                  + (pitch == Pitch.R ? "" : (" " + octave + " " + accidental)));
        }
        if (pitch == Pitch.R) {
            // play no sound (but do delay) if the note is a rest
            // musicPlayer.StdAudio.play(musicPlayer.StdAudio.note(0, duration, 0.5), duration);
            stdAudio.play(this, stdAudio.note(0, duration, 0.5), duration);
        } else {
            char note = pitch.toString().charAt(0);
            int steps = (note - 'A') * 2;

            // adjust for sharps/flats
            if (note == 'C' || note == 'D' || note == 'E') {
                steps -= 1;
            } else if (note == 'F' || note == 'G') {
                steps -= 2;
            }

            // adjust pitch for proper octave
            if (octave > 4 || (octave == 4 && note <= 'B')) {
                steps += (octave - 4) * 12;
            } else {
                steps -= (4 - octave) * 12;
            }

            // octave start at C so A and B are an octave lower
            if (note != 'A' && note != 'B') {
                steps -= 12;
            }

            // adjust for sharps and flats
            if (accidental.equals(Accidental.SHARP)) {
                steps += 1;
            } else if (accidental.equals(Accidental.FLAT)) {
                steps -= 1;
            }

            // play the note!
            double hz = 440.0 * Math.pow(2, steps / 12.0);
            stdAudio.play(this, stdAudio.note(hz, duration, 0.5), duration);
        }
    }

    /**
     * Sets this musicPlayer.Note's accidental value to be the given value: SHARP, FLAT, or NATURAL.
     * The accidental value is meaningless for a rest, but the musicPlayer.Note object still
     * maintains an accidental value internally (initially musicPlayer.Accidental.NATURAL)
     * which is ignored.
     *
     * @param accidental musicPlayer.Note's accidental from musicPlayer.Accidental.SHARP, FLAT, or NATURAL.
     * @throws NullPointerException if the accidental is null.
     */
    public void setAccidental(Accidental accidental) {
        if (accidental == null) {
            throw new NullPointerException();
        }
        if (pitch != Pitch.R) {
            this.accidental = accidental;
        }
    }

    /**
     * Sets this musicPlayer.Note's duration in seconds to be the given value.
     *
     * @param duration musicPlayer.Note's duration in seconds.
     * @throws IllegalArgumentException if duration is negative.
     */
    public void setDuration(double duration) {
        if (duration < 0.0) {
            throw new IllegalArgumentException();
        }
        this.duration = duration;
    }

    /**
     * Sets this musicPlayer.Note's octave to be the given value.
     * The octave value is meaningless for a rest, but the musicPlayer.Note object still
     * maintains an octave value internally (initially OCTAVE_MIN + 1)
     * which is ignored.
     *
     * @param octave musicPlayer.Note's octave from OCTAVE_MIN through OCTAVE_MAX inclusive.
     * @throws IllegalArgumentException if octave is not between OCTAVE_MIN
     *                                  and OCTAVE_MAX inclusive.
     */
    public void setOctave(int octave) {
        if (octave < OCTAVE_MIN || octave > OCTAVE_MAX) {
            throw new IllegalArgumentException("Illegal octave value: " + octave);
        }
        if (pitch != Pitch.R) {
            this.octave = octave;
        }
    }

    /**
     * Sets this musicPlayer.Note's pitch to be the given value.
     *
     * @param pitch musicPlayer.Note's pitch from musicPlayer.Pitch.A through musicPlayer.Pitch.G, or musicPlayer.Pitch.R for a rest.
     * @throws NullPointerException if pitch is null.
     */
    public void setPitch(Pitch pitch) {
        if (pitch == null) {
            throw new NullPointerException();
        }
        this.pitch = pitch;
    }

    /**
     * Sets this musicPlayer.Note's repeat flag to be the given value.
     *
     * @param repeat true to indicate that this note is the start/end of a
     *               repeated section, or false if not.
     */
    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    /**
     * Returns a string representation of this note.
     *
     * @return A string such as "0.4 C 5 NATURAL false".
     */
    public String toString() {
        if (pitch == Pitch.R) {
            return duration + " " + pitch + " " + repeat;
        } else {
            return duration + " " + pitch + " " + octave + " " + accidental + " " + repeat;
        }
    }
}
