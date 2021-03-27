package chocolate_chiptunes;
import com.jsyn.*;
import com.jsyn.swing.*;
import com.jsyn.unitgen.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Synthesizer extends Circuit {

    public com.jsyn.Synthesizer synth;
    private LineOut out;
    private Instrument[] instruments = new Instrument[4];
    private int selectedInstrumentID;
    private int instrumentCount = 1;

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
        out = new LineOut();

        // Start the synth and add the line out
        synth.start();
        synth.add(out);

        // Create a default instrument
        instruments[0] = new Instrument();

        // Add the instrument and connect it to the line out
        synth.add(instruments[0].getOscillator());
        instruments[0].getOscillator().output.connect(0, out.input, 0);
        instruments[0].getOscillator().output.connect(0, out.input, 1);
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
        synth.remove(instruments[selectedInstrumentID].getOscillator());
        instruments[selectedInstrumentID].getOscillator().output.disconnect(0, out.input, 0);
        instruments[selectedInstrumentID].getOscillator().output.disconnect(0, out.input, 1);
    }

    // Connect the currently selected waveform
    public void connectInstrument() {
        synth.add(instruments[selectedInstrumentID].getOscillator());
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

    public void setKeyFrequency(char keyChar) {
        instruments[selectedInstrumentID].getOscillator().frequency.set(KEY_FREQUENCIES.get(keyChar));
        out.start();
    }

    public void stopOut() {
        out.stop();
    }
}
