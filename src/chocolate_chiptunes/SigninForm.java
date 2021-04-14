package chocolate_chiptunes;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class SigninForm extends Application {

    Controller changelabel;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {

        Authenticator helper = new Authenticator();


        Stage window;
        Button signin_button;
        Button forgotpassword_but;


        window = primaryStage;
        window.setTitle("Chocolate Chiptunes");
        window.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("chip_icon.jpg")));
        VBox vb = new VBox();

        TextField username = new TextField();
        username.setPromptText("Username");
        username.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");

        HBox hbu = new HBox();
        hbu.getChildren().addAll(username);
        hbu.setAlignment(Pos.CENTER);

        TextField password = new PasswordField();
        password.setPromptText("Password");
        password.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        HBox hbp = new HBox();
        hbp.getChildren().addAll(password);
        hbp.setSpacing(10);
        hbp.setAlignment(Pos.CENTER);

        HBox hbButtons = new HBox(130);
        vb.setSpacing(20);
        Label image_label = new Label();
        Image image = new Image(getClass().getClassLoader().getResourceAsStream("chocolate_chiptunes.jpg"));
        image_label.setGraphic(new ImageView(image));
        vb.getChildren().add(image_label);
        hbButtons.setAlignment(Pos.CENTER);

        //System.out.println("TEST?");
        signin_button = new Button("Sign In");
        Label signin_label = new Label();
        signin_button.setOnAction((ActionEvent e) -> {
            String result = helper.ValidateUser(username.getText(), password.getText());
            if (result != null) {
                System.out.println(username.getText());
                String[] t = (String.valueOf(JWT_Parser.getPayload(result)).split(","));
                String[] nick = t[7].split(":");
                String nickname = nick[1].replace('\"',' ').strip();


                signin_label.setText("User is authenticated");
                signin_label.setStyle("-fx-text-fill: #4cff00; -fx-font-size: 16px;");
                //boolean auth = Authenticated.display("Chocolate Chiptunes", "Hello! " + username.getText() + " you are now logged in.");
                changelabel.setWelLabel("Hello! " + nickname + " you are now logged in.");
                changelabel.setBtnSignin(false);
                changelabel.setBtnSignup(false);
                window.close();
            } else {
                System.out.println("Username or Password is not correct (Wont say more for security reasons)");
                signin_label.setText("Username ot Password is not correct");
                signin_label.setStyle("-fx-text-fill: #e30b0b; -fx-font-size: 16px;");

            }

        });
        forgotpassword_but = new Button("Forgot password?");
        forgotpassword_but.setOnAction(e -> {
            boolean result = ForgotPasswordForm.display("Chocolate Chiptunes - Forgot Password", "Forgot password?");
            System.out.println(result);
        });
        signin_button.setMaxWidth(150);
        forgotpassword_but.setMaxWidth(150);
        hbButtons.getChildren().addAll(forgotpassword_but);
        vb.getChildren().addAll(hbu, hbp, signin_button,hbButtons, signin_label);
        vb.setAlignment(Pos.CENTER);


        Scene scene = new Scene(vb, 400, 600);
        window.setScene(scene);
        window.show();
    }
    public void isLogged(Controller test){
        changelabel = test;
    }

}
