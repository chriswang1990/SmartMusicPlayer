//Project authors: Minquan Wang & Guanqing Hao

package musicPlayer;

/*
 * Melody Player
 *
 * Authors: Robert Sedgewick and Kevin Wayne (see comments below);
 *          modified slightly by Allison Obourn and Marty Stepp
 *          modified slightly by Minquan Wang
 * Version: Wed 2016/06/01
 * 
 * This instructor-provided file implements the underlying audio system 
 */


/*************************************************************************
 * A simple library for reading, writing, and manipulating .wav files
 * and playing raw audio data.
 * <p>
 * Limitations:
 * - Does not seem to work properly when reading .wav files from a .jar file.
 * - Assumes the audio is monaural, with sampling rate of 44,100.
 *************************************************************************/

import java.util.HashSet;
import java.util.Set;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 * <i>Standard audio</i>. This class provides a basic capability for creating,
 * reading, and saving audio.
 * <p>
 * The audio format uses a sampling rate of 44,100 (CD quality audio), 16-bit,
 * monaural.
 *
 * <p>
 * For additional documentation, see <a
 * href="http://introcs.cs.princeton.edu/15inout">Section 1.5</a> of
 * <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by
 * Robert Sedgewick and Kevin Wayne.
 */
public final class StdAudio {
    /**
     * The sample rate - 44,100 Hz for CD quality audio.
     */
    public static final int SAMPLE_RATE = 44100;

    private static final int BYTES_PER_SAMPLE = 2; // 16-bit audio
    private static final int BITS_PER_SAMPLE = 16; // 16-bit audio
    private static final double MAX_16_BIT = Short.MAX_VALUE; // 32,767
    private static final int SAMPLE_BUFFER_SIZE = 4096;

    private static SourceDataLine line; // to play the sound
    private static byte[] buffer; // our internal buffer
    private static int bufferSize = 0; // number of samples currently in
    // internal buffer
    private static boolean muted = false;
    private static boolean paused = false;
    private static Set<AudioEventListener> listeners;

    // static initializer
    static {
        init();
    }

    private static StdAudio instance = new StdAudio();

    public static StdAudio getInstance() {
        return instance;
    }

    public static class AudioEvent {
        public enum Type {PLAY, LOOP, PAUSE, UNPAUSE, STOP, MUTE, UNMUTE}

        private Type type;
        private Note note;
        private double duration;

        public AudioEvent(Type type) {
            this(type, 0.0);
        }

        public AudioEvent(Type type, double duration) {
            this.type = type;
            this.duration = duration;
        }

        public AudioEvent(Type type, Note note, double duration) {
            this.type = type;
            this.note = note;
            this.duration = duration;
        }

        public double getDuration() {
            return duration;
        }

        public Type getType() {
            return type;
        }

        public String toString() {
            return "AudioEvent{Type=" + type
                  + (note == null ? "" : (", note=" + note))
                  + (duration == 0.0 ? "" : (", duration=" + duration)) + "}";
        }
    }

    public interface AudioEventListener {
        void onAudioEvent(AudioEvent event);
    }

    public void addAudioEventListener(AudioEventListener listener) {
        listeners.add(listener);
    }

    // open up an audio stream
    private static void init() {
        try {
            // 44,100 samples per second, 16-bit audio, mono, signed PCM, little
            // Endian
            AudioFormat format = new AudioFormat((float) SAMPLE_RATE, BITS_PER_SAMPLE, 1, true,
                  false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE);

            // the internal buffer is a fraction of the actual buffer size, this
            // choice is arbitrary
            // it gets divided because we can't expect the buffered data to line
            // up exactly with when
            // the sound card decides to push out its samples.
            buffer = new byte[SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE / 3];
            listeners = new HashSet<>();
        } catch (Exception e) {
            System.err.println("Error initializing musicPlayer.StdAudio audio system:");
            e.printStackTrace();
            System.exit(1);
        }

        // no sound gets made before this call
        line.start();
    }

    /**
     * Removes all audio event listeners from being notified of future
     * audio events, if any were present.  If none were present, has no effect.
     */
    public void clearAudioEventListeners() {
        listeners.clear();
    }

    /**
     * Close standard audio.
     */
    public void close() {
        line.drain();
        line.stop();
    }

