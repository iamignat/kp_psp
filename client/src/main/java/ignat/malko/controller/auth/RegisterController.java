package ignat.malko.controller.auth;

import ignat.malko.handler.UserHandler;
import ignat.malko.model.PersonData;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.User;
import ignat.malko.model.enums.RequestType;
import ignat.malko.model.enums.ResponseType;
import ignat.malko.model.enums.Role;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML
    private Label errorLabel;
    @FXML
    private RadioButton otherRadioButton;
    @FXML
    private Button loginButton;
    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField repeatPasswordField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private Spinner<Integer> ageSpinner;
    private final SpinnerValueFactory<Integer> valueMarkFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(14, 80);
    private final ToggleGroup group = new ToggleGroup();
    private final UserHandler userHandler = new UserHandler();

    public void onRegister() throws IOException {
        if (loginTextField.getText().isEmpty() || passwordField.getText().isEmpty() || repeatPasswordField.getText().isEmpty() || lastNameTextField.getText().isEmpty() || firstNameTextField.getText().isEmpty() || emailTextField.getText().isEmpty() || phoneNumberTextField.getText().isEmpty()) {
            errorLabel.setVisible(true);
            errorLabel.setText("Все поля должны быть заполнены");
        } else if (! emailTextField.getText().matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            errorLabel.setVisible(true);
            errorLabel.setText("Некорректный email");
        } else if (! phoneNumberTextField.getText().matches("^\\+[0-9]{12}$")) {
            errorLabel.setVisible(true);
            errorLabel.setText("Некорректный номер телефона");
        } else if (! passwordField.getText().equals(repeatPasswordField.getText())) {
            errorLabel.setVisible(true);
            errorLabel.setText("Пароли не совпадают");
        } else if (! maleRadioButton.isSelected() && ! femaleRadioButton.isSelected() && ! otherRadioButton.isSelected()) {
            errorLabel.setVisible(true);
            errorLabel.setText("Выберите пол");
        } else {
            User user = getUser();
            Response response = userHandler.handle(RequestType.REGISTER, user);
            if (response.getResponseType() == ResponseType.OK) {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/auth/Login.fxml")));
                Scene newScene = new Scene(root);
                stage.setScene(newScene);
            } else {
                errorLabel.setVisible(true);
                errorLabel.setText(response.getResponseMessage());
            }
        }

    }

    private User getUser() {
        User user = new User();
        user.setLogin(loginTextField.getText());
        user.setPassword(passwordField.getText());
        user.setRole(Role.USER);

        PersonData personData = new PersonData();
        personData.setFirstName(firstNameTextField.getText());
        personData.setLastName(lastNameTextField.getText());
        personData.setAge(ageSpinner.getValue());
        personData.setEmail(emailTextField.getText());
        personData.setPhoneNumber(phoneNumberTextField.getText());
        personData.setSex(maleRadioButton.isSelected() ? "Мужской" : femaleRadioButton.isSelected() ? "Женский" : "Другой");
        user.setPersonData(personData);
        return user;
    }

    public void onLogin() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/auth/Login.fxml")));
        stage.setScene(new Scene(root));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        valueMarkFactory.setValue(14);
        ageSpinner.setValueFactory(valueMarkFactory);
        maleRadioButton.setToggleGroup(group);
        femaleRadioButton.setToggleGroup(group);
        otherRadioButton.setToggleGroup(group);
    }

    public void onEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            try {
                onRegister();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
