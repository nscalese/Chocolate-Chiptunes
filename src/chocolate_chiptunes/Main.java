//This is the main JavaFX file
package chocolate_chiptunes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	//Start method which creates the application UI and displays it
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("chocolate_chiptunes.fxml"));
		primaryStage.setTitle("Chocolate Chiptunes");
		primaryStage.setScene(new Scene(root));
		primaryStage.setMaximized(true);
		primaryStage.show();

	}
	
	//Main function which invokes JavaFX
	public static void main(String[] args) {
		launch(args);
	}
}
