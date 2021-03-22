package chocolate_chiptunes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class Controller {

	Synthesizer synth = new Synthesizer();

	int selectedInstrumentID = 0;
	
	@FXML
	private ScrollPane pianoRoll;
	
	@FXML
	private ScrollPane arrangementEditor;

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
	private Button btnAddInstrument;

	@FXML
	private GridPane instrumentList;

	@FXML
	public void showPianoRoll(ActionEvent event) {
		pianoRoll.setVisible(true);
		arrangementEditor.setVisible(false);
	}
	
	@FXML
	public void showArrangementEditor(ActionEvent event) {
		pianoRoll.setVisible(false);
		arrangementEditor.setVisible(true);
	}

	@FXML
	public void onSliderChanged() {
		double attackValue = (double) attackSlider.getValue();
		double decayValue = (double) decaySlider.getValue();
		double sustainValue = (double) sustainSlider.getValue();
		double releaseValue = (double) releaseSlider.getValue();

		double[] envelopeData = {
				attackValue, 1.0,
				decayValue, 0.6,
				sustainValue, 0.6,
				releaseValue, 0.0
		};

		synth.getSelectedInstrument().updateEnvelope(envelopeData);
	}

	@FXML
	public void onAddInstrumentClick(MouseEvent e) {
		int instrumentCount = synth.getInstrumentCount();

		if(instrumentCount < 4) {
			synth.createInstrument();
			instrumentCount++;

			Button newInstrument = new Button("Instrument " + instrumentCount);
			newInstrument.setUserData(instrumentCount - 1);
			newInstrument.setId("instrument" + instrumentCount);
			newInstrument.setOnMouseClicked(event -> {
				onInstrumentButtonClick(event);
			});
			newInstrument.setMaxHeight(btnAddInstrument.getMaxHeight());
			newInstrument.setMaxWidth(btnAddInstrument.getMaxWidth());

			instrumentList.getChildren().remove(btnAddInstrument);
			instrumentList.add(newInstrument, 0, instrumentCount - 1);
			instrumentList.add(btnAddInstrument, 0, instrumentCount);
		} else {
			System.out.println("Max instruments");
		}

	}

	@FXML
	public void onInstrumentButtonClick(MouseEvent e) {
		Object node = e.getSource();
		Button instrument = (Button)node;

		selectedInstrumentID = Integer.parseInt(instrument.getUserData().toString());
		synth.setSelectedInstrument(selectedInstrumentID);

		double[] envelopeData = synth.getSelectedInstrument().getEnvelopeData();
		attackSlider.setValue(envelopeData[Instrument.ATTACK_VALUE]);
		System.out.println("Attack: " + envelopeData[Instrument.ATTACK_VALUE]);
		decaySlider.setValue(envelopeData[Instrument.DECAY_VALUE]);
		sustainSlider.setValue(envelopeData[Instrument.SUSTAIN_VALUE]);
		releaseSlider.setValue(envelopeData[Instrument.RELEASE_VALUE]);
	}
}
