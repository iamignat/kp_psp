package ignat.malko.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ignat.malko.handler.AccountHandler;
import ignat.malko.handler.InterestRateHandler;
import ignat.malko.handler.TransactionHandler;
import ignat.malko.handler.UserHandler;
import ignat.malko.model.Account;
import ignat.malko.model.InterestRate;
import ignat.malko.model.TCP.Request;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.Transaction;
import ignat.malko.model.User;
import ignat.malko.service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;

public class ClientThread implements Runnable {
    private final Socket clientSocket;
    private final BufferedReader in;
    private final PrintWriter out;
    private Request request;
    private Response response;
    private final Gson gson;
    private final TransactionHandler transactionHandler;
    private final AccountHandler accountHandler;
    private final UserHandler userHandler;
    private final InterestRateHandler interestRateHandler;

    public ClientThread(Socket clientSocket) throws IOException {
        response = new Response();
        request = new Request();
        this.clientSocket = clientSocket;
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream());
        UserService userService = new UserService();
        AccountService accountService = new AccountService();
        TransactionService transactionService = new TransactionService();
        PersonDataService personDataService = new PersonDataService();
        InterestRateService interestRateService = new InterestRateService();
        transactionHandler = new TransactionHandler(transactionService, accountService, gson);
        accountHandler = new AccountHandler(accountService, transactionService, personDataService, interestRateService, gson);
        userHandler =  new UserHandler(userService, personDataService, accountService, transactionService, gson);
        interestRateHandler = new InterestRateHandler(interestRateService, gson);
    }

    @Override
    public void run() {
        try {
            while (clientSocket.isConnected()) {
                String message = in.readLine();
                request = gson.fromJson(message, Request.class);

                switch (request.getRequestType()) {
                    case REGISTER: {
                        User user = gson.fromJson(request.getRequestMessage(), User.class);
                        response = userHandler.register(user);
                        break;
                    }
                    case LOGIN: {
                        User user = gson.fromJson(request.getRequestMessage(), User.class);
                        response = userHandler.login(user);
                        break;
                    }
                    case CREATE_ACCOUNT: {
                        Account account = gson.fromJson(request.getRequestMessage(), Account.class);
                        response = accountHandler.createAccount(account);
                        break;
                    }
                    case GET_USER_ACCOUNTS: {
                        User user = gson.fromJson(request.getRequestMessage(), User.class);
                        response = accountHandler.findAllUserAccounts(user);
                        break;
                    }
                    case CLOSE_ACCOUNT: {
                        Account account = gson.fromJson(request.getRequestMessage(), Account.class);
                        response = accountHandler.closeAccount(account);
                        break;
                    }
                    case GET_USER_TRANSACTIONS: {
                        User user = gson.fromJson(request.getRequestMessage(), User.class);
                        response = transactionHandler.findAllUserTransactions(user);
                        break;
                    }
                    case TRANSFER_MONEY_TO_SAVINGS: {
                        Transaction transaction = gson.fromJson(request.getRequestMessage(), Transaction.class);
                        response = accountHandler.transferMoneyToSavings(transaction);
                        break;
                    }
                    case TRANSFER_MONEY_BY_NUMBER: {
                        Transaction transaction = gson.fromJson(request.getRequestMessage(), Transaction.class);
                        response = accountHandler.transferMoneyByNumber(transaction);
                        break;
                    }
                    case TRANSFER_MONEY_BY_ACCOUNT: {
                        Transaction transaction = gson.fromJson(request.getRequestMessage(), Transaction.class);
                        response = accountHandler.transferMoneyByAccount(transaction);
                        break;
                    }
                    case UPDATE_USER: {
                        User user = gson.fromJson(request.getRequestMessage(), User.class);
                        response = userHandler.updateUser(user);
                        break;
                    }
                    case GET_ALL_ACCOUNTS: {
                        response = accountHandler.findAllAccounts();
                        break;
                    }
                    case UPDATE_ACCOUNT_STATUS:{
                        Account account = gson.fromJson(request.getRequestMessage(), Account.class);
                        response = accountHandler.updateStatus(account);
                        break;
                    }
                    case REMOVE_ACCOUNT: {
                        Account account = gson.fromJson(request.getRequestMessage(), Account.class);
                        response = accountHandler.removeAccount(account);
                        break;
                    }
                    case GET_ALL_INTEREST_RATES: {
                        response = interestRateHandler.findAllInterestRates();
                        break;
                    }
                    case UPDATE_INTEREST_RATE: {
                        InterestRate interestRate = gson.fromJson(request.getRequestMessage(), InterestRate.class);
                        response = interestRateHandler.updateInterestRate(interestRate);
                        break;
                    }
                    case GET_ALL_USERS: {
                        response = userHandler.findAllUsers();
                        break;
                    }
                    case DELETE_USER: {
                        User user = gson.fromJson(request.getRequestMessage(), User.class);
                        response = userHandler.deleteUser(user);
                        break;
                    }
                    case GET_ALL_TRANSACTIONS: {
                        response = transactionHandler.findAllTransactions();
                        break;
                    }
                    case ROLLBACK_TRANSACTION:{
                        Transaction transaction = gson.fromJson(request.getRequestMessage(), Transaction.class);
                        response = transactionHandler.rollbackTransaction(transaction);
                        break;
                    }
                }
                out.println(gson.toJson(response));
                out.flush();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            System.out.println("Клиент " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " закрыл соединение.");
            try {
                clientSocket.close();
                in.close();
                out.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}

