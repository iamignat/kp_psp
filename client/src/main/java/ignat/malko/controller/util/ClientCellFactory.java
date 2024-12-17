package ignat.malko.controller.util;

import ignat.malko.controller.admin.ClientCellController;
import ignat.malko.controller.manager.AccountCellController;
import ignat.malko.model.Account;
import ignat.malko.model.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class ClientCellFactory extends ListCell<User> {
    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/admin/ClientCell.fxml"));
            ClientCellController controller = null;
            controller = new ClientCellController(user);
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
