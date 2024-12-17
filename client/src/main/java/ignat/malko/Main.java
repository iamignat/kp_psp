package ignat.malko;

import ignat.malko.util.ClientSocket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Socket socket = ClientSocket.getSocket();
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/FXML/auth/Login.fxml"))));
        primaryStage.setTitle("Kitten's bank");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

