package ignat.malko.controller.admin;

import ignat.malko.model.User;
import ignat.malko.util.ClientSocket;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ClientCellController implements Initializable {
    @FXML
    private Label username;
    @FXML
    private Label fullName;
    @FXML
    private Button editButton;
    private final User user;

    public ClientCellController(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText(username.getText() + " " + user.getLogin());
        fullName.setText(fullName.getText() + " " + user.getPersonData().getLastName() + " " + user.getPersonData().getFirstName());
        editButton.setOnAction(event -> {
            BorderPane parent = (BorderPane) editButton.getScene().getRoot();
            AnchorPane pane = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/admin/Client.fxml"));
                ClientController controller = new ClientController(user);
                loader.setController(controller);
                pane = loader.load();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            parent.setCenter(pane);
        });
        if (Objects.equals(user.getLogin(), ClientSocket.getInstance().getUser().getLogin())) {
            editButton.setDisable(true);
        }
    }
}
