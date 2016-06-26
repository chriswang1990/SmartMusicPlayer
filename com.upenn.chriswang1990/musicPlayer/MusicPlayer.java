//Project authors: Minquan Wang & Guanqing Hao

package musicPlayer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class MusicPlayer implements ActionListener, StdAudio.AudioEventListener {

    // instance variables
    private Song song;
    private boolean playing; // whether a song is currently playing
    private JFrame frame;
    private JFileChooser fileChooser;
    private JTextField tempoText;
    private JSlider currentTimeSlider;
    private JPanel time, timeLabels, buttons, tempo;
    private JButton load, play, pause, stop, up, down, reverse, changeTempo;
    private StdAudio stdAudio = StdAudio.getInstance();

    //these are the two labels that indicate time
    // to the right of the slider
    private JLabel currentTimeLabel, totalTimeLabel;

    //this the label that shows 'welcome to the music player' and the song information after loading
    private JLabel titleLabel;

    //a label that shows the current status of the player
    private JLabel statusLabel;
    private JLabel blank = new JLabel("");

    Color green = new Color(102, 204, 10);
    Color blue = new Color(10, 102, 204);
    Color pink = new Color(204, 10, 102);

    /*
     * Creates the music player GUI window and graphical components.
     */
    public MusicPlayer() {
        song = null;
        createComponents();
        doLayout();
        stdAudio.addAudioEventListener(this);
        frame.setVisible(true);
    }

    /*
     * Called when the user interacts with graphical components, such as
     * clicking on a button.
     */
    public void actionPerformed(ActionEvent event) {
        String cmd = event.getActionCommand();
        switch (cmd) {
            case "Play":
                playSong();
                break;
            case "Pause":
                stdAudio.setPaused(!stdAudio.isPaused());
                pause.setText("Resume");
                break;
            case "Resume":
                stdAudio.setPaused(!stdAudio.isPaused());
                pause.setText("Pause");
                break;
            case "Stop":
                stdAudio.setMute(true);
                stdAudio.setPaused(false);
                playing = false;
                break;
            case "Load":
                try {
                    loadFile();
                } catch (IOException ioe) {
                    System.out.println("not able to load from the file");
                }
                break;
            case "Reverse":
                song.reverse();
                statusLabel.setText("  Reverse");
                statusLabel.setForeground(pink);
                break;
            case "Octave Up":
                if (song.octaveUp()) {
                    System.out.println("Octave up successfully!");
                    statusLabel.setText("  Octave up");
                    statusLabel.setForeground(green);
                } else {
                    System.out.println("Can't octave up this song anymore!");
                    statusLabel.setText("Reach upper limit");
                    statusLabel.setForeground(Color.red);
                }
                break;
            case "Octave Down":
                if (song.octaveDown()) {
                    System.out.println("Octave down successfully!");
                    statusLabel.setText("  Octave down");
                    statusLabel.setForeground(green);
                } else {
                    System.out.println("Can't octave down this song anymore!");
                    statusLabel.setText("Reach lower limit");
                    statusLabel.setForeground(Color.red);
                }
                break;
            case "Change Tempo":
                String input = tempoText.getText();
                System.out.println(input);
                try {
                    double tempo = Double.parseDouble(input);
                    song.changeTempo(tempo);
                    updateTotalTime();
                    statusLabel.setText("Tempo changed");
                    statusLabel.setForeground(blue);
                } catch (NumberFormatException nfe) {
                    System.out.println("please input a number tempo ratio");
                }
                break;
        }
    }

    /*
     * Called when audio events occur in the musicPlayer.StdAudio library. We use this to
     * set the displayed current time in the slider.
     */
    public void onAudioEvent(StdAudio.AudioEvent event) {
        // update current time
        if (event.getType() == StdAudio.AudioEvent.Type.PLAY
              || event.getType() == StdAudio.AudioEvent.Type.STOP) {
            setCurrentTime(getCurrentTime() + event.getDuration());
        }
    }

    /*
     * Sets up the graphical components in the window and event listeners.
     */
    private void createComponents() {
        // note that you should have already defined your components as instance variables.
        Font titleFont = new Font("Razer Oblique", Font.BOLD, 22);
        Font compFont = new Font("Razer Regular", Font.BOLD, 13);
        Font compFont2 = new Font("Razer Regular", Font.PLAIN, 13);

        frame = new JFrame("GM Smart Music Player");
        titleLabel = new JLabel("Welcome to GM Smart Music Player!");
        titleLabel.setFont(titleFont);

        time = new JPanel();
        timeLabels = new JPanel();
        currentTimeLabel = new JLabel("000000.0 /");
        totalTimeLabel = new JLabel("000000.0 sec");
        currentTimeLabel.setFont(compFont);
        totalTimeLabel.setFont(compFont);
        currentTimeSlider = new JSlider();

        load = new JButton("Load");
        play = new JButton("Play");
        pause = new JButton("Pause");
        stop = new JButton("Stop");
        up = new JButton("Octave Up");
        down = new JButton("Octave Down");
        reverse = new JButton("Reverse");
        load.setFont(compFont);
        play.setFont(compFont);
        pause.setFont(compFont);
        stop.setFont(compFont);
        up.setFont(compFont);
        down.setFont(compFont);
        reverse.setFont(compFont);
        buttons = new JPanel();

        tempo = new JPanel();
        statusLabel = new JLabel("  Status : Normal");
        statusLabel.setFont(compFont2);
        tempoText = new JTextField("Enter the ratio");
        tempoText.setEditable(false);
        tempoText.setFont(compFont2);
        changeTempo = new JButton("Change Tempo");
        changeTempo.setFont(compFont2);

        doEnabling();
    }

    /*
     * Sets whether every button, slider, spinner, etc. should be currently
     * enabled, based on the current state of whether a song has been loaded and
     * whether or not it is currently playing. This is done to prevent the user
     * from doing actions at inappropriate times such as clicking play while the
     * song is already playing, etc.
     */
    private void doEnabling() {
        if (song == null) {
            play.setEnabled(false);
            pause.setEnabled(false);
            stop.setEnabled(false);
            up.setEnabled(false);
            down.setEnabled(false);
            reverse.setEnabled(false);
            changeTempo.setEnabled(false);
        } else {
            play.setEnabled(true);
            pause.setEnabled(false);
            stop.setEnabled(false);
            up.setEnabled(true);
            down.setEnabled(true);
            reverse.setEnabled(true);
            changeTempo.setEnabled(true);
            if (playing) {
                play.setEnabled(false);
                pause.setEnabled(true);
                stop.setEnabled(true);
                up.setEnabled(false);
                down.setEnabled(false);
                reverse.setEnabled(false);
                changeTempo.setEnabled(false);
            }
        }
    }

    /*
     * Performs layout of the components within the graphical window.
     * Also make the window a certain size and put it in the center of the screen.
     */
    private void doLayout() {
        frame.setLayout(new GridLayout(4, 1));
        frame.add(titleLabel);

        timeLabels.setLayout(new GridLayout(2, 1));
        timeLabels.add(currentTimeLabel);
        timeLabels.add(totalTimeLabel);
        time.setLayout(new FlowLayout());
        currentTimeSlider.setPreferredSize(new Dimension(540, 60));
        currentTimeSlider.setValue(0);
        time.add(currentTimeSlider);
        time.add(timeLabels);
        frame.add(time);

        buttons.setLayout(new GridLayout(1, 7));
        buttons.add(load);
        buttons.add(play);
        buttons.add(pause);
        buttons.add(stop);
        buttons.add(up);
        buttons.add(down);
        buttons.add(reverse);
        frame.add(buttons);

        tempo.setLayout(new GridLayout(0, 4));
        tempo.add(statusLabel);
        tempo.add(blank);
        tempo.add(tempoText);
        tempo.add(changeTempo);
        frame.add(tempo);

        load.addActionListener(this);
        play.addActionListener(this);
        pause.addActionListener(this);
        stop.addActionListener(this);
        up.addActionListener(this);
        down.addActionListener(this);
        reverse.addActionListener(this);
        changeTempo.addActionListener(this);

        frame.setMinimumSize(new Dimension(850, 250));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        //center the frame on the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
    }

    /*
     * Returns the estimated current time within the overall song, in seconds.
     */
    private double getCurrentTime() {
        String timeStr = currentTimeLabel.getText();
        timeStr = timeStr.replace(" /", "");
        try {
            return Double.parseDouble(timeStr);
        } catch (NumberFormatException nfe) {
            return 0.0;
        }
    }

    /*
     * Pops up a file-choosing window for the user to select a song file to be
     * loaded. If the user chooses a file, a musicPlayer.Song object is created and used
     * to represent that song.
     */
    private void loadFile() throws IOException {
        fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File selected = fileChooser.getSelectedFile();
        if (selected == null) {
            return;
        }
        statusLabel.setText("  Loading complete");
        statusLabel.setForeground(green);
        String filename = selected.getAbsolutePath();
        System.out.println("Loading song from " + selected.getName() + " ...");
        song = new musicPlayer.Song(filename);
        frame.setTitle(song.getArtist() + " - [" + song.getTitle() + "]");
        titleLabel.setText(song.getArtist() + " - [" + song.getTitle() + "]");

        tempoText.setText("1.0");
        setCurrentTime(0.0);
        tempoText.setEditable(!playing);

        updateTotalTime();
        System.out.println("Loading complete.");
        System.out.println("musicPlayer.Song: " + song);
        doEnabling();
    }

    /*
     * Initiates the playing of the current song in a separate thread (so
     * that it does not lock up the GUI).
     * You do not need to change this method.
     * It will not compile until you make your musicPlayer.Song class.
     */
    private void playSong() {
        if (song != null) {
            setCurrentTime(0.0);
            Thread playThread = new Thread(new Runnable() {
                public void run() {
                    stdAudio.setMute(false);
                    playing = true;
                    doEnabling();
                    String title = song.getTitle();
                    String artist = song.getArtist();
                    double duration = song.getTotalDuration();

                    System.out.println("Playing \"" + title + "\", by "
                          + artist + " (" + duration + " sec)");
                    song.play();
                    System.out.println("Playing complete.");
                    playing = false;
                    doEnabling();
                }
            });
            playThread.start();
        }
    }

    /*
     * Sets the current time display slider/label to show the given time in
     * seconds. Bounded to the song's total duration as reported by the song.
     */
    private void setCurrentTime(double time) {
        double total = song.getTotalDuration();
        time = Math.max(0, Math.min(total, time));
        currentTimeLabel.setText(String.format("%08.2f /", time));
        currentTimeSlider.setValue((int) (100 * time / total));
    }

    /*
     * Updates the total time label on the screen to the current total duration.
     */
    private void updateTotalTime() {
        double duration = song.getTotalDuration();
        totalTimeLabel.setText(String.format("%08.2f sec", duration));

    }
}
