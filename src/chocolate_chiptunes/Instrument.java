package chocolate_chiptunes;

import com.jsyn.JSyn;
import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.swing.EnvelopePoints;
import com.jsyn.unitgen.*;

public class Instrument {

    private UnitOscillator waveform;
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
        waveform = sine;

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

        envelope = new SegmentedEnvelope(envelopeData);
    }

    public void setWaveform(int waveformID) {
        switch(waveformID) {
            case 0: // Sine
                waveform = sine;
            case 1: // Square
                waveform = square;
            case 2: // Triangle
                waveform = triangle;
            case 3: // Sawtooth
                waveform = saw;
        }
    }

    public UnitOscillator getOscillator() {
        return this.waveform;
    }

    public void updateEnvelope(double[] data) {
        this.envelopeData = data;
        envelope.write(this.envelopeData);
    }

    public double[] getEnvelopeData() {
        return envelopeData;
    }
}
