package chocolate_chiptunes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;

public class Controller {

	Synthesizer synth = new Synthesizer();

	int selectedInstrumentID = 0;
	
	@FXML
	private ScrollPane pianoRoll;
	
	@FXML
	private ScrollPane arrangementEditor;

	@FXML
	private Slider attackSlider;

	@FXML
	private Slider decaySlider;

	@FXML
	private Slider sustainSlider;

	@FXML
	private Slider releaseSlider;

	@FXML
	private Button addInstrument;

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
		synth.setSelectedInstrument(selectedInstrumentID);

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

		synth.resetOut();
		synth.getSelectedInstrument().updateEnvelope(envelopeData);

	}

	@FXML
	public void onInstrumentButtonClick() {

	}
}
