package ignat.malko.controller.manager;

import ignat.malko.controller.util.AllTransactionCellFactory;
import ignat.malko.model.Transaction;
import ignat.malko.service.TransactionService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AllTransactionController implements Initializable {
    private final TransactionService transactionService = new TransactionService();
    @FXML
    private ListView<Transaction> transactionsListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            transactions = transactionService.findAllTransactions();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (transactions != null) {
            ObservableList<Transaction> transactionsList = transactionsListView.getItems();
            transactionsList.setAll(transactions.stream().sorted((e1, e2) -> e2.getDate().compareTo(e1.getDate())).toList());
            transactionsListView.setItems(transactionsList);
            transactionsListView.setCellFactory(e -> new AllTransactionCellFactory());
        }
    }
}
