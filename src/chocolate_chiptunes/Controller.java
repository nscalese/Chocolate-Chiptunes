package chocolate_chiptunes;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.jsyn.unitgen.UnitOscillator;

public class Controller {
	
	ActionLog actionLog = new ActionLog(this);

	Synthesizer synth;

	int selectedInstrumentID = 0;

	private double[] noteFrequencies = new double[]{-1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0};
	private Button[] selectedNotes = new Button[] {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};

	@FXML
	private GridPane mainGrid;
	
	@FXML
	private GridPane chordsGrid;
	
	@FXML
	private ScrollPane pianoRoll;
		
	@FXML
	private ScrollPane arrangementEditor;
	
	@FXML
	private Label bpmLabel;

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
	
	@FXML
	private ToggleGroup waveformGroup;


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
	
	public void onBPMButtonClick(MouseEvent e) {
		int bpmValue = Integer.parseInt(bpmLabel.getText());
		
		String buttonID = ((Button)e.getSource()).getId();
		
		try {
			//Add the action
			actionLog.AddAction(
					String.valueOf(bpmValue),
					String.valueOf(buttonID.equals("incrementBPMButton") ? ++bpmValue : --bpmValue),
					bpmLabel,
					bpmLabel.getClass().getMethod("setText", String.class),
					this.getClass().getMethod("changeBPM", int.class));
		} catch (NoSuchMethodException | SecurityException e1) {
			System.out.println("An unexpected error has occured.");
			e1.printStackTrace();
		}	
		
		changeBPM(String.valueOf(bpmValue)); 
	}
	
	public void changeBPM(String bpmValue) {
		synth.setBPM(Integer.parseInt(bpmValue));
		
		bpmLabel.setText(bpmValue);
	}

	// Add a new instrument when the add instrument button is clicked
	@FXML
	public void onAddInstrumentClick(MouseEvent e) {
		addInstrument();
	}
	
	public void addInstrument() {
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
	/*
	public void removeInstrument(Button buttonToRemove) {
		// Get the current instrument count
				int instrumentCount = synth.getInstrumentCount();

				// If there are at least 2 instruments, move on
				if(instrumentCount > 1) {
					// Create the instrument and increment the count
					synth.removeInstrument();
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
*/
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
		ToggleButton waveform = (ToggleButton)e.getSource();
		
		//Instantiate an empty toggle button
		ToggleButton oldWaveform = null;

		//Switch conditional for the current instruments waveform
		//Sets the oldWaveform toggle button based on the waveformID 
		switch(synth.getSelectedInstrument().getWaveformId()) {
		case Instrument.SINE_WAVE:
			oldWaveform = sineButton;
			break;
		case Instrument.SQUARE_WAVE:
			oldWaveform = squareButton;
			break;
		case Instrument.TRIANGLE_WAVE:
			oldWaveform = triangleButton;
			break;
		case Instrument.SAW_WAVE:
			oldWaveform = sawButton;
			break;
		}
					
		//Conditional to prevent action from being added for certain cases
		if(oldWaveform != null && !oldWaveform.equals(waveform)) {
			try {
				//Add the action
				actionLog.AddAction(
						oldWaveform,
						waveform,
						waveformGroup,
						waveformGroup.getClass().getMethod("selectToggle", Toggle.class),
						this.getClass().getMethod("changeWaveform", Toggle.class));
			} catch (NoSuchMethodException | SecurityException e1) {
				System.out.println("An unexpected error has occured.");
				e1.printStackTrace();
			}	
		}
		
		//Change the wave form
		changeWaveform(waveform);
	}
	
	public void changeWaveform(Toggle waveform) {
		synth.disconnectInstrument();
		// Retrieve the waveform ID and set the waveform of the instrument
		int selectedWaveformID = Integer.parseInt(waveform.getUserData().toString());
		synth.getSelectedInstrument().setWaveform(selectedWaveformID);

		synth.connectInstrument();
	}

	// Update the envelope of the instrument when the user changes a slider value
	@FXML
	public void onSliderChanged(MouseEvent e) {
		//Grab the affected slider
		Slider sliderChanged = (Slider) e.getSource();
		
		//The envelope's array index corresponding to the slider type		
		int sliderIndex = -1;

		//Switch conditional for the slider's fxID
		//Sets the slider index value
		switch(sliderChanged.getId()) {
		case "attackSlider":
			sliderIndex = Instrument.ATTACK_VALUE;
			break;
		case "decaySlider":
			sliderIndex = Instrument.DECAY_VALUE;
			break;
		case "sustainSlider":
			sliderIndex = Instrument.SUSTAIN_VALUE;
			break;
		case "releaseSlider":
			sliderIndex = Instrument.RELEASE_VALUE;
			break;
		}
		
		//Grab old/new slider values
		double oldValue = synth.getSelectedInstrument().getEnvelopeData()[sliderIndex],
				newValue = sliderChanged.getValue();
		
		//Add the action if the values differ
		if(oldValue != newValue) {
			try {
				//Add the action
				actionLog.AddAction(
						oldValue,
						newValue,
						sliderChanged,
						sliderChanged.getClass().getMethod("setValue", double.class),
						this.getClass().getMethod("updateEnvelope", double.class));
			} catch (NoSuchMethodException | SecurityException e1) {
				System.out.println("An unexpected error has occured.");
				e1.printStackTrace();
			}	
		}			
	}
	
	public void updateEnvelope(double value) {
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
	
	@FXML
	public void onChordButtonClicked(MouseEvent e) {
		Button note = (Button)e.getSource();
		
		ObservableList<String> classes = note.getStyleClass();

		int noteColumn = GridPane.getColumnIndex(note);
		int noteRow = GridPane.getRowIndex(note);

		/* If the note was selected before...
		 *  1. Un-highlight it
		 *  2. Reset arrays to null values
		 */
		if(classes.contains("selected")) {
			classes.remove("selected");
			selectedNotes[noteColumn] = null;
			noteFrequencies[noteColumn] = -1.0;

		//Otherwise, check if there is already a selected note in the same column
		} else {
			// If there is, un-highlight the other note and replace the values in the arrays
			if(selectedNotes[noteColumn] != null) {
				selectedNotes[noteColumn].getStyleClass().remove("selected");

				classes.add("selected");
				selectedNotes[noteColumn] = note;
				noteFrequencies[noteColumn] = Utils.Math.getKeyFrequency(88 - noteRow);

			// Otherwise, simply highlight the note and add it to the arrays
			} else {
				classes.add("selected");
				selectedNotes[noteColumn] = note;
				noteFrequencies[noteColumn] = Utils.Math.getKeyFrequency(88 - noteRow);
			}
		}
	}
	
	@FXML
	public void onChordButtonDragged(MouseEvent e) {
		System.out.println("HERE");
		if(e.isControlDown()) {
			Button chord = (Button)e.getSource();
			
			ObservableList<String> classes = chord.getStyleClass(); 
			
			if(classes.contains("selected"))
				classes.remove("selected");
			else
				classes.add("selected");	
		}		
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
			case P:
				synth.playSong(noteFrequencies);
			default:
				System.out.println("There is no special function for this character sequence.");
			}
		}
	}
}