    /**
     * Returns whether the audio system is currently muted.
     */
    public boolean isMuted() {
        return muted;
    }

    /**
     * Returns whether the audio system is currently paused.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Create a note (sine wave) of the given frequency (Hz), for the given
     * duration (seconds) scaled to the given volume (amplitude).
     */
    public double[] note(double hz, double duration, double amplitude) {
        int N = (int) (StdAudio.SAMPLE_RATE * duration);
        double[] a = new double[N + 1];
        for (int i = 0; i <= N; i++)
            a[i] = amplitude * Math.sin(2 * Math.PI * i * hz / StdAudio.SAMPLE_RATE);
        return a;
    }

    /**
     * Write one sample (between -1.0 and +1.0) to standard audio. If the sample
     * is outside the range, it will be clipped.
     */
    public void play(double in) {
        if (muted) {
            return;
        }

        // clip if outside [-1, +1]
        if (in < -1.0) {
            in = -1.0;
        }
        if (in > +1.0) {
            in = +1.0;
        }

        // convert to bytes
        short s = (short) (MAX_16_BIT * in);
        buffer[bufferSize++] = (byte) s;
        buffer[bufferSize++] = (byte) (s >> 8); // little Endian

        // send to sound card if buffer is full
        if (bufferSize >= buffer.length) {
            line.write(buffer, 0, buffer.length);
            bufferSize = 0;
        }
    }

    /**
     * Write an array of samples (between -1.0 and +1.0) to standard audio. If a
     * sample is outside the range, it will be clipped.
     */
    public void play(double[] input) {
        prePlay();
        for (double i : input) {
            play(i);
        }
    }

    /**
     * Write an array of samples (between -1.0 and +1.0) to standard audio. If a
     * sample is outside the range, it will be clipped.
     */
    public void play(Note note, double[] input, double duration) {
        play(input);
        notifyListeners(new AudioEvent(AudioEvent.Type.PLAY, note, duration));
    }

    /**
     * Removes the given audio event listener from being notified of future
     * audio events, if it was present.  If not present, has no effect.
     */
    public void removeAudioEventListener(AudioEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Sets whether the audio system is muted.
     * If audio is muted, notes do not play and playing methods return immediately.
     */
    public void setMute(boolean mute) {
        muted = mute;
        notifyListeners(new AudioEvent(mute ? AudioEvent.Type.MUTE : AudioEvent.Type.UNMUTE));
    }

    /**
     * Sets whether the audio system is paused.
     * If audio is paused, playing methods "block" in an infinite while loop.
     */
    public void setPaused(boolean pause) {
        paused = pause;
        notifyListeners(new AudioEvent(pause ? AudioEvent.Type.PAUSE : AudioEvent.Type.UNPAUSE));
    }

    /*
     * Informs all added audio event listeners of the given event.
     */
    private void notifyListeners(AudioEvent event) {
        for (AudioEventListener listener : listeners) {
            listener.onAudioEvent(event);
        }
    }

    /*
     * Maintenance to be done before playing; pause/mute management.
     */
    private void prePlay() {
        if (muted) {
            return;
        }
        while (paused) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                // empty
            }
        }
    }

    /**
     * Test client - play an A major scale to standard audio.
     */
    public static void main(String[] args) {
        // 440 Hz for 1 sec
        StdAudio stdAudio = StdAudio.getInstance();

        double freq = 440.0;
        for (int i = 0; i <= StdAudio.SAMPLE_RATE; i++) {
            stdAudio.play(0.5 * Math.sin(2 * Math.PI * freq * i / StdAudio.SAMPLE_RATE));
        }

        // scale increments
        int[] steps = {0, 2, 4, 5, 7, 9, 11, 12};
        for (int i : steps) {
            double hz = 440.0 * Math.pow(2, i / 12.0);
            stdAudio.play(stdAudio.note(hz, 1.0, 0.5));
        }

        // need to call this in non-interactive stuff so the program doesn't
        // terminate
        // until all the sound leaves the speaker.
        stdAudio.close();

        // need to terminate a Java program with sound
        System.exit(0);
    }

    /*
     * This private constructor ensures that musicPlayer.StdAudio is non-instantiable.
     */
    private StdAudio() {
        // empty
    }
}
