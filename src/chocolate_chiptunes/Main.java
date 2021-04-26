//This is the main JavaFX file
package chocolate_chiptunes;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.event.KeyAdapter;

public class Main extends Application {

	//Start method which creates the application UI and displays it
	@Override
	public void start(Stage primaryStage) throws Exception {
		Synthesizer synth = new Synthesizer();

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chocolate_chiptunes.fxml"));
		Parent root = (Parent)fxmlLoader.load();
		Controller controller = fxmlLoader.<Controller>getController();

		controller.setSynth(synth);

		primaryStage.setTitle("Chocolate Chiptunes");
		Scene scene = new Scene(root);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if(!keyEvent.isControlDown())
					synth.playNote(keyEvent.getCode().getChar().toLowerCase().charAt(0));
			}
		});

		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if(!keyEvent.isControlDown())
					synth.stopOut();
			}
		});

		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.show();

	}
	
	//Main function which invokes JavaFX
	public static void main(String[] args) {
		launch(args);
	}
}
