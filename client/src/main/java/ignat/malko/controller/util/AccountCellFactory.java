package ignat.malko.controller.util;

import ignat.malko.controller.manager.AccountCellController;
import ignat.malko.model.Account;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class AccountCellFactory extends ListCell<Account> {
    @Override
    protected void updateItem(Account account, boolean empty) {
        super.updateItem(account, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/manager/AccountCell.fxml"));
            AccountCellController controller = null;
            try {
                controller = new AccountCellController(account);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            loader.setController(controller);
            setText(null);
            try {
                setGraphic(loader.load());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
