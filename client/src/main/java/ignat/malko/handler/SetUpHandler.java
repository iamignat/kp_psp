package ignat.malko.handler;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import ignat.malko.model.Account;
import ignat.malko.model.enums.AccountStatus;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.util.List;

public class SetUpHandler {
    public static void setUpDashboard(Account account, Label balanceLabel, Label numberLabel, FontAwesomeIconView dollar) {
        if (account.getStatus() == AccountStatus.OPENED) {
            balanceLabel.setText(String.valueOf(account.getBalance()));
            numberLabel.setText("**** **** **** " + List.of(account.getNumber().split("(?!^)(?=(?:.{4})+$)")).getLast());
            numberLabel.setVisible(true);
            dollar.setVisible(true);
        } else if (account.getStatus() == AccountStatus.PENDING) {
            balanceLabel.setText(String.valueOf(account.getBalance()));
            numberLabel.setText("Ожидается открытие сотрудником");
            numberLabel.setVisible(true);
            dollar.setVisible(true);
        } else if (account.getStatus() == AccountStatus.FROZEN) {
            balanceLabel.setText(String.valueOf(account.getBalance()));
            balanceLabel.setStyle("-fx-text-fill: #FF0000");
            numberLabel.setText("Счет заморожен");
            numberLabel.setVisible(true);
            dollar.setVisible(true);
        } else {
            balanceLabel.setText("Закрыт навсегда");
            dollar.setVisible(false);
        }
    }

    public static void setUpMyAccounts(Account account, Label balanceLabel, Label numberLabel, Button functionButton, FontAwesomeIconView iconView, Text date, FontAwesomeIconView dollar) {
        if (account.getStatus() == AccountStatus.OPENED) {
            balanceLabel.setText(String.valueOf(account.getBalance()));
            numberLabel.setText(String.join(" ", account.getNumber().split("(?!^)(?=(?:.{4})+$)")));
            numberLabel.setVisible(true);
            functionButton.setText("Закрыть");
            iconView.setGlyphName("LOCK");
            date.setText("Открыт: " + account.getCreated());
            dollar.setVisible(true);
        } else if (account.getStatus() == AccountStatus.PENDING) {
            balanceLabel.setText(String.valueOf(account.getBalance()));
            numberLabel.setText("Ожидается открытие сотрудником");
            numberLabel.setVisible(true);
            functionButton.setText("Закрыть");
            iconView.setGlyphName("LOCK");
            functionButton.setDisable(true);
            date.setVisible(false);
            dollar.setVisible(true);
        } else if (account.getStatus() == AccountStatus.FROZEN) {
            balanceLabel.setText(String.valueOf(account.getBalance()));
            balanceLabel.setStyle("-fx-text-fill: #FF0000");
            numberLabel.setText("Счет заморожен");
            numberLabel.setVisible(true);
            functionButton.setText("Закрыть");
            iconView.setGlyphName("LOCK");
            date.setText("Открыт: " + account.getCreated());
            dollar.setVisible(true);
        } else {
            balanceLabel.setText("Закрыт навсегда");
            date.setText("Закрыт: " + account.getCreated());
            functionButton.setDisable(true);
            dollar.setVisible(false);
        }
    }
}
