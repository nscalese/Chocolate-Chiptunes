package chocolate_chiptunes;

import com.jsyn.JSyn;
import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.swing.EnvelopePoints;
import com.jsyn.unitgen.*;

public class Instrument {

    private UnitOscillator waveform;
    private int waveformID;
    private UnitOscillator[] waveformList = new UnitOscillator[4];
    private SegmentedEnvelope envelope;
    private double[] envelopeData = new double[8];

    private SineOscillator sine = new SineOscillator();
    private SquareOscillator square = new SquareOscillator();
    private TriangleOscillator triangle = new TriangleOscillator();
    private SawtoothOscillator saw = new SawtoothOscillator();

    public static final int ATTACK_VALUE = 0;
    public static final int DECAY_VALUE = 2;
    public static final int SUSTAIN_VALUE = 4;
    public static final int RELEASE_VALUE = 6;


    public Instrument() {
        // Set the default waveform of a new instrument to a sine wave
        waveform = sine;
        waveformID = 0;

        /* Standard ADSR envelope (https://en.wikipedia.org/wiki/Envelope_(music)#ADSR)
        *  envelopeData[1, 3, 5, 7] represent amplitude (this doesn't change)
        */
        envelopeData[ATTACK_VALUE] = 0.5;
        envelopeData[1] = 1.0;
        envelopeData[DECAY_VALUE] = 0.25;
        envelopeData[3] = 0.6;
        envelopeData[SUSTAIN_VALUE] = 0.25;
        envelopeData[5] = 0.6;
        envelopeData[RELEASE_VALUE] = 0.5;
        envelopeData[7] = 0.0;

        // Set the envelope of the instrument
        envelope = new SegmentedEnvelope(envelopeData);
    }

    // Depending on the currently selected waveform, set the instrument
    public void setWaveform(int waveformID) {
        this.waveformID = waveformID;
        System.out.println("Setting waveform of ID: " + waveformID);

        if(waveformID == 0) {
            waveform = sine;
        } else if (waveformID == 1) {
            waveform = square;
        } else if (waveformID == 2) {
            waveform = triangle;
        } else if (waveformID == 3) {
            waveform = saw;
        } else {
            System.out.println("Error in selecting waveform");
        }
    }

    public int getWaveformId() {
        return waveformID;
    }

    // Return the currently selected waveform
    public UnitOscillator getOscillator() {
        return this.waveform;
    }

    // Update the envelope when the user changes any of the ADSR values
    public void updateEnvelope(double[] data) {
        this.envelopeData = data;
        envelope.write(this.envelopeData);
    }

    // Return the envelope values
    public double[] getEnvelopeData() {
        return envelopeData;
    }
}
