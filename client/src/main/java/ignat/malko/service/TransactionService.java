package ignat.malko.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ignat.malko.handler.TransactionHandler;
import ignat.malko.handler.UserHandler;
import ignat.malko.model.Account;
import ignat.malko.model.PersonData;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.Transaction;
import ignat.malko.model.User;
import ignat.malko.model.enums.RequestType;
import ignat.malko.model.enums.ResponseType;
import ignat.malko.util.LocalDateTypeAdapter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
public class TransactionService {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();
    private final TransactionHandler transactionHandler = new TransactionHandler();
    private final UserHandler userHandler = new UserHandler();
    public List<Transaction> findAllUserTransactions(User user) throws IOException {
        Response response = userHandler.handle(RequestType.GET_USER_TRANSACTIONS, user);
        if (response.getResponseType() == ResponseType.OK) {
            return List.of(gson.fromJson(response.getResponseData(), Transaction[].class));
        }
        System.err.println(response.getResponseMessage());
        return null;
    }

    public Account transferMoneyByNumber(User user, String number, double amount, String message) throws IOException {
        Account account = new Account();
        account.setOwner(user);
        User receiver = new User();
        PersonData personData = new PersonData();
        personData.setPhoneNumber(number);
        receiver.setPersonData(personData);
        Account receiverAccount = new Account();
        receiverAccount.setOwner(receiver);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setMessage(message);
        transaction.setSender(account);
        transaction.setReceiver(receiverAccount);
        Response response =transactionHandler.handle(RequestType.TRANSFER_MONEY_BY_NUMBER, transaction);
        if (response.getResponseType() == ResponseType.OK) {
            return gson.fromJson(response.getResponseData(), Account.class);
        }
        System.err.println(response.getResponseMessage());
        return null;
    }
    public Account transferMoneyByAccount(User user, String accountNumber, double amount, String message) throws IOException {
        Account account = new Account();
        account.setOwner(user);
        Account receiverAccount = new Account();
        receiverAccount.setNumber(accountNumber);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setMessage(message);
        transaction.setSender(account);
        transaction.setReceiver(receiverAccount);
        Response response = transactionHandler.handle(RequestType.TRANSFER_MONEY_BY_ACCOUNT, transaction);
        if (response.getResponseType() == ResponseType.OK) {
            return gson.fromJson(response.getResponseData(), Account.class);
        }
        System.err.println(response.getResponseMessage());
        return null;
    }

    public List<Transaction> findAllTransactions() throws IOException {
        Response response = transactionHandler.handle(RequestType.GET_ALL_TRANSACTIONS, null);
        if (response.getResponseType() == ResponseType.OK) {
            return List.of(gson.fromJson(response.getResponseData(), Transaction[].class));
        }
        System.err.println(response.getResponseMessage());
        return null;
    }

    public void rollbackTransaction(Transaction transaction) throws IOException {
        Response response = transactionHandler.handle(RequestType.ROLLBACK_TRANSACTION, transaction);
        if (response.getResponseType() != ResponseType.OK) {
            System.err.println(response.getResponseMessage());
        }
    }
}
