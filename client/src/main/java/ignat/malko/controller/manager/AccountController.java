package ignat.malko.controller.manager;

import ignat.malko.controller.util.AccountCellFactory;
import ignat.malko.model.Account;
import ignat.malko.model.enums.AccountStatus;
import ignat.malko.service.AccountService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AccountController implements Initializable {
    private final AccountService accountService = new AccountService();
    @FXML
    private ListView<Account> accountsListView;
    private List<Account> accounts;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            accounts = accountService.getAllAccounts();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        if (accounts != null) {
            ObservableList<Account> accountsList = accountsListView.getItems();
            accountsList.setAll(accounts
                    .stream()
                    .filter(a -> a.getStatus() == AccountStatus.PENDING)
                    .toList());
            accountsListView.setItems(accountsList);
            accountsListView.setCellFactory(e -> new AccountCellFactory());
        }
    }

    public void onNew() {
        try {
            accounts = accountService.getAllAccounts();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        ObservableList<Account> accountsList = accountsListView.getItems();
        accountsList.setAll(accounts
                .stream()
                .filter(a -> a.getStatus() == AccountStatus.PENDING)
                .toList());
        accountsListView.setItems(accountsList);
        accountsListView.setCellFactory(e -> new AccountCellFactory());
    }

    public void onOpened() {
        try {
            accounts = accountService.getAllAccounts();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        ObservableList<Account> accountsList = accountsListView.getItems();
        accountsList.setAll(accounts
                .stream()
                .filter(a -> a.getStatus() == AccountStatus.OPENED)
                .sorted((a1, a2) -> a2.getCreated().compareTo(a1.getCreated()))
                .toList());
        accountsListView.setItems(accountsList);
        accountsListView.setCellFactory(e -> new AccountCellFactory());
    }

    public void onFrozen() {
        try {
            accounts = accountService.getAllAccounts();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        ObservableList<Account> accountsList = accountsListView.getItems();
        accountsList.setAll(accounts
                .stream()
                .filter(a -> a.getStatus() == AccountStatus.FROZEN)
                .sorted((a1, a2) -> a2.getCreated().compareTo(a1.getCreated()))
                .toList());
        accountsListView.setItems(accountsList);
        accountsListView.setCellFactory(e -> new AccountCellFactory());
    }

    public void onClosed() {
        try {
            accounts = accountService.getAllAccounts();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        ObservableList<Account> accountsList = accountsListView.getItems();
        accountsList.setAll(accounts
                .stream()
                .filter(a -> a.getStatus() == AccountStatus.CLOSED)
                .sorted((a1, a2) -> a2.getCreated().compareTo(a1.getCreated()))
                .toList());
        accountsListView.setItems(accountsList);
        accountsListView.setCellFactory(e -> new AccountCellFactory());
    }
}
