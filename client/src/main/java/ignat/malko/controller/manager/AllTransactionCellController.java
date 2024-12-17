package ignat.malko.controller.manager;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import ignat.malko.model.Transaction;
import ignat.malko.service.TransactionService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AllTransactionCellController implements Initializable {
    private final Transaction transaction;
    private final TransactionService transactionService = new TransactionService();
    @FXML
    private FontAwesomeIconView inIcon;
    @FXML
    private FontAwesomeIconView outIcon;
    @FXML
    private Label transactionDateLabel;
    @FXML
    private Label senderLabel;
    @FXML
    private Label receiverLabel;
    @FXML
    private Label amountLabel;
    @FXML
    private Button messageButton;
    @FXML
    private Button rollbackButton;
    @FXML
    private AnchorPane transactionCell;

    public AllTransactionCellController(Transaction transaction) throws IOException {
        this.transaction = transaction;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        senderLabel.setText(transaction.getSender() != null ? transaction.getSender().getNumber() : "Счёт закрыт!");
        receiverLabel.setText(transaction.getReceiver() != null ? transaction.getReceiver().getNumber() : "Счёт закрыт!");
        amountLabel.setText(String.valueOf(transaction.getAmount()));
        transactionDateLabel.setText(transaction.getDate().toString());
        if (transaction.getMessage() != null && ! transaction.getMessage().isEmpty()) {
            messageButton.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Сообщение");
                alert.setHeaderText(null);
                alert.setContentText(transaction.getMessage());
                alert.showAndWait();
            });
        } else {
            messageButton.setVisible(false);
        }
        if (transaction.getSender() == null || transaction.getReceiver() == null) {
            rollbackButton.setDisable(true);
        }
        rollbackButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение операции");
            alert.setHeaderText("Вы уверены, что хотите откатить транзакцию?");
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
                try {
                    transactionService.rollbackTransaction(transaction);
                    transactionCell.setVisible(false);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            });
        });
    }
}
