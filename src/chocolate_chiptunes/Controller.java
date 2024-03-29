package chocolate_chiptunes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Controller {
	private Stage stage;
	
	private File projectFile;
	
	private ActionLog actionLog;

	private Synthesizer synth;

	private int selectedInstrumentID;
	
	private Gson gson;
	
	private FileChooser fileChooser;

	private double[] noteFrequencies;
	private Button[] selectedNotes;

	@FXML
	private GridPane mainGrid;

	@FXML
	private GridPane chordsGrid;

	@FXML
	private ScrollPane pianoRoll;

	@FXML
	private ScrollPane arrangementEditor;

	@FXML
	private Button btnSignin;

	@FXML
	private Button btnSignup;

	@FXML
	private Label welcomelbl;

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

	@FXML
	private Slider volumeSlider;

	/**
	 * This function instantiates the controller object, setting values like the ActionLog and the GSON serializer
	 *
	 * @return nothing
	 */
	public Controller() {
		//Set the file to null as default
		projectFile = null;
		
		//Set the action log
		actionLog = new ActionLog(this);
		
		//Set the selected instrument ID
		selectedInstrumentID = 0;
		
		//Set the gson reader/writer
		gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();
		
		//Set the File Chooser
		fileChooser = new FileChooser();
		
		fileChooser.setTitle("Save Project");
		
		fileChooser.getExtensionFilters().add(new ExtensionFilter("JSON", "*.json"));
				
		//Set note frequencies
		noteFrequencies = new double[]{-1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0};
		
		//Set selected notes
		selectedNotes = new Button[] {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
	}

	/**
	 * This function sets controller's stage value (for the filechooser)
	 *
	 * @param stage - the stage associated with the controller
	 * @return nothing
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * This function sets the controller's synth value
	 *
	 * @param synth - the synth object associated with the controller
	 * @return nothing
	 */
	public void setSynth(Synthesizer synth) {
		this.synth = synth;
	}

	/**
	 * This function shows the piano roll when the piano roll button is pressed
	 *
	 * @param event - the action event object containing information about the particular action event
	 * @return nothing
	 */
	@FXML
	public void showPianoRoll(ActionEvent event) {
		pianoRoll.setVisible(true);
		arrangementEditor.setVisible(false);
	}

	/**
	 * This function shows the arrangement when the arrangement button is pressed
	 *
	 * @param event - the action event object containing information about the particular action event
	 * @return nothing
	 */
	@FXML
	public void showArrangementEditor(ActionEvent event) {
		pianoRoll.setVisible(false);
		arrangementEditor.setVisible(true);
	}

	/**
	 * This function determines what to do when one of the BPM buttons is clicked (increment/decrement)
	 *
	 * @param e - the mouse event object containing information about the particular mouse event
	 * @return nothing
	 */
	public void onBPMButtonClick(MouseEvent e) {
		int bpmValue = Integer.parseInt(bpmLabel.getText());

		//Grab the button's ID
		String buttonID = ((Button)e.getSource()).getId();

		try {
			//Add the action
			actionLog.AddAction(
					String.valueOf(bpmValue),
					String.valueOf(buttonID.equals("incrementBPMButton") ? ++bpmValue : --bpmValue),
					bpmLabel,
					bpmLabel.getClass().getMethod("setText", String.class),
					this.getClass().getMethod("changeBPM", String.class));
		} catch (NoSuchMethodException | SecurityException e1) {
			System.out.println("An unexpected error has occured. - BPM");
			e1.printStackTrace();
		}

		//Change the label
		bpmLabel.setText(String.valueOf(bpmValue));

		//Change the BPM value
		changeBPM(String.valueOf(bpmValue));
	}

	/**
	 * This function changes the BPM in the synth
	 *
	 * @param bpmValue - the bpm value to update the synth to. Is a string for semantics purposes with the ActionLog
	 * @return nothing
	 */
	public void changeBPM(String bpmValue) {
		synth.setBPM(Integer.parseInt(bpmValue));
	}

	/**
	 * This function adds a new instrument when the add instrument button is clicked
	 *
	 * @param e - the mouse event object containing information about the particular mouse event
	 * @return nothing
	 */
	@FXML
	public void onAddInstrumentClick(MouseEvent e) {
		// Get the current instrument count
		int instrumentCount = synth.getInstrumentCount();

		// If there are 4 instruments or more, move on
		if(instrumentCount < 4) {
			//Increment the count
			instrumentCount++;

			// Create a new Instrument button and add the necessary functionality
			Button newInstrument = new Button("Instrument " + instrumentCount);

			//Set the instrument number in the user data (0 based)
			newInstrument.setUserData(instrumentCount - 1);

			//Set the instruments id based on the instrument count (1 based)
			newInstrument.setId("instrument" + instrumentCount);

			//Set the instruments on click event
			newInstrument.setOnMouseClicked(event -> {
				onInstrumentButtonClick(event);
			});

			//Set the new instruments max height and width, based on the pre-existing add instrument button
			newInstrument.setMaxHeight(btnAddInstrument.getMaxHeight());
			newInstrument.setMaxWidth(btnAddInstrument.getMaxWidth());

			//Add the instrument-button class to the instruments style class list
			newInstrument.getStyleClass().add("instrument-button");

			//Attempt to add this action to the action log (using the InstrumentButton class for the old/new values)
			try {
				InstrumentButton oldInstrumentButton = new InstrumentButton(), newInstrumentButton = new InstrumentButton();

				//Set the old instrument button fields
				oldInstrumentButton.setInstrumentButton(newInstrument);
				oldInstrumentButton.setExists(false);

				//Set the new instrument button fields
				newInstrumentButton.setInstrumentButton(newInstrument);
				newInstrumentButton.setExists(true);

				//Add the action
				actionLog.AddAction(
						oldInstrumentButton,
						newInstrumentButton,
						newInstrument,
						null,
						this.getClass().getMethod("alterInstrument", InstrumentButton.class));
			} catch (NoSuchMethodException | SecurityException e1) {
				System.out.println("An unexpected error has occured.");
				e1.printStackTrace();
			}

			//Add the instrument
			addInstrument(newInstrument);
		} else {
			System.out.println("Max number of instruments created.");
		}
	}

	public void alterInstrument(InstrumentButton instrumentButton){
		if(instrumentButton.getExists())
			addInstrument(instrumentButton.getInstrumentButton());
		else
			removeInstrument(instrumentButton.getInstrumentButton());
	}

	public void addInstrument(Button buttonToAdd) {
		int instrumentCount = synth.getInstrumentCount();

		// If there are 4 instruments or more, move on
		if(instrumentCount < 4) {
			// Create the instrument and increment the count
			synth.createInstrument();
			instrumentCount++;

			// Make sure the instrument is added to the list correctly
			instrumentList.getChildren().remove(btnAddInstrument);
			instrumentList.add(buttonToAdd, 0, instrumentCount - 1);
			instrumentList.add(btnAddInstrument, 0, instrumentCount);
		}
	}

	public void removeInstrument(Button buttonToRemove) {
		// Get the current instrument count
		int instrumentCount = synth.getInstrumentCount();

		// If there are at least 2 instruments, move on
		if(instrumentCount > 0) {
			// Create the instrument and increment the count
			synth.removeInstrument();
			instrumentList.getChildren().remove(buttonToRemove);

			// Make sure the add instrument is re-added to the list correctly
			instrumentList.getChildren().remove(btnAddInstrument);
			instrumentList.add(btnAddInstrument, 0, --instrumentCount);
		}
	}


	/**
	 * This function calls the signin function when the sign in button is clicked
	 *
	 * @param e - the mouse event object containing information about the particular mouse event
	 * @return nothing
	 */
	public void onSignInClick(MouseEvent e) {
		signin();
	}


	/**
	 * This function displays the sign in form
	 *
	 * @return nothing
	 */
	public void signin() {
		Stage window = new Stage();
		//Scene scene = new Scene(primarygridpane,400,500);
		SigninForm signin = new SigninForm();
		signin.start(window);
		signin.isLogged(this);
	}


	public void setBtnSignin(boolean invisible){
		btnSignin.setVisible(invisible);
	}

	public void setBtnSignup(boolean invisible){
		btnSignup.setVisible(invisible);
	}

	public void setWelLabel(String lbltest){
		welcomelbl.setText(lbltest);
	}
	/**
	 * This function calls the signup function when the signup button is clicked
	 *
	 * @param e - the mouse event object containing information about the particular mouse event
	 * @return nothing
	 */
	public void onSignupClick(MouseEvent e){signup();}

	/**
	 * This function displays the sign up form
	 *
	 * @param  - the mouse event object containing information about the particular mouse event
	 * @return nothing
	 */
	public void signup(){
		boolean result = SignUpForm.display("Chocolate Chiptunes - Sign Up", "Sign-Up Form");
		System.out.println(result);
	}

	/**
	 * This function hooks up the selected instrument to the synth, and sets the GUI's slider/waveform based on the selected instrument
	 *
	 * @param e - the mouse event object containing information about the particular mouse event
	 * @return nothing
	 */
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

	/**
	 * This function determines what to do when one of the waveform radio buttons is clicked
	 *
	 * @param e - the mouse event object containing information about the particular mouse event
	 * @return nothing
	 */
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

	/**
	 * This function changes the waveform selected in the synth
	 *
	 * @param waveform - the radiobutton used to change the synth's waveform (contains the waveformID)
	 * @return nothing
	 */
	public void changeWaveform(Toggle waveform) {
		synth.disconnectInstrument();
		// Retrieve the waveform ID and set the waveform of the instrument
		int selectedWaveformID = Integer.parseInt(waveform.getUserData().toString());
		synth.getSelectedInstrument().setWaveform(selectedWaveformID);

		synth.connectInstrument();
	}

	/**
	 * This function determines what to do when any of the instrument sliders are changed
	 *
	 * @param e - the mouse event object containing information about the particular mouse event
	 * @return nothing
	 */
	@FXML
	public void onInstrumentSliderChanged(MouseEvent e) {
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

		//Add the action if the values differ and the slider index is valid
		if(oldValue != newValue && sliderIndex != -1) {
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

			updateEnvelope(0);
		}
	}

	/**
	 * This function determines what to do when the volume slider is changed
	 *
	 * @param e - the mouse event object containing information about the particular mouse event
	 * @return nothing
	 */
	@FXML
	public void onVolumeSliderChanged(MouseEvent e){
		//Grab the affected slider
		Slider sliderChanged = (Slider) e.getSource();

		//Grab old/new slider values
		double oldValue = synth.getVolume(),
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
						this.getClass().getMethod("changeVolume", double.class));
			} catch (NoSuchMethodException | SecurityException e1) {
				System.out.println("An unexpected error has occured.");
				e1.printStackTrace();
			}

			changeVolume(newValue);
		}
	}

	/**
	 * This function changes the master volume of the synth
	 *
	 * @param volume - the volume to change the synth's master volume to
	 * @return nothing
	 */
	public void changeVolume(double volume){
		synth.changeVolume(volume);
	}

	/**
	 * This function takes the values from all instrument sliders and updates the currently selected intrument's envelope
	 *
	 * @param value - has no purpose, just a semantic value for the ActionLog
	 * @return nothing
	 */
	public void updateEnvelope(double value) {
	// Get the values of the individual sliders
		double attackValue = (double) attackSlider.getValue();
		double decayValue = (double) decaySlider.getValue();
		double sustainValue = (double) sustainSlider.getValue();
		double releaseValue = (double) releaseSlider.getValue();
		System.out.println("Attack value: " + attackValue);

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
	 * This function handles selecting and unselecting notes in the piano roll
	 *
	 * If a note is selected in one row, and another note is selected in the same row, the first note will be unselected
	 *
	 * @param e - the mouse event object containing information about the particular mouse event
	 * @return nothing
	 */
	@FXML
	public void onNoteButtonClicked(MouseEvent e) {
		//Grab the note button from the source
		Button note = (Button)e.getSource();

		//Grab the note button's column in the gridpane
		int noteColumn = GridPane.getColumnIndex(note);

		//Grab the style classes for reuse
		ObservableList<String> styleClasses = note.getStyleClass();

		//Initialize old/new note buttons
		NoteButton oldNoteButton = new NoteButton(), newNoteButton = new NoteButton();

		/* If the note was selected before...
		 *  1. Un-highlight it
		 *  2. Reset arrays to null values
		 */
		if(styleClasses.contains("selected")) {
			oldNoteButton.setNoteButton(note);
			oldNoteButton.setSelected(true);

			newNoteButton.setNoteButton(note);
			newNoteButton.setSelected(false);

			//Otherwise, check if there is already a selected note in the same column
		} else {

			newNoteButton.setNoteButton(note);
			newNoteButton.setSelected(true);

			// If there is, un-highlight the other note
			if(selectedNotes[noteColumn] != null) {
				oldNoteButton.setNoteButton(selectedNotes[noteColumn]);
				oldNoteButton.setSelected(true);
			} else {
				oldNoteButton.setNoteButton(note);
				oldNoteButton.setSelected(false);
			}
		}

		try {
			//Add the action
			actionLog.AddAction(
					oldNoteButton,
					newNoteButton,
					note,
					null,
					this.getClass().getMethod("updateNote", NoteButton.class));
		} catch (NoSuchMethodException | SecurityException e1) {
			System.out.println("An unexpected error has occured.");
			e1.printStackTrace();
		}

		//Update the note
		updateNote(newNoteButton);
	}

	public void updateNote(NoteButton noteButton){
		//Grab the button from the note
		Button note = noteButton.getNoteButton();

		//Grab the style classes for reuse
		ObservableList<String> styleClasses = note.getStyleClass();

		//Grab the note buttons row/column in the gridpane
		int noteColumn = GridPane.getColumnIndex(note);
		int noteRow = GridPane.getRowIndex(note);

		if(!noteButton.selected) {
			styleClasses.remove("selected");
			selectedNotes[noteColumn] = null;
			noteFrequencies[noteColumn] = -1.0;

			//Otherwise, check if there is already a selected note in the same column
		} else {
			// If there is, un-highlight the other note
			if(selectedNotes[noteColumn] != null) {
				selectedNotes[noteColumn].getStyleClass().remove("selected");
			}

			//Set the values in the array
			styleClasses.add("selected");
			selectedNotes[noteColumn] = note;
			noteFrequencies[noteColumn] = Utils.Math.getKeyFrequency(88 - noteRow);
		}
	}

	/**
	 * This function determines what to do based on the keys pressed while on the gridpane (piano adapter, ctrl functions, etc.)
	 *
	 * @param e - the key event object containing information about the particular key event
	 * @return nothing
	 */
	@FXML
	public void onGridKeyPressed(KeyEvent e) {

		KeyCode keyCode = e.getCode();

		//Begin special function processing
		if(e.isControlDown()) {
			switch(keyCode) {
			case Z:
				actionLog.Undo();
				break;
			case Y:
				actionLog.Redo();
				break;
			case S:
				if(e.isShiftDown())
					saveFile(true);
				else
					saveFile(false);
				break;
			case P:
				synth.playSong(noteFrequencies);
				break;
			case O:
				loadFile();
				break;
			default:
				System.out.println("There is no special function for this character sequence.");
			}
		}
	}

	/**
	 * This function calls the saveFile function when the save button is pressed
	 *
	 * @param e - the action event object containing information about the particular action event
	 * @return nothing
	 */
	public void onSaveButtonClick(ActionEvent e){
		saveFile(false);
	}

	/**
	 * This function calls the loadFile function when the load button is pressed
	 *
	 * @param e - the action event object containing information about the particular action event
	 * @return nothing
	 */
	public void onLoadButtonCLick(ActionEvent e){
		loadFile();
	}

	/**
	 * This function saves the project to a JSON file
	 *
	 * @param newFile - Whether or not to save the project as a new file
	 * @return nothing
	 */
	public void saveFile(boolean newFile) {
		if(projectFile == null || newFile)
			projectFile = fileChooser.showSaveDialog(stage);

		if(projectFile != null){
			//Convert the synth object to JSON format
			String projectJson = gson.toJson(new Project(synth.getInstruments(), synth.getBPM(), synth.getVolume(), noteFrequencies), Project.class);

			try {
				Files.writeString(Path.of(projectFile.getAbsolutePath()), projectJson, StandardOpenOption.CREATE);
			} catch (IOException ignored) {}
		}
	}

	/**
	 * This function loads a project JSON file and modifies the GUI/Synth to reflect the correct project state
	 *
	 * @return nothing
	 */
	public void loadFile() {
		File file = fileChooser.showOpenDialog(stage);
		
		if(file != null && file.getName().endsWith(".json")) {
			try {
				//Grab the json text from the supplied json file
				String projectJson = Files.readString(Path.of(file.getAbsolutePath()));

				//Deserialize the Json and construct the Project object
				Project project = gson.fromJson(projectJson, Project.class);

				//If project successfully instantiated, continue on
				if(project != null){
					//Set the current project file to the file opened
					projectFile = file;

					//Grab the instruments list
					ObservableList<Node> instrumentsList = instrumentList.getChildren();

					//Remove the instruments from the grid and the synth
					for(int i = 0; i < instrumentsList.size(); i++ ){
						if(instrumentsList.get(i).getId() != "btnAddInstrument")
							removeInstrument((Button) instrumentsList.get(i));
					}

					//Add each instrument and update its envelope
					for(Project.ProjectInstrument projInstrument : project.getInstruments()) {
						//Only run for non-null instrument entries
						if(projInstrument != null){
							onAddInstrumentClick(null);

							synth.setSelectedInstrument(synth.getInstrumentCount() - 1);

							ToggleButton waveformButton = null;

							switch(projInstrument.getWaveformID()){
								case Instrument.SINE_WAVE:
									waveformButton = sineButton;
									break;
								case Instrument.SQUARE_WAVE:
									waveformButton = squareButton;
									break;
								case Instrument.TRIANGLE_WAVE:
									waveformButton = triangleButton;
									break;
								case Instrument.SAW_WAVE:
									waveformButton = sawButton;
									break;
							}

							//Toggle the waveform in the UI
							waveformGroup.selectToggle(waveformButton);

							//Change it in the instrument
							changeWaveform(waveformButton);

							//Grab the envelope data from the project instrument
							double[] envelopeData = projInstrument.getEnvelopeData();

							//Set the appropriate slider values in the UI
							attackSlider.setValue(envelopeData[Instrument.ATTACK_VALUE]);
							decaySlider.setValue(envelopeData[Instrument.DECAY_VALUE]);
							sustainSlider.setValue(envelopeData[Instrument.SUSTAIN_VALUE]);
							releaseSlider.setValue(envelopeData[Instrument.RELEASE_VALUE]);

							//Update the instrument's envelope
							updateEnvelope(0);
						}
					}

					//Grab the master volume from the project
					double masterVolume = project.getMasterVolume();

					//Change it in the UI
					volumeSlider.setValue(masterVolume);

					//Update it in the synth
					changeVolume(masterVolume);

					//Grab the BPM form the project and parse it as a string
					String bpm = String.valueOf(project.getBpm());

					//Set the BPM in the UI
					bpmLabel.setText(bpm);

					//Update it in the synth
					changeBPM(bpm);

					//Unhighlight all notes
					chordsGrid.lookupAll(".selected").forEach(node -> {
						node.getStyleClass().remove("selected");
					});

					//Set the note frequencies from the project
					noteFrequencies = project.getNoteFrequencies();

					//Highlight the selected notes and add them to the selectedNotes array
					for(int i = 0; i < noteFrequencies.length; i++){
						if(noteFrequencies[i] > -1){
							int noteRow = 88 - Utils.Math.getKey(noteFrequencies[i]);

							for(Node note : chordsGrid.getChildren()){
								Integer gridRow = GridPane.getRowIndex(note), gridCol = GridPane.getColumnIndex(note);
								if(gridRow != null && gridCol != null && GridPane.getRowIndex(note) == noteRow && GridPane.getColumnIndex(note) == i){
									note.getStyleClass().add("selected");
									selectedNotes[i] = (Button)note;
								}
							}
						}
					}

					//Reset the action log
					actionLog.emptyLog();
				}
			} catch (IOException ignored) {}
		}
	}

	/**
	 * Note Button class that contains the button clicked and it's selected state
	 */
	public class NoteButton {
		private Button noteButton;
		private boolean selected;

		public Button getNoteButton(){
			return noteButton;
		}

		public void setNoteButton(Button button){
			this.noteButton = button;
		}

		public boolean getSelected(){
			return selected;
		}

		public void setSelected(boolean selected){
			this.selected = selected;
		}
	}

	/**
	 * Instrument Button class that contains the button clicked and it's existing state
	 */
	public class InstrumentButton {
		private Button instrumentButton;
		private boolean exists;

		public Button getInstrumentButton(){
			return instrumentButton;
		}

		public void setInstrumentButton(Button button){
			this.instrumentButton = button;
		}

		public boolean getExists(){
			return exists;
		}

		public void setExists(boolean exists){
			this.exists = exists;
		}
	}
}
