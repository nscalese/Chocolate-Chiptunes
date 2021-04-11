package chocolate_chiptunes;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.*;


public class Authenticated {
    static boolean answer;

    public static boolean display(String title, String message) {
        Authenticator helper = new Authenticator();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 500, 500);

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.getIcons().add(new Image(Authenticated.class.getClassLoader().getResourceAsStream("chip_icon.jpg")));
        window.setMinWidth(250);
//        Label label = new Label();
//        label.setText(message);
        Label scenetitle = new Label();
        scenetitle.setText(message);
        scenetitle.setStyle("-fx-text-fill: #227200; -fx-font-size: 16px;");

        grid.add(scenetitle, 0, 0, 2, 1);
        window.setScene(scene);
        window.showAndWait();
        return true;
    }
}
