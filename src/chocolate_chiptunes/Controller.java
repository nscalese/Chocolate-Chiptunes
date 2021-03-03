package chocolate_chiptunes;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {

	@FXML
	private Label label;
	
	public void initialize() {
		label.setText("This is the start of our JavaFX project.\nRight now, it's just a measely label, but we'll have a full fledged UI soon enough.");
	}
}
