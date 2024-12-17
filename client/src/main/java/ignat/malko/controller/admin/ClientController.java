package ignat.malko.controller.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ignat.malko.handler.UserHandler;
import ignat.malko.model.PersonData;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.User;
import ignat.malko.model.enums.RequestType;
import ignat.malko.model.enums.ResponseType;
import ignat.malko.model.enums.Role;
import ignat.malko.util.LocalDateTypeAdapter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    private final SpinnerValueFactory<Integer> valueMarkFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(14, 80);
    final private ToggleGroup group = new ToggleGroup();
    private final UserHandler userHandler = new UserHandler();
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();
    @FXML
    private AnchorPane client;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField loginTextField;
    @FXML
    private ChoiceBox<String> roleChoiceBox;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private RadioButton otherRadioButton;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private Button backButton;
    @FXML
    private PasswordField repeatPasswordField;
    @FXML
    private Spinner<Integer> ageSpinner;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private Button deleteButton;
    @FXML
    private Button updateButton;

    private User user;

    public ClientController(User user) {
        this.user = user;
    }

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
        } else if (! maleRadioButton.isSelected() && ! femaleRadioButton.isSelected() && ! otherRadioButton.isSelected()) {
            errorLabel.setVisible(true);
            errorLabel.setText("Выберите пол");
        } else {
            user = getUser();
            Response response = userHandler.handle(RequestType.UPDATE_USER, user);
            if (response.getResponseType() == ResponseType.OK) {
                User newUser = gson.fromJson(response.getResponseData(), User.class);
                init(newUser);
                user = newUser;
            } else {
                errorLabel.setVisible(true);
                errorLabel.setText(response.getResponseMessage());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение операции");
            alert.setHeaderText("Вы уверены, что хотите изменить данные?");
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
                try {
                    onUpdate();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            });
        });
        deleteButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение операции");
            alert.setHeaderText("Вы уверены, что хотите удалить аккаунт?");
            alert.setContentText("Удаление аккаунта приведет к удаление всех его счетов и транзакций!");
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
                try {
                    Response deleteResponse = userHandler.handle(RequestType.DELETE_USER, user);
                    if (deleteResponse.getResponseType() == ResponseType.OK) {
                        BorderPane parent = (BorderPane) backButton.getScene().getRoot();
                        AnchorPane pane = null;
                        try {
                            pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/admin/Clients.fxml")));
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }
                        parent.setCenter(pane);
                    } else {
                        errorLabel.setVisible(true);
                        errorLabel.setText(deleteResponse.getResponseMessage());
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            });
        });
        backButton.setOnAction(event -> {
            BorderPane parent = (BorderPane) backButton.getScene().getRoot();
            AnchorPane pane = null;
            try {
                pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/admin/Clients.fxml")));
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            parent.setCenter(pane);
        });
        init(user);
    }

    private void init(User user) {
        valueMarkFactory.setValue(14);
        roleChoiceBox.getItems().clear();
        roleChoiceBox.getItems().addAll("USER", "MANAGER");
        roleChoiceBox.setValue(user.getRole().toString());
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
        roleChoiceBox.setValue(user.getRole().toString());
        loginTextField.setDisable(true);
        passwordField.setDisable(true);
        repeatPasswordField.setDisable(true);
    }

    private User getUser() {
        user.setLogin(loginTextField.getText());
        user.setPassword(passwordField.getText());
        user.setRole(Role.valueOf(roleChoiceBox.getValue()));

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
}
