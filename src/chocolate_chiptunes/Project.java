package chocolate_chiptunes;

public class Project {
	private ProjectInstrument[] instruments = new ProjectInstrument[4];
    private int bpm;
    private double masterVolume;
    private double[] noteFrequencies;
	
    Project(Instrument[] instruments, int bpm, double masterVolume, double[] noteFrequencies){

    	for(int i = 0; i < instruments.length; i++) {
    		if(instruments[i] != null)
    			this.instruments[i] = new ProjectInstrument(instruments[i].getWaveformId(), instruments[i].getEnvelopeData());
    	}
    	
    	this.bpm = bpm;
    	this.masterVolume = masterVolume;
    	this.noteFrequencies = noteFrequencies;
    }
    
    public ProjectInstrument[] getInstruments() {
    	return instruments;
    }

    public int getBpm(){
    	return bpm;
	}

	public double getMasterVolume(){
    	return masterVolume;
	}

	public double[] getNoteFrequencies(){
    	return noteFrequencies;
	}
    
    private class ProjectInstrument {
    	private int waveformID;
    	private double[] envelopeData;
    	
    	ProjectInstrument(int waveformID, double[] envelopeData){
    		this.waveformID = waveformID;
    		this.envelopeData = envelopeData;
    	}

    	public int getWaveformID(){
    		return waveformID;
		}

		public double[] getEnvelopeData(){
    		return envelopeData;
		}
    }
}