package ignat.malko.controller.manager;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import ignat.malko.model.Account;
import ignat.malko.model.InterestRate;
import ignat.malko.model.enums.AccountStatus;
import ignat.malko.model.enums.AccountType;
import ignat.malko.service.AccountService;
import ignat.malko.service.InterestRateService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class AccountCellController implements Initializable {
    private final AccountService accountService = new AccountService();
    @FXML
    AnchorPane accountCell;
    @FXML
    Label owner;
    @FXML
    Label number;
    @FXML
    Text accountType;
    @FXML
    Text date;
    @FXML
    FontAwesomeIconView checkIcon;
    @FXML
    FontAwesomeIconView timesIcon;
    @FXML
    Button acceptButton;
    @FXML
    Button declineButton;
    @FXML
    Label balance;
    private Account account;
    private final InterestRateService interestRateService = new InterestRateService();

    public AccountCellController(Account account) throws IOException {
        this.account = account;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<InterestRate> rates = null;
        try {
            rates = interestRateService.getAllInterestRates();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        if (account.getType() == AccountType.MAIN) {
            accountType.setText("Основной счет");
        } else {
            accountType.setText("Накопительный счет");
        }
        if (rates != null) {
            if (account.getType() == AccountType.MAIN) {
                InterestRate mainRate = rates.stream().filter(rate -> rate.getType() == AccountType.MAIN).findFirst().orElse(null);
                if (mainRate != null) {
                    DecimalFormat df = new DecimalFormat("#.##");
                    accountType.setText("Основной счёт" + " (" + Double.valueOf(df.format(mainRate.getValue() * 100)) + "%)" );
                }
            } else {
                InterestRate savingsRate = rates.stream().filter(rate -> rate.getType() == AccountType.SAVINGS).findFirst().orElse(null);
                if (savingsRate != null) {
                    accountType.setText("Накопительный счёт" + " (" + (int) (savingsRate.getValue() * 100) + "%)");
                }
            }
        }

        owner.setText(account.getOwner().getPersonData().getLastName() + " " + account.getOwner().getPersonData().getFirstName());
        number.setText(String.join(" ", account.getNumber().split("(?!^)(?=(?:.{4})+$)")));

        if (account.getStatus() == AccountStatus.OPENED) {
            accountCell.setStyle("-fx-background-color:#e6ffe6;");
            acceptButton.setVisible(false);
            declineButton.setText("Заморозить");
            declineButton.setStyle("-fx-font-size: 1.75em");
            declineButton.setOnAction(actionEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Подтвердите действие");
                alert.setHeaderText("Вы уверены, что хотите заморозить счет?");
                alert.setContentText("После заморозки пользователь не сможет воспользоваться этим счетом.");
                alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
                    try {
                        account = accountService.updateAccountStatus(account, AccountStatus.FROZEN);
                        accountCell.setVisible(false);
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                });
            });
            timesIcon.setGlyphName("SNOWFLAKE_ALT");
            balance.setText(String.valueOf(account.getBalance()));
            date.setText("Открыт: " + account.getCreated());
        } else if (account.getStatus() == AccountStatus.PENDING) {
            accountCell.setStyle("-fx-background-color: #ffffe6;");
            balance.setText(String.valueOf(account.getBalance()));
            date.setVisible(false);
            acceptButton.setOnAction(actionEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Подтвердите действие");
                alert.setHeaderText("Вы уверены, что хотите открыть счет пользователю?");
                alert.setContentText("После открытия счета пользователь сможет воспользоваться этим счетом.");
                alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
                    try {
                        account = accountService.updateAccountStatus(account, AccountStatus.OPENED);
                        accountCell.setVisible(false);
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                });
            });
            declineButton.setOnAction(actionEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Подтвердите действие");
                alert.setHeaderText("Вы уверены, что хотите отклонить заявку на открытие счета?");
                alert.setContentText("После отклонения заявки пользователь не сможет воспользоваться этим счетом.");
                alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
                    try {
                        accountService.removeAccount(account);
                        accountCell.setVisible(false);
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                });
            });
        } else if (account.getStatus() == AccountStatus.FROZEN) {
            accountCell.setStyle("-fx-background-color: #e6f7ff");
            acceptButton.setVisible(true);
            acceptButton.setText("Разморозить");
            acceptButton.setStyle("-fx-font-size: 1.75em");
            acceptButton.setOnAction(actionEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Подтвердите действие");
                alert.setHeaderText("Вы уверены, что хотите разморозить счет?");
                alert.setContentText("После разморозки пользователь сможет опять пользоваться этим счетом.");
                alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
                    try {
                        account = accountService.updateAccountStatus(account, AccountStatus.OPENED);
                        accountCell.setVisible(false);
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                });
            });
            checkIcon.setGlyphName("SNOWFLAKE_ALT");
            declineButton.setVisible(true);
            declineButton.setText("Закрыть");
            declineButton.setOnAction(actionEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Подтвердите действие");
                alert.setHeaderText("Вы уверены, что хотите закрыть счет?");
                alert.setContentText("После закрытие счета пользователь может потерять свои средства.");
                alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
                    try {
                        account = accountService.updateAccountStatus(account, AccountStatus.CLOSED);
                        accountCell.setVisible(false);
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                });
            });
            balance.setText(String.valueOf(account.getBalance()));
            balance.setStyle("-fx-text-fill: #FF0000");
            date.setText("Заморожен: " + account.getCreated());
        } else {
            accountCell.setStyle("-fx-background-color: #ffe6e6");
            acceptButton.setVisible(false);
            declineButton.setVisible(true);
            declineButton.setText("Удалить");
            declineButton.setOnAction(actionEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Подтвердите действие");
                alert.setHeaderText("Вы уверены, что хотите удалить счет?");
                alert.setContentText("После удаления пользователь сможет повторно открыть счет в банке.");
                alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
                    try {
                        accountService.removeAccount(account);
                        accountCell.setVisible(false);
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                });
            });
            balance.setVisible(false);
            date.setText("Закрыт: " + account.getCreated());
        }
    }
}
