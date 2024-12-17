package ignat.malko.controller.auth;

import com.google.gson.Gson;
import ignat.malko.handler.UserHandler;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.User;
import ignat.malko.model.enums.RequestType;
import ignat.malko.model.enums.ResponseType;
import ignat.malko.util.ClientSocket;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;

    private final UserHandler userHandler = new UserHandler();

    public void onLogin() throws IOException {
        if (!loginTextField.getText().isEmpty() || !passwordField.getText().isEmpty()){
            User user = new User();
            user.setLogin(loginTextField.getText());
            user.setPassword(passwordField.getText());
            Response response = userHandler.handle(RequestType.LOGIN, user);
            if (response.getResponseType() == ResponseType.OK) {
                ClientSocket.getInstance().setUser(new Gson().fromJson(response.getResponseData(), User.class));
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/client/Client.fxml")));
                stage.setScene(new Scene(root));
            } else {
                errorLabel.setVisible(true);
                errorLabel.setText(response.getResponseMessage());
            }
        }
        else {
            errorLabel.setVisible(true);
            errorLabel.setText("Все поля должны быть заполнены");
        }
    }

    public void onRegister() throws IOException{
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/auth/Register.fxml")));
        stage.setScene(new Scene(root));
    }

    public void onEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            try {
                onLogin();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}

