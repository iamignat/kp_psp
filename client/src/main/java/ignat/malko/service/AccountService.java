package ignat.malko.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ignat.malko.handler.AccountHandler;
import ignat.malko.handler.TransactionHandler;
import ignat.malko.handler.UserHandler;
import ignat.malko.model.Account;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.Transaction;
import ignat.malko.model.User;
import ignat.malko.model.enums.AccountStatus;
import ignat.malko.model.enums.AccountType;
import ignat.malko.model.enums.RequestType;
import ignat.malko.model.enums.ResponseType;
import ignat.malko.util.LocalDateTypeAdapter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class AccountService {
    private final AccountHandler accountHandler = new AccountHandler();
    private final UserHandler userHandler = new UserHandler();
    private final TransactionHandler transactionHandler = new TransactionHandler();
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();

    public List<Account> findAllUserAccounts(User user) throws IOException {
        Response response = userHandler.handle(RequestType.GET_USER_ACCOUNTS, user);
        if (response.getResponseType() == ResponseType.OK) {
            return List.of(gson.fromJson(response.getResponseData(), Account[].class));
        }
        System.err.println(response.getResponseMessage());
        return null;
    }

    public Account createNewAccount(User user, AccountType type) throws IOException {
        Account account = new Account();
        account.setOwner(user);
        account.setType(type);
        Response response = accountHandler.handle(RequestType.CREATE_ACCOUNT, account);
        if (response.getResponseType() == ResponseType.OK) {
            return gson.fromJson(response.getResponseData(), Account.class);
        }
        System.err.println(response.getResponseMessage());
        return null;
    }

    public List<Account> closeAccount(User user, AccountType type) throws IOException {
        Account account = new Account();
        account.setOwner(user);
        account.setType(type);
        Response response = accountHandler.handle(RequestType.CLOSE_ACCOUNT, account);
        if (response.getResponseType() == ResponseType.OK) {
            return List.of(gson.fromJson(response.getResponseData(), Account[].class));
        }
        System.err.println(response.getResponseMessage());
        return null;
    }

    public List<Account> transferMoneyToSaving(User user, double amount) throws IOException {
        Account account = new Account();
        account.setOwner(user);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSender(account);
        Response response = transactionHandler.handle(RequestType.TRANSFER_MONEY_TO_SAVINGS, transaction);
        if (response.getResponseType() == ResponseType.OK) {
            return List.of(gson.fromJson(response.getResponseData(), Account[].class));
        }
        System.err.println(response.getResponseMessage());
        return null;
    }

    public List<Account> getAllAccounts() throws IOException {
        Response response = accountHandler.handle(RequestType.GET_ALL_ACCOUNTS, null);
        if (response.getResponseType() == ResponseType.OK) {
            return List.of(gson.fromJson(response.getResponseData(), Account[].class));
        }
        System.err.println(response.getResponseMessage());
        return null;
    }

    public Account updateAccountStatus(Account account, AccountStatus accountStatus) throws IOException {
        account.setStatus(accountStatus);
        Response response = accountHandler.handle(RequestType.UPDATE_ACCOUNT_STATUS, account);
        if (response.getResponseType() == ResponseType.OK) {
            return gson.fromJson(response.getResponseData(), Account.class);
        }
        System.err.println(response.getResponseMessage());
        return null;
    }

    public void removeAccount(Account account) throws IOException {
        accountHandler.handle(RequestType.REMOVE_ACCOUNT, account);
    }
}
