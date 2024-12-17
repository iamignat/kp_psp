package ignat.malko.controller.client;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import ignat.malko.model.Account;
import ignat.malko.model.Transaction;
import ignat.malko.model.User;
import ignat.malko.model.enums.AccountType;
import ignat.malko.service.AccountService;
import ignat.malko.util.ClientSocket;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TransactionCellController implements Initializable {

    private final Transaction transaction;
    private final AccountService accountService = new AccountService();
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

    public TransactionCellController(Transaction transaction) throws IOException {
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
        try {
            transactionIcons();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void transactionIcons() throws IOException {
        User user = ClientSocket.getInstance().getUser();
        List<Account> accounts = accountService.findAllUserAccounts(user);
        if (accounts != null) {
            Account account = accounts.stream().filter(a -> a.getType() == AccountType.MAIN).findFirst().orElse(null);
            if (account != null) {
                if (transaction.getSender() != null && transaction.getSender().equals(account)) {
                    inIcon.setFill(Color.rgb(240, 240, 240));
                    outIcon.setFill(Color.RED);
                } else {
                    inIcon.setFill(Color.GREEN);
                    outIcon.setFill(Color.rgb(240, 240, 240));
                }
            }
        }
    }
}
