package ignat.malko.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ignat.malko.handler.AccountHandler;
import ignat.malko.handler.TransactionHandler;
import ignat.malko.handler.UserHandler;
import ignat.malko.model.Account;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.User;
import ignat.malko.model.enums.AccountStatus;
import ignat.malko.model.enums.AccountType;
import ignat.malko.model.enums.RequestType;
import ignat.malko.model.enums.ResponseType;
import ignat.malko.util.LocalDateTypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {
    private AccountHandler accountHandler;
    private UserHandler userHandler;
    private AccountService accountService;
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();


    @BeforeEach
    void setUp() {
        accountHandler = mock(AccountHandler.class);
        userHandler = mock(UserHandler.class);
        TransactionHandler transactionHandler = mock(TransactionHandler.class);

        accountService = new AccountService();

        // Используем рефлексию для замены приватных полей
        setPrivateField(accountService, "accountHandler", accountHandler);
        setPrivateField(accountService, "userHandler", userHandler);
        setPrivateField(accountService, "transactionHandler", transactionHandler);
        setPrivateField(accountService, "gson", gson);
    }

    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFindAllUserAccounts_success() throws IOException {
        User user = new User();
        Response response = new Response();
        response.setResponseType(ResponseType.OK);
        response.setResponseData(gson.toJson(new Account[]{new Account(1L, 400, "5444855579851234",  AccountType.MAIN, AccountStatus.OPENED,  LocalDate.now(), user), new Account(2L, 10000, "5444855579851239",  AccountType.SAVINGS, AccountStatus.OPENED,  LocalDate.now(), user)}));

        when(userHandler.handle(RequestType.GET_USER_ACCOUNTS, user)).thenReturn(response);

        List<Account> accounts = accountService.findAllUserAccounts(user);

        assertNotNull(accounts);
        assertEquals(2, accounts.size());
        verify(userHandler).handle(RequestType.GET_USER_ACCOUNTS, user);
    }

    @Test
    void testFindAllUserAccounts_failure() throws IOException {
        User user = new User();
        Response response = new Response();
        response.setResponseType(ResponseType.ERROR);
        response.setResponseMessage("Error occurred");

        when(userHandler.handle(RequestType.GET_USER_ACCOUNTS, user)).thenReturn(response);

        List<Account> accounts = accountService.findAllUserAccounts(user);

        assertNull(accounts);
        verify(userHandler).handle(RequestType.GET_USER_ACCOUNTS, user);
    }

    @Test
    void testCreateNewAccount_success() throws IOException {
        User user = new User();
        AccountType type = AccountType.SAVINGS;
        Response response = new Response();
        response.setResponseType(ResponseType.OK);
        response.setResponseData(gson.toJson(new Account(1L, 0, "5444855579851234",  AccountType.SAVINGS, AccountStatus.PENDING,  LocalDate.now(), user)));

        when(accountHandler.handle(eq(RequestType.CREATE_ACCOUNT), any(Account.class))).thenReturn(response);

        Account account = accountService.createNewAccount(user, type);

        assertNotNull(account);
        assertEquals(1, account.getId());

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountHandler).handle(eq(RequestType.CREATE_ACCOUNT), captor.capture());
        assertEquals(user, captor.getValue().getOwner());
        assertEquals(type, captor.getValue().getType());
    }

    @Test
    void testCloseAccount_success() throws IOException {
        User user = new User();
        AccountType type = AccountType.MAIN;
        AccountStatus status = AccountStatus.CLOSED;
        Response response = new Response();
        response.setResponseType(ResponseType.OK);
        response.setResponseData(gson.toJson(new Account[]{new Account(1L, 400, "5444855579851234",  AccountType.MAIN, AccountStatus.CLOSED,  LocalDate.now(), user), new Account(2L, 10000, "5444855579851239",  AccountType.SAVINGS, AccountStatus.OPENED,  LocalDate.now(), user)}));

        when(accountHandler.handle(eq(RequestType.CLOSE_ACCOUNT), any(Account.class))).thenReturn(response);

        List<Account> accounts = accountService.closeAccount(user, type);

        assertNotNull(accounts);
        assertEquals(2, accounts.size());

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountHandler).handle(eq(RequestType.CLOSE_ACCOUNT), captor.capture());
        assertEquals(user, captor.getValue().getOwner());
        assertEquals(type, captor.getValue().getType());
        System.out.println(captor.getValue());
        assertEquals(status, accounts.stream().filter(a -> a.getType() == type).findFirst().get().getStatus());
    }

    @Test
    void testUpdateAccountStatus_success() throws IOException {
        User user = new User();
        Account account = new Account();
        AccountStatus status = AccountStatus.OPENED;
        Response response = new Response();
        response.setResponseType(ResponseType.OK);
        response.setResponseData(gson.toJson(new Account(1L, 0, "5444855579851234",  AccountType.SAVINGS, AccountStatus.OPENED,  LocalDate.now(), user)));

        when(accountHandler.handle(RequestType.UPDATE_ACCOUNT_STATUS, account)).thenReturn(response);

        Account updatedAccount = accountService.updateAccountStatus(account, status);

        assertNotNull(updatedAccount);
        assertEquals(AccountStatus.OPENED, updatedAccount.getStatus());
        verify(accountHandler).handle(RequestType.UPDATE_ACCOUNT_STATUS, account);
    }
}
