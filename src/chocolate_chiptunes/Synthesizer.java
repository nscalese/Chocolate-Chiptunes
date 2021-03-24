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
        synth = JSyn.createSynthesizer();
        out = new LineOut();

        synth.start();
        synth.add(out);

        instruments[0] = new Instrument();

        synth.add(instruments[0].getOscillator());
        instruments[0].getOscillator().output.connect(0, out.input, 0);
        instruments[0].getOscillator().output.connect(0, out.input, 1);


    }

    public void createInstrument() {
        instrumentCount++;

        if(instrumentCount <= 4) {
            instruments[instrumentCount - 1] = new Instrument();
        } else {
            System.out.println("Max instruments created.");
        }
    }

    public Instrument getSelectedInstrument() {
        return instruments[selectedInstrumentID];
    }

    public void setSelectedInstrument(int id) {
        selectedInstrumentID = id;
    }

    public int getInstrumentCount() {
        return instrumentCount;
    }

    public void resetOut() {
        out.stop();
        out.start();
    }

    public void stopSynth() {
        synth.stop();
    }
}
