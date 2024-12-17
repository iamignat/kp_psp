package ignat.malko.util;

import ignat.malko.model.User;
import lombok.Data;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Data
public class ClientSocket {
    @Getter
    private static final ClientSocket instance = new ClientSocket();
    private User user;
    @Getter
    private static Socket socket;
    @Getter
    private BufferedReader in;
    private PrintWriter out;
    private ClientSocket() {
        try {
            socket = new Socket("localhost", 5555);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

