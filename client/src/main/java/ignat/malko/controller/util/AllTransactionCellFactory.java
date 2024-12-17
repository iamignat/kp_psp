package ignat.malko.controller.util;

import ignat.malko.controller.manager.AllTransactionCellController;
import ignat.malko.model.Transaction;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class AllTransactionCellFactory extends ListCell<Transaction> {
    @Override
    protected void updateItem(Transaction transaction, boolean empty) {
        super.updateItem(transaction, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/manager/AllTransactionCell.fxml"));
            AllTransactionCellController controller = null;
            try {
                controller = new AllTransactionCellController(transaction);
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
