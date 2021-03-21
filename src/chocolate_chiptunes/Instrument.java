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

    public Instrument() {
        waveform = sine;

        /* Standard ADSR envelope (https://en.wikipedia.org/wiki/Envelope_(music)#ADSR)
        *  envelopeData[0] = attack value
        *  envelopeData[2] = decay value
        *  envelopeData[4] = sustain value
        *  envelopeData[6] = release value
        *  envelopeData[1, 3, 5, 7] represent amplitude (this doesn't change)
        */
        envelopeData[0] = 0.5;
        envelopeData[1] = 1.0;
        envelopeData[2] = 0.25;
        envelopeData[3] = 0.6;
        envelopeData[4] = 0.25;
        envelopeData[5] = 0.6;
        envelopeData[6] = 0.5;
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
}
