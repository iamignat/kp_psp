package ignat.malko.controller.client;

import ignat.malko.model.enums.Role;
import ignat.malko.util.ClientSocket;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    private Button dashboardButton;
    @FXML
    private Button transactionButton;
    @FXML
    private Button myAccountsButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button accountsButton;
    @FXML
    private Button clientsButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button percentButton;
    @FXML
    private Button allTransactionsButton;

    public void onLogout() throws Exception {
        ClientSocket.getInstance().setUser(null);
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/auth/Login.fxml")));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Role role = ClientSocket.getInstance().getUser().getRole();
        if (role == Role.MANAGER || role == Role.ADMIN) {
            accountsButton.setVisible(true);
            percentButton.setVisible(true);
            allTransactionsButton.setVisible(true);
        }
        if (role == Role.ADMIN) {
            clientsButton.setVisible(true);
        }
    }

    public void openMyAccounts() throws IOException {
        BorderPane parent = (BorderPane) myAccountsButton.getScene().getRoot();
        AnchorPane pane  = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/client/MyAccounts.fxml")));
        parent.setCenter(pane);
    }
    public void openDashboard() throws IOException{
        BorderPane parent = (BorderPane) dashboardButton.getScene().getRoot();
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/client/Dashboard.fxml")));
        parent.setCenter(pane);
    }

    public void openTransactions() throws IOException{
        BorderPane parent = (BorderPane) transactionButton.getScene().getRoot();
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/client/Transactions.fxml")));
        parent.setCenter(pane);
    }

    public void openProfile() throws IOException {
        BorderPane parent = (BorderPane) profileButton.getScene().getRoot();
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/client/Profile.fxml")));
        parent.setCenter(pane);
    }

    public void openAccounts() throws IOException {
        BorderPane parent = (BorderPane) accountsButton.getScene().getRoot();
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/manager/Accounts.fxml")));
        parent.setCenter(pane);
    }

    public void openRates() throws IOException {
        BorderPane parent = (BorderPane) percentButton.getScene().getRoot();
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/manager/Rates.fxml")));
        parent.setCenter(pane);
    }

    public void openClients() throws IOException {
        BorderPane parent = (BorderPane) clientsButton.getScene().getRoot();
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/admin/Clients.fxml")));
        parent.setCenter(pane);
    }

    public void openAllTransactions() throws IOException {
        BorderPane parent = (BorderPane) transactionButton.getScene().getRoot();
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/manager/AllTransactions.fxml")));
        parent.setCenter(pane);
    }
}
