package ignat.malko.controller.admin;

import ignat.malko.controller.util.AccountCellFactory;
import ignat.malko.controller.util.ClientCellFactory;
import ignat.malko.model.Account;
import ignat.malko.model.User;
import ignat.malko.model.enums.AccountStatus;
import ignat.malko.model.enums.Role;
import ignat.malko.service.UserService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    @FXML
    private ListView<User> clientsListView;
    private List<User> clients;
    private final UserService userService = new UserService();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            clients = userService.findAllUsers();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        if (clients != null) {
            ObservableList<User> clientsList = clientsListView.getItems();
            clientsList.setAll(clients.stream().sorted(Comparator.comparing(User::getRole)).toList());
            clientsListView.setItems(clientsList);
            clientsListView.setCellFactory(e -> new ClientCellFactory());
        }
    }

    public void onClients(ActionEvent actionEvent) {
        try {
            clients = userService.findAllUsers();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        ObservableList<User> clientsList = clientsListView.getItems();
        clientsList.setAll(clients
                .stream()
                .filter(client -> client.getRole() == Role.USER)
                .toList());
        clientsListView.setItems(clientsList);
        clientsListView.setCellFactory(lv -> new ClientCellFactory());
    }

    public void onManagers(ActionEvent actionEvent) {
        try {
            clients = userService.findAllUsers();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        ObservableList<User> clientsList = clientsListView.getItems();
        clientsList.setAll(clients
                .stream()
                .filter(client -> client.getRole() == Role.MANAGER)
                .toList());
        clientsListView.setItems(clientsList);
        clientsListView.setCellFactory(lv -> new ClientCellFactory());
    }

    public void onAdmins(ActionEvent actionEvent) {
        try {
            clients = userService.findAllUsers();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        ObservableList<User> clientsList = clientsListView.getItems();
        clientsList.setAll(clients
                .stream()
                .filter(client -> client.getRole() == Role.ADMIN)
                .toList());
        clientsListView.setItems(clientsList);
        clientsListView.setCellFactory(lv -> new ClientCellFactory());
    }
}
