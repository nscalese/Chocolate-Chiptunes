package chocolate_chiptunes;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class PasswordResetForm {
    static boolean answer;

    public static boolean display(String title, String message, String username) {
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
        TextField txtusername = new TextField();
        txtusername.setPromptText("Email");
        txtusername.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        txtusername.setText(username);
        grid.add(txtusername, 0, 1);


        // PIN field
        TextField txtresetcode = new TextField();
        txtresetcode.setPromptText("Reset code");
        txtresetcode.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        grid.add(txtresetcode, 0, 2);

        // password field
        TextField txtpassword = new PasswordField();
        txtpassword.setPromptText("New Password");
        txtpassword.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        grid.add(txtpassword, 0, 3);



        //Create signup button
        Button resetPassword = new Button("Reset Password");
        HBox suBtn = new HBox(10);
        //       suBtn.setAlignment(Pos.BOTTOM_LEFT);
        suBtn.getChildren().add(resetPassword);
        suBtn.setMaxWidth(190);
        grid.add(suBtn,0,6);
        // Create cancel button
        Button cancelButton = new Button("Cancel");
        HBox clBtn = new HBox(10);
        clBtn.setAlignment(Pos.BOTTOM_RIGHT);
        clBtn.getChildren().add(cancelButton);
        suBtn.setMaxWidth(190);
        grid.add(clBtn,1,6 );
        Label lblmessage = new Label();

        grid.add(lblmessage, 0, 7, 2, 1);
        //Clicking will set answer and close window
        resetPassword.setOnAction(e -> {
            answer = true;
            try {
                helper.UpdatePassword(txtusername.getText(), txtpassword.getText(), txtresetcode.getText());
                lblmessage.setText("Password reset successfully!");
                lblmessage.setStyle("-fx-text-fill: #e30b0b; -fx-font-size: 16px;");
            }catch (Exception exp)
            {
                System.out.println(exp);
                answer=false;

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

}
