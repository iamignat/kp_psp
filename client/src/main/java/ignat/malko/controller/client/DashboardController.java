package ignat.malko.controller.client;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import ignat.malko.controller.util.DashboardTransactionCellFactory;
import ignat.malko.model.Account;
import ignat.malko.model.InterestRate;
import ignat.malko.model.Transaction;
import ignat.malko.model.User;
import ignat.malko.model.enums.AccountStatus;
import ignat.malko.model.enums.AccountType;
import ignat.malko.service.AccountService;
import ignat.malko.service.InterestRateService;
import ignat.malko.service.TransactionService;
import ignat.malko.util.ClientSocket;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static ignat.malko.handler.SetUpHandler.setUpDashboard;

public class DashboardController implements Initializable {
    private final TransactionService transactionService = new TransactionService();
    private final AccountService accountService = new AccountService();
    private final InterestRateService interestRateService= new InterestRateService();
    @FXML
    private Text savingsAccountText;
    @FXML
    private Text mainAccountText;
    @FXML
    private FontAwesomeIconView mainAccountDollar;
    @FXML
    private FontAwesomeIconView savingsAccountDollar;
    @FXML
    private Text userNameText;
    @FXML
    private Label dateLabel;
    @FXML
    private Label mainBalance;
    @FXML
    private Label mainNumber;
    @FXML
    private Label savingsBalance;
    @FXML
    private Label savingsNumber;
    @FXML
    private Label incomeLabel;
    @FXML
    private Label expenseLabel;
    @FXML
    private ListView<Transaction> transactionListview;
    @FXML
    private TextField newTransactionTextField;
    @FXML
    private TextField newAmountTextField;
    @FXML
    private TextArea newMessageTextField;
    @FXML
    private Button sendMoneyButton;
    @FXML
    private Label newTransactionErrorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            init();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void init() throws IOException {
        User user = ClientSocket.getInstance().getUser();
        userNameText.setText("Добрый день, " + user.getPersonData().getFirstName() + " " + user.getPersonData().getLastName());
        dateLabel.setText("Сегодня: " + LocalDate.now());
        List<InterestRate> rates = interestRateService.getAllInterestRates();
        InterestRate mainRate = rates.stream().filter(rate -> rate.getType() == AccountType.MAIN).findFirst().orElse(null);
        InterestRate savingsRate = rates.stream().filter(rate -> rate.getType() == AccountType.SAVINGS).findFirst().orElse(null);
        if (mainRate != null) {
            DecimalFormat df = new DecimalFormat("#.##");
            mainAccountText.setText("Основной счёт" + " (" + Double.valueOf(df.format(mainRate.getValue() * 100)) + "%)" );
        }
        if (savingsRate != null) {
            savingsAccountText.setText("Накопительный счёт" + " (" + (int) (savingsRate.getValue() * 100) + "%)");
        }
        List<Account> accounts = new ArrayList<>();
        try {
            accounts = accountService.findAllUserAccounts(user);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if ((accounts != null ? accounts.size() : 0) == 0) {
            sendMoneyButton.setDisable(true);
        }
        for (int i = 0; i < (accounts != null ? accounts.size() : 0); i++) {
            Account account = accounts.get(i);
            if (account.getType() == AccountType.MAIN) {
                setUpDashboard(account, mainBalance, mainNumber, mainAccountDollar);
                if (account.getStatus() != AccountStatus.OPENED) {
                    sendMoneyButton.setDisable(true);
                }
            }
            if (account.getType() == AccountType.SAVINGS) {
                setUpDashboard(account, savingsBalance, savingsNumber, savingsAccountDollar);
            }
        }
        List<Transaction> transactions = new ArrayList<>();
        try {
            transactions = transactionService.findAllUserTransactions(user);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (transactions != null) {
            double income = transactions.stream().filter(t -> t.getReceiver() != null && t.getReceiver().getType() == AccountType.MAIN)
                    .filter(t -> t.getReceiver().getOwner().equals(user))
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            double expense = transactions.stream().filter(t -> t.getSender() != null && t.getSender().getType() == AccountType.MAIN)
                    .filter(t -> t.getSender().getOwner().equals(user))
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            incomeLabel.setText(String.valueOf(income));
            expenseLabel.setText(String.valueOf(expense));
            ObservableList<Transaction> transactionsList = transactionListview.getItems();
            transactionsList.setAll(transactions.stream().sorted((e1, e2) -> e2.getDate().compareTo(e1.getDate())).toList().stream().limit(3).toList());
            transactionListview.setItems(transactionsList);
            transactionListview.setCellFactory(e -> new DashboardTransactionCellFactory());
        }
    }

    public void onSendMoney() throws IOException {
        if (newTransactionTextField.getText().isEmpty()) {
            newTransactionErrorLabel.setVisible(true);
            newTransactionErrorLabel.setText("Некорректный номер счета или телефон.");
        } else {
            if (newTransactionTextField.getText().matches("^\\+[0-9]{12}$") && ! newTransactionTextField.getText().equals(ClientSocket.getInstance().getUser().getPersonData().getPhoneNumber())) {
                try {
                    double amount = Double.parseDouble(newAmountTextField.getText());
                    if (amount <= 0 || amount > Double.parseDouble(mainBalance.getText())) {
                        newTransactionErrorLabel.setVisible(true);
                        newTransactionErrorLabel.setText("Некорректная сумма для перевода");
                    } else {
                        User user = ClientSocket.getInstance().getUser();
                        Account account = transactionService.transferMoneyByNumber(user, newTransactionTextField.getText(), amount, newMessageTextField.getText());
                        if (account != null) {
                            init();
                            newTransactionErrorLabel.setVisible(false);
                        } else {
                            newTransactionErrorLabel.setVisible(true);
                            newTransactionErrorLabel.setText("Получатель не найден");
                        }
                    }

                } catch (NumberFormatException e) {
                    newTransactionErrorLabel.setVisible(true);
                    newTransactionErrorLabel.setText("Некорректная сумма для перевода");
                }
            } else if (newTransactionTextField.getText().length() == 16) {
                try {
                    double amount = Double.parseDouble(newAmountTextField.getText());
                    if (amount <= 0 || amount > Double.parseDouble(mainBalance.getText())) {
                        newTransactionErrorLabel.setVisible(true);
                        newTransactionErrorLabel.setText("Некорректная сумма для перевода");
                    } else {
                        User user = ClientSocket.getInstance().getUser();
                        Account account = transactionService.transferMoneyByAccount(user, newTransactionTextField.getText(), amount, newMessageTextField.getText());
                        if (account != null) {
                            init();
                            newTransactionErrorLabel.setVisible(false);
                        } else {
                            newTransactionErrorLabel.setVisible(true);
                            newTransactionErrorLabel.setText("Получатель не найден");
                        }
                    }

                } catch (NumberFormatException e) {
                    newTransactionErrorLabel.setVisible(true);
                    newTransactionErrorLabel.setText("Некорректная сумма для перевода");
                }
            } else {
                newTransactionErrorLabel.setVisible(true);
                newTransactionErrorLabel.setText("Некорректный номер получателя");
            }

        }
    }
}


