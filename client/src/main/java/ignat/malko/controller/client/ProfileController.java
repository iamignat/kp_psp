package ignat.malko.controller.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ignat.malko.handler.UserHandler;
import ignat.malko.model.PersonData;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.User;
import ignat.malko.model.enums.RequestType;
import ignat.malko.model.enums.ResponseType;
import ignat.malko.model.enums.Role;
import ignat.malko.util.ClientSocket;
import ignat.malko.util.LocalDateTypeAdapter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private Label errorLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField repeatPasswordField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private Spinner<Integer> ageSpinner;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton otherRadioButton;
    private final SpinnerValueFactory<Integer> valueMarkFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(14, 80);
    final private ToggleGroup group = new ToggleGroup();
    private final UserHandler userHandler = new UserHandler();
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate .class, new LocalDateTypeAdapter()).create();

    public void onUpdate() throws IOException {
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
            Response response = userHandler.handle(RequestType.UPDATE_USER, user);
            if (response.getResponseType() == ResponseType.OK) {
                User newUser = gson.fromJson(response.getResponseData(), User.class);
                init(newUser);
                ClientSocket.getInstance().setUser(newUser);
            } else {
                errorLabel.setVisible(true);
                errorLabel.setText(response.getResponseMessage());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User user = ClientSocket.getInstance().getUser();
        init(user);
    }
    private void init(User user) {
        valueMarkFactory.setValue(14);
        ageSpinner.setValueFactory(valueMarkFactory);
        maleRadioButton.setToggleGroup(group);
        femaleRadioButton.setToggleGroup(group);
        otherRadioButton.setToggleGroup(group);
        loginTextField.setText(user.getLogin());
        passwordField.setText(user.getPassword());
        repeatPasswordField.setText(user.getPassword());
        firstNameTextField.setText(user.getPersonData().getFirstName());
        lastNameTextField.setText(user.getPersonData().getLastName());
        emailTextField.setText(user.getPersonData().getEmail());
        phoneNumberTextField.setText(user.getPersonData().getPhoneNumber());
        ageSpinner.getValueFactory().setValue(user.getPersonData().getAge());
        maleRadioButton.setSelected(user.getPersonData().getSex().equals("Мужской"));
        femaleRadioButton.setSelected(user.getPersonData().getSex().equals("Женский"));
        otherRadioButton.setSelected(user.getPersonData().getSex().equals("Другой"));
        loginTextField.setDisable(true);
        firstNameTextField.setDisable(true);
        lastNameTextField.setDisable(true);
        ageSpinner.setDisable(true);
    }

    private User getUser() {
        User user = ClientSocket.getInstance().getUser();
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

    public void onEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            try {
                onUpdate();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}



