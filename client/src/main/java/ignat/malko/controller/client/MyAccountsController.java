package ignat.malko.controller.client;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import ignat.malko.model.Account;
import ignat.malko.model.InterestRate;
import ignat.malko.model.User;
import ignat.malko.model.enums.AccountStatus;
import ignat.malko.model.enums.AccountType;
import ignat.malko.service.AccountService;
import ignat.malko.service.InterestRateService;
import ignat.malko.util.ClientSocket;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static ignat.malko.handler.SetUpHandler.setUpMyAccounts;

public class MyAccountsController implements Initializable {
    @FXML
    private Text savingsAccountText;
    @FXML
    private Text mainAccountText;
    @FXML
    private FontAwesomeIconView mainDollar;
    @FXML
    private FontAwesomeIconView savingsDollar;
    @FXML
    private Label mainBalance;
    @FXML
    private Label mainNumber;
    @FXML
    private Button mainButton;
    @FXML
    private TextField amountToTransfer;
    @FXML
    private Button transferButton;
    @FXML
    private Label savingNumber;
    @FXML
    private Label savingBalance;
    @FXML
    private Button savingButton;
    @FXML
    private FontAwesomeIconView mainIcon;
    @FXML
    private FontAwesomeIconView savingIcon;
    @FXML
    private Text mainDate;
    @FXML
    private Text savingsDate;
    @FXML
    private Text transferLabel;
    private final AccountService accountService = new AccountService();
    private final InterestRateService interestRateService = new InterestRateService();

    public void onTransfer() {
        try {
            double amount = Double.parseDouble(amountToTransfer.getText());
            double money = Double.parseDouble(mainBalance.getText());
            if (amount > money) {
                transferLabel.setText("Недостаточно средств");
                transferLabel.setStyle("-fx-fill: #FF0000");
            } else {
                if (amount <= 0){
                    transferLabel.setText("Некорректное значение");
                    transferLabel.setStyle("-fx-fill: #FF0000");
                }
                else{
                    User user = ClientSocket.getInstance().getUser();
                    accountsInitialization(accountService.transferMoneyToSaving(user, amount));
                }
            }
        } catch (NumberFormatException e) {
            transferLabel.setText("Некорректное значение");
            transferLabel.setStyle("-fx-fill: #FF0000");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User user = ClientSocket.getInstance().getUser();
        List<Account> accounts = new ArrayList<>();
        List<InterestRate> rates = null;
        try {
            rates = interestRateService.getAllInterestRates();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        if (rates != null) {
            InterestRate mainRate = rates.stream().filter(rate -> rate.getType() == AccountType.MAIN).findFirst().orElse(null);
            InterestRate savingsRate = rates.stream().filter(rate -> rate.getType() == AccountType.SAVINGS).findFirst().orElse(null);
            if (mainRate != null) {
                DecimalFormat df = new DecimalFormat("#.##");
                mainAccountText.setText("Основной счёт" + " (" + Double.valueOf(df.format(mainRate.getValue() * 100)) + "%)" );
            }
            if (savingsRate != null) {
                savingsAccountText.setText("Накопительный счёт"+ " (" + (int) (savingsRate.getValue() * 100) + "%)");
            }
        }
        try {
            accounts = accountService.findAllUserAccounts(user);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        accountsInitialization(accounts);
        if (accounts != null && accounts.size() == 2 && accounts.getFirst().getStatus() == AccountStatus.OPENED && accounts.get(1).getStatus() == AccountStatus.OPENED) {
            transferButton.setDisable(false);
        }
    }

    public void onMainButton() throws IOException {
        if (mainButton.getText().equals("Открыть")) {
            Account account = accountService.createNewAccount(ClientSocket.getInstance().getUser(), AccountType.MAIN);
            if (account != null) {
                setUpMyAccounts(account, mainBalance, mainNumber, mainButton, mainIcon, mainDate, mainDollar);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтвердите действие");
            alert.setHeaderText("Вы уверены, что хотите закрыть счет?");
            alert.setContentText("Вы не сможете восстановить его!");
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
                List<Account> accounts = null;
                try {
                    accounts = accountService.closeAccount(ClientSocket.getInstance().getUser(), AccountType.MAIN);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
                accountsInitialization(accounts);
                mainNumber.setVisible(false);
                transferButton.setDisable(true);
            });
        }
    }

    public void onSavingButton() throws IOException {
        if (savingButton.getText().equals("Открыть")) {
            Account account = accountService.createNewAccount(ClientSocket.getInstance().getUser(), AccountType.SAVINGS);
            if (account != null) {
                setUpMyAccounts(account, savingBalance, savingNumber, savingButton, savingIcon, savingsDate, savingsDollar);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтвердите действие");
            alert.setHeaderText("Вы уверены, что хотите закрыть счет?");
            alert.setContentText("Вы не сможете восстановить его!");
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
                List<Account> accounts = null;
                try {
                    accounts = accountService.closeAccount(ClientSocket.getInstance().getUser(), AccountType.SAVINGS);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
                accountsInitialization(accounts);
                savingNumber.setVisible(false);
                transferButton.setDisable(true);
            });
        }
    }

    private void accountsInitialization(List<Account> accounts) {
        for (int i = 0; i < (accounts != null ? accounts.size() : 0); i++) {
            Account account = accounts.get(i);
            if (account.getType() == AccountType.MAIN) {
                setUpMyAccounts(account, mainBalance, mainNumber, mainButton, mainIcon, mainDate, mainDollar);
            }
            if (account.getType() == AccountType.SAVINGS) {
                setUpMyAccounts(account, savingBalance, savingNumber, savingButton, savingIcon, savingsDate, savingsDollar);
            }
        }
    }
}
