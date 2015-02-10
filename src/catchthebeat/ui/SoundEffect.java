package catchthebeat.ui;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Class SoundEffect
 * 
 * This class serves as a container holding Clip objects used for playing
 * short sound samples that are pre-loaded in the memory during the 
 * application initialization.
 * 
 * @author Michal Kabát
 * @version 2012.04
 */
public class SoundEffect {

    // Nested class for specifying volume
    public static enum Volume {

        MUTE, LOW, MEDIUM, HIGH
    }
    public static Volume volume = Volume.HIGH;
    private final int numOfClips;
    private Clip[] clips;
    private String[] stringClips = {"../sounds/drum0.wav", "../sounds/drum1.wav", "../sounds/drum2.wav", "../sounds/drum3.wav", "../sounds/correct.wav", "../sounds/fail.wav"};
    public static final int CORRECT_FX = 4;
    public static final int FAIL_FX = 5;

    /**
     * Constructor takes the hardcoded array of Strings containing the relative
     * path to sound effects, creates Clip objects out of them and pre-loads
     * them in the memory.
     */
    SoundEffect() {
        numOfClips = stringClips.length;
        clips = new Clip[numOfClips];
        try {
            for (int i = 0; i < numOfClips; i++) {
                URL url = this.getClass().getResource(stringClips[i]);
                // Set up an audio input stream piped from the sound file.
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
                // Get a clip resource.
                clips[i] = AudioSystem.getClip();
                // Preload the clip into memory.
                clips[i].open(audioInputStream);
            }

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rewinds the desired soundclip and plays it.
     * 
     * The play method does not use threads (however, the Clip objects use 
     * threading when playing).
     * Reason: to save CPU load (separate threads are not created everytime play() 
     * method is called).
     * Drawback: every sound effect can play only once in a given time. If play()
     * method is called with request to play a sound effect that is already playing,
     * the playback is interrupted and played again from the beginning.
     * However, for purpose of this application this should be a sufficient solution.
     * 
     * @param clipNo Index of desired sound effect in the clips array.
     */
    public void play(int clipNo) {
        clips[clipNo].setFramePosition(0);
        clips[clipNo].start();
    }
}
