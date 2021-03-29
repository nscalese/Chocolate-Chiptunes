package chocolate_chiptunes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class Controller {
	
	ActionLog actionLog = new ActionLog();

	Synthesizer synth = new Synthesizer();

	int selectedInstrumentID = 0;
		
	@FXML
	private GridPane mainGrid;
	
	@FXML
	private ScrollPane pianoRoll;
	
	@FXML
	private ScrollPane arrangementEditor;
	
	@FXML
	private Label bpm;

	@FXML
	private Button btnAddInstrument;

	@FXML
	private GridPane instrumentList;

	@FXML
	private Button instrument1;

	@FXML
	private Slider attackSlider;

	@FXML
	private Slider decaySlider;

	@FXML
	private Slider sustainSlider;

	@FXML
	private Slider releaseSlider;

	@FXML
	private ToggleButton sineButton;

	@FXML
	private ToggleButton squareButton;

	@FXML
	private ToggleButton triangleButton;

	@FXML
	private ToggleButton sawButton;


	public void setSynth(Synthesizer synth) {
		this.synth = synth;
	}

	// Show the piano roll when the piano roll button is pressed
	@FXML
	public void showPianoRoll(ActionEvent event) {
		pianoRoll.setVisible(true);
		arrangementEditor.setVisible(false);
	}

	// Show the arrangement when the arrangement button is pressed
	@FXML
	public void showArrangementEditor(ActionEvent event) {
		pianoRoll.setVisible(false);
		arrangementEditor.setVisible(true);
	}

	// Increment the BPM value when the BPM slider is changed
	@FXML
	public void incrementBPM() {
		int bpmValue = Integer.parseInt(bpm.getText()), newBPMValue = bpmValue + 1;		
		
		bpm.setText(String.valueOf(newBPMValue));
	}

	// Decrement the BPM value when the BPM slider is changed
	@FXML
	public void decrementBPM() {
		int bpmValue = Integer.parseInt(bpm.getText()), newBPMValue = bpmValue - 1;
		
		
		bpm.setText(String.valueOf(newBPMValue));
	}

	// Add a new instrument when the add instrument button is clicked
	@FXML
	public void onAddInstrumentClick(MouseEvent e) {
		// Get the current instrument count
		int instrumentCount = synth.getInstrumentCount();

		// If there are 4 instruments or more, move on
		if(instrumentCount < 4) {
			// Create the instrument and increment the count
			synth.createInstrument();
			instrumentCount++;

			// Create a new Instrument button in the list and add the necessary functionality
			Button newInstrument = new Button("Instrument " + instrumentCount);
			newInstrument.setUserData(instrumentCount - 1);
			newInstrument.setId("instrument" + instrumentCount);
			newInstrument.setOnMouseClicked(event -> {
				onInstrumentButtonClick(event);
			});
			newInstrument.setMaxHeight(btnAddInstrument.getMaxHeight());
			newInstrument.setMaxWidth(btnAddInstrument.getMaxWidth());

			// Make sure the instrument is added to the list correctly
			instrumentList.getChildren().remove(btnAddInstrument);
			instrumentList.add(newInstrument, 0, instrumentCount - 1);
			instrumentList.add(btnAddInstrument, 0, instrumentCount);
		} else {
			System.out.println("Max instruments");
		}

	}

	// When an instrument in the list is clicked, change the envelope values
	@FXML
	public void onInstrumentButtonClick(MouseEvent e) {
		// Get the button object of the current instrument
		Object node = e.getSource();
		Button instrument = (Button)node;

		// Disconnect current waveform before connected new one
		synth.disconnectInstrument();

		// Get the ID associated with the button
		selectedInstrumentID = Integer.parseInt(instrument.getUserData().toString());
		synth.setSelectedInstrument(selectedInstrumentID);

		// Connect waveform of current instrument
		synth.connectInstrument();

		// Get the waveform of the selected instrument and set the RadioButton of the waveform
		int waveformId = synth.getSelectedInstrument().getWaveformId();
		if(waveformId == 0) {
			sineButton.setSelected(true);
		} else if (waveformId == 1) {
			squareButton.setSelected(true);
		} else if (waveformId == 2) {
			triangleButton.setSelected(true);
		} else if (waveformId == 3) {
			sawButton.setSelected(true);
		}

		// Get the envelope data of the selected instrument and set the values of the sliders to reflect the values
		double[] envelopeData = synth.getSelectedInstrument().getEnvelopeData();
		attackSlider.setValue(envelopeData[Instrument.ATTACK_VALUE]);
		decaySlider.setValue(envelopeData[Instrument.DECAY_VALUE]);
		sustainSlider.setValue(envelopeData[Instrument.SUSTAIN_VALUE]);
		releaseSlider.setValue(envelopeData[Instrument.RELEASE_VALUE]);
	}

	// Change the waveform of the current instrument when a waveform is selected
	@FXML
	public void onWaveformClick(MouseEvent e) {
		// Get the button object of the current instrument
		Object node = e.getSource();
		RadioButton waveform = (RadioButton)node;

		synth.disconnectInstrument();
		// Retrieve the waveform ID and set the waveform of the instrument
		int selectedWaveformID = Integer.parseInt(waveform.getUserData().toString());
		synth.getSelectedInstrument().setWaveform(selectedWaveformID);

		synth.connectInstrument();
	}

	// Update the envelope of the instrument when the user changes a slider value
	@FXML
	public void onSliderChanged() {
		// Get the values of the individual sliders
		double attackValue = (double) attackSlider.getValue();
		double decayValue = (double) decaySlider.getValue();
		double sustainValue = (double) sustainSlider.getValue();
		double releaseValue = (double) releaseSlider.getValue();

		// Place the values in a double array
		double[] envelopeData = {
				attackValue, 1.0,
				decayValue, 0.6,
				sustainValue, 0.6,
				releaseValue, 0.0
		};

		// Update the envelope data of the instrument
		synth.getSelectedInstrument().updateEnvelope(envelopeData);
	}

	/**
	 * This function determines what to do based on the keys pressed while on the gridpane (piano adapter, ctrl functions, etc.)
	 * 
	 * @param event - the key event object containing information about the particular key event
	 * @return nothing
	 */
	@FXML
	public void onGridKeyPressed(KeyEvent event) {
		
		KeyCode keyCode = event.getCode();
		
		//Begin special function processing
		if(event.isControlDown()) {
			switch(keyCode) {
			case Z:
				actionLog.Undo();
				break;
			case Y:
				actionLog.Redo();
				break;
			case S: 
				if(event.isShiftDown())
					System.out.println("Save the project to a certain file");
				else
					System.out.println("Save the project");
				break;
			default:
				System.out.println("There is no special function for this character sequence.");
			}
		}
	}
}
