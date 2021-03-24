package chocolate_chiptunes;
import com.jsyn.*;
import com.jsyn.swing.*;
import com.jsyn.unitgen.*;

public class Synthesizer extends Circuit {

    public com.jsyn.Synthesizer synth;
    private LineOut out;
    private Instrument[] instruments = new Instrument[4];
    private int selectedInstrumentID;
    private int instrumentCount = 1;

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

    // Reset output
    public void resetOut() {
        out.stop();
        out.start();
    }

    // Stop the synth
    public void stopSynth() {
        synth.stop();
    }
}
