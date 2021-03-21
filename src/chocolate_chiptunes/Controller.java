package chocolate_chiptunes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

public class Controller {
	
	@FXML
	private ScrollPane pianoRoll;
	
	@FXML
	private ScrollPane arrangementEditor;
	
	@FXML
	private Label bpm;

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
	public void incrementBPM() {
		int bpmValue = Integer.parseInt(bpm.getText()), newBPMValue = bpmValue + 1;		
		
		bpm.setText(String.valueOf(newBPMValue));
	}
	
	@FXML
	public void decrementBPM() {
		int bpmValue = Integer.parseInt(bpm.getText()), newBPMValue = bpmValue - 1;
		
		
		bpm.setText(String.valueOf(newBPMValue));
	}	
}
