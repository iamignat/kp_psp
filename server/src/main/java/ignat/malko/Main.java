package ignat.malko;

import ignat.malko.util.ClientThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    private static final int PORT_NUMBER = 5555;

    private static final List<Socket> currentSockets = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
        while (true) {
            currentSockets.removeIf(Socket::isClosed);
            Socket socket = serverSocket.accept();
            currentSockets.add(socket);
            ClientThread clientHandler = new ClientThread(socket);
            Thread thread = new Thread(clientHandler);
            String socketInfo = "Клиент " + socket.getInetAddress() + ":" + socket.getPort() + " подключен.";
            System.out.println(socketInfo);
            thread.start();
            System.out.flush();
        }
    }
}
