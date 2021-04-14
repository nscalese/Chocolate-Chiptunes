package chocolate_chiptunes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SignUpConformation {
    //Create variable


    static boolean display(String title, String message, String username) {
        boolean answer=false;
        Authenticator helper = new Authenticator();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid,400, 500);

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label scenetitle = new Label();
        scenetitle.setText(message);

        grid.add(scenetitle, 0, 0, 2, 1);
        // Username field
        TextField otpcode = new TextField();
        otpcode.setPromptText("Enter verification code");
        otpcode.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        grid.add(otpcode, 1, 1);

        //Create signup button
        Button submutBtn = new Button("Submit");
        HBox suBtn = new HBox(10);
        suBtn.getChildren().add(submutBtn);
        suBtn.setMaxWidth(190);
        grid.add(suBtn,0,6);
        // Create cancel button
        Button cancelButton = new Button("Cancel");
        HBox clBtn = new HBox(10);
        clBtn.setAlignment(Pos.BOTTOM_RIGHT);
        clBtn.getChildren().add(cancelButton);
        clBtn.setMaxWidth(190);
        grid.add(clBtn,1,6);
        Label otp_message = new Label();

        grid.add(otp_message, 0, 7, 2, 1);
        //Clicking will set answer and close window
        submutBtn.setOnAction(e -> {
            boolean success=helper.VerifyAccessCode(username, otpcode.getText());
            if (success){
                System.out.println("You have been verified is successful");
                otp_message.setText("OTP validation is successful");
                otp_message.setStyle("-fx-text-fill: #21e30b; -fx-font-size: 16px;");

            }
            else {
                System.out.println("Verification Code is incorrect");
                otp_message.setText("Verification Code is incorrect. Please try again.");
                otp_message.setStyle("-fx-text-fill: #e30b0b; -fx-font-size: 16px;");
            }

        });
        cancelButton.setOnAction(e -> {
            window.close();
        });

        window.setScene(scene);
        window.showAndWait();

        return answer;
    }

}
