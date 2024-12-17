package ignat.malko.controller.client;

import ignat.malko.controller.util.TransactionCellFactory;
import ignat.malko.model.Transaction;
import ignat.malko.model.User;
import ignat.malko.service.TransactionService;
import ignat.malko.util.ClientSocket;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class TransactionsController implements Initializable {
    @FXML
    private ListView<Transaction> transactionsListView;
    private final TransactionService transactionService = new TransactionService();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User user = ClientSocket.getInstance().getUser();
        List<Transaction> transactions = new ArrayList<>();
        try {
            transactions = transactionService.findAllUserTransactions(user);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (transactions != null) {
            ObservableList<Transaction> transactionsList = transactionsListView.getItems();
            transactionsList.setAll(transactions.stream().sorted((e1, e2) -> e2.getDate().compareTo(e1.getDate())).toList());
            transactionsListView.setItems(transactionsList);
            transactionsListView.setCellFactory(e -> new TransactionCellFactory());
        }
    }
}
