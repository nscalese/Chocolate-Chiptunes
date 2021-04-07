package chocolate_chiptunes;
import com.jsyn.*;
import com.jsyn.swing.*;
import com.jsyn.unitgen.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Synthesizer extends Circuit {

    public com.jsyn.Synthesizer synth;
    private LineOut out;
    private Instrument[] instruments = new Instrument[4];
    private VariableRateDataReader envelopePlayer;
    private int selectedInstrumentID;
    private int instrumentCount = 1;
    private int bpm = 120;

    public static final HashMap<Character, Double> KEY_FREQUENCIES = new HashMap<Character, Double>();

    static {
        final char[] KEYS = "q2w3er5t6y7uzsxdcvgbhnjm".toCharArray();
        final int STARTING_KEY = 40;
        for(int i = STARTING_KEY, key = 0; key < KEYS.length; i++, key++) {
            KEY_FREQUENCIES.put(KEYS[key], Utils.Math.getKeyFrequency(i));
        }

    }

    public Synthesizer() {
        // Create the synth and line out
        synth = JSyn.createSynthesizer();

        // Create a default instrument
        instruments[0] = new Instrument();

        // Add the instrument and connect it to the line out
        synth.add(instruments[0].getOscillator());

        synth.add(envelopePlayer = new VariableRateMonoReader());

        // Start the synth and add the line out
        synth.add(out = new LineOut());

        envelopePlayer.output.connect(instruments[0].getOscillator().amplitude);

        instruments[0].getOscillator().output.connect(0, out.input, 0);
        instruments[0].getOscillator().output.connect(0, out.input, 1);
        synth.start();
    }

    /* Called when the new instrument button is pressed
     * Increments the instrument count and adds the new instrument to the list
     */
    public void createInstrument() {
        instrumentCount++;

        if(instrumentCount <= 4) {
            instruments[instrumentCount - 1] = new Instrument();
        } else {
            System.out.println("Max instruments created.");
        }
    }

    // Returns the currently selected instrument in the instrument list
    public Instrument getSelectedInstrument() {
        return instruments[selectedInstrumentID];
    }

    // Set the currently selected instrument id
    public void setSelectedInstrument(int id) {
        selectedInstrumentID = id;
    }

    // Return the number of instruments
    public int getInstrumentCount() {
        return instrumentCount;
    }

    // Disconnect the currently selected waveform
    public void disconnectInstrument() {
        instruments[selectedInstrumentID].getOscillator().output.disconnect(0, out.input, 0);
        instruments[selectedInstrumentID].getOscillator().output.disconnect(0, out.input, 1);

        envelopePlayer.output.disconnect(instruments[selectedInstrumentID].getOscillator().amplitude);

        synth.remove(instruments[selectedInstrumentID].getOscillator());

    }

    // Connect the currently selected waveform
    public void connectInstrument() {
        synth.add(instruments[selectedInstrumentID].getOscillator());

        envelopePlayer.output.connect(instruments[selectedInstrumentID].getOscillator().amplitude);

        instruments[selectedInstrumentID].getOscillator().output.connect(0, out.input, 0);
        instruments[selectedInstrumentID].getOscillator().output.connect(0, out.input, 1);
    }

    // Reset output
    public void resetOut() {
        out.stop();
        out.start();
    }

    // Stop the synth
    public void stopSynth() {
        synth.stop();
    }
    
    // Get the current time
    public double getCurrentTime() {
    	return synth.getCurrentTime();
    }
    
    // Get the current BPM
    public int getBPM(){
    	return bpm;
    }
    
    // Set the new BPM
    public void setBPM(int bpm) {
    	this.bpm = bpm;
    }

    public void playNote(char keyChar) {
        try {
            instruments[selectedInstrumentID].getOscillator().frequency.set(KEY_FREQUENCIES.get(keyChar));
            out.start();
            envelopePlayer.dataQueue.queueOn(instruments[selectedInstrumentID].getEnvelope());
        } catch (NullPointerException e) {

        }
    }
    
    public LineOut getLineOut() {
    	return out;
    }

    public void stopOut() {
        envelopePlayer.dataQueue.queueOff(instruments[selectedInstrumentID].getEnvelope());
    }

    public void playSong(double[] noteFrequencies) {
        double freq;
        long time = (long) ((60.0 / bpm) * 1000); // Time in milliseconds

        // If the note is not empty, set the frequency to the frequency in the array
        // Else, set the frequency to 0.0 (no volume)
        for(int i = 0; i < noteFrequencies.length; i++) {
            if(noteFrequencies[i] != -1.0) {
                freq = noteFrequencies[i];
                instruments[selectedInstrumentID].getOscillator().frequency.set(freq);
            } else {
                instruments[selectedInstrumentID].getOscillator().frequency.set(0.0);
            }

            // Then, start the output and queue the note
            out.start();
            envelopePlayer.dataQueue.queueOn(instruments[selectedInstrumentID].getEnvelope());

            // Wait until the note is done playing
            try {
                System.out.println("Waiting...");
                TimeUnit.MILLISECONDS.sleep(time);
                System.out.println("Done waiting");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Then turn the queue off and loop until all notes are played
            stopOut();
        }
    }
}
