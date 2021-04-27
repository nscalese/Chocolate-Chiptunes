package chocolate_chiptunes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class SignUpForm {
    //Create variable
    static boolean answer;


    public static boolean display(String title, String message) {
        Authenticator helper = new Authenticator();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 500, 600);

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.getIcons().add(new Image(SignUpForm.class.getClassLoader().getResourceAsStream("chip_icon.jpg")));
        window.setMinWidth(250);
//        Label label = new Label();
//        label.setText(message);
        Label scenetitle = new Label();
        scenetitle.setText(message);
        scenetitle.setFont(Font.font("Courier", FontWeight.NORMAL, 30));
        grid.add(scenetitle, 0, 0, 3, 1);
        grid.setAlignment(Pos.CENTER);
        // Username field
        TextField Username = new TextField();
        Username.setPromptText("Email");
        Username.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        grid.add(Username, 0, 1);

        // password field
        TextField Password = new PasswordField();
        Password.setPromptText("Password");
        Password.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        grid.add(Password, 0, 2);


        // Repeat password field
        TextField Passwordr = new PasswordField();
        Passwordr.setPromptText("Confirm Password");
        Passwordr.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        //TextField Passwordr = new PasswordField();
        grid.add(Passwordr, 0, 3);

        // Nickname field
        TextField nickname = new TextField();
        nickname.setPromptText("Nickname");
        nickname.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        grid.add(nickname, 0, 4);
        //Create signup button
        Button signUpButton = new Button("Sign-Up");
        HBox suBtn = new HBox(10);

        suBtn.getChildren().add(signUpButton);
        suBtn.setMaxWidth(190);
        grid.add(suBtn, 0, 6);

        Button cancelButton = new Button("Cancel");
        HBox clBtn = new HBox(10);
        clBtn.setAlignment(Pos.BOTTOM_RIGHT);
        clBtn.getChildren().add(cancelButton);
        clBtn.setMaxWidth(190);
        grid.add(clBtn, 1, 6);


        /**
         * Password must contain a lower case letter
         * ✖ Password must contain an upper case letter
         * ✖ Password must contain a special character
         * ✓ Password must contain a number
         * ✖ Password must contain at least 8 characters
         */

        Label lowercase = new Label();
        lowercase.setText("Password must contain a lower case letter");

        Label uppercase = new Label();
        uppercase.setText("Password must contain an upper case letter");

        Label specialcase = new Label();
        specialcase.setText("Password must contain a special character");

        Label numbercase = new Label();
        numbercase.setText("Password must contain a number");

        Label eightcase = new Label();
        eightcase.setText("Password must contain at a length of 8 and no spaces");

        grid.add(lowercase, 0, 8);
        grid.add(uppercase, 0, 9);
        grid.add(specialcase, 0, 10);
        grid.add(numbercase, 0, 11);
        grid.add(eightcase, 0, 12);

        Password.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                boolean[] test = isLegalPassword(Password.getText());

                if (test[0]) {
                    uppercase.setStyle("-fx-text-fill: #4cff00; -fx-font-size: 16px;");
                } else {
                    uppercase.setStyle("-fx-text-fill: #e30b0b; -fx-font-size: 16px;");
                }

                if (test[1]) {
                    lowercase.setStyle("-fx-text-fill: #4cff00; -fx-font-size: 16px;");
                } else {
                    lowercase.setStyle("-fx-text-fill: #e30b0b; -fx-font-size: 16px;");
                }

                if (test[2]) {
                    numbercase.setStyle("-fx-text-fill: #4cff00; -fx-font-size: 16px;");
                } else {
                    numbercase.setStyle("-fx-text-fill: #e30b0b; -fx-font-size: 16px;");
                }

                if (test[3]) {
                    eightcase.setStyle("-fx-text-fill: #4cff00; -fx-font-size: 16px;");
                    ;
                } else {
                    eightcase.setStyle("-fx-text-fill: #e30b0b; -fx-font-size: 16px;");
                }

                if (test[4]) {
                    specialcase.setStyle("-fx-text-fill: #4cff00; -fx-font-size: 16px;");
                } else {
                    specialcase.setStyle("-fx-text-fill: #e30b0b; -fx-font-size: 16px;");
                }
            }
        });


        Label usercreation_message = new Label();
        usercreation_message.setFont(Font.font("Courier", FontWeight.NORMAL, 30));
        grid.add(usercreation_message, 0, 7, 2, 1);
        signUpButton.setOnAction(e -> {
            if (!Password.getText().equals(Passwordr.getText())) {

                usercreation_message.setText("Passwords are not the same");
                usercreation_message.setStyle("-fx-text-fill: #e30b0b; -fx-font-size: 16px;");

            }else{
                boolean success = helper.SignUpUser(Username.getText(), Password.getText(), Username.getText(), nickname.getText());
                if (success) {

                    System.out.println("Enter your validation code from email");
                    SignUpConformation.display("Chocolate Chiptunes", "Confirm User", Username.getText());

                    window.close();

                } else {
                        System.out.println("User creation failed");
                        usercreation_message.setText("User creation failed");

                    usercreation_message.setAlignment(Pos.CENTER);
                    usercreation_message.setStyle("-fx-text-fill: #e30b0b; -fx-font-size: 16px;");


                }
            }





        });
        cancelButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        window.setScene(scene);
        window.showAndWait();

        return answer;
    }


    private static boolean[] isLegalPassword(String pass) {
        boolean[] valid = new boolean[5];

        if (!pass.matches(".*[A-Z].*")) {
            valid[0] = false;
        } else {
            valid[0] = true;
        }

        if (!pass.matches(".*[a-z].*")) {
            valid[1] = false;
        } else {
            valid[1] = true;
        }

        if (!pass.matches(".*\\d.*")) {
            valid[2] = false;
        } else {
            valid[2] = true;
        }


        if (!pass.matches("\\S{8,99}")) {
            valid[3] = false;
        } else {
            valid[3] = true;
        }

        Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]");

        if (!regex.matcher(pass).find()) {
            valid[4] = false;
        } else {
            valid[4] = true;
        }

        return valid;
    }
}
