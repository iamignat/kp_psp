package ignat.malko.handler;

import com.google.gson.Gson;
import ignat.malko.model.Account;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.Transaction;
import ignat.malko.model.User;
import ignat.malko.model.enums.ResponseType;
import ignat.malko.service.AccountService;
import ignat.malko.service.PersonDataService;
import ignat.malko.service.TransactionService;
import ignat.malko.service.UserService;
import ignat.malko.util.PasswordEncryptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHandler {

    private UserService userService;
    private PersonDataService personDataService;
    private AccountService accountService;
    private TransactionService transactionService;
    private Gson gson;

    public Response updateUser(User user) {
        Response response;
        user.getPersonData().setId(user.getId());
        user.setPassword(PasswordEncryptor.hashPassword(user.getPassword()));
        userService.merge(user);
        User responseUser = userService.findById(user.getId());
        response = new Response(ResponseType.OK, "Обновление прошло успешно!", gson.toJson(responseUser));
        return response;
    }

    public Response register(User user) {
        Response response;
        if (userService.findAll().stream().noneMatch(x -> x.getLogin().equalsIgnoreCase(user.getLogin()))) {
            user.setPassword(PasswordEncryptor.hashPassword(user.getPassword()));
            userService.persist(user);
            response = new Response(ResponseType.OK, "Регистрация прошла успешно!", "");
        } else {
            response = new Response(ResponseType.ERROR, "Такой пользователь уже существует!", "");
        }
        return response;
    }

    public Response login(User user) {
        Response response;
        if (userService.findAll().stream().anyMatch(x -> x.getLogin().equalsIgnoreCase(user.getLogin()))) {
            if (userService.findAll().stream().anyMatch(x -> PasswordEncryptor.verify(user.getPassword(), x.getPassword()))) {
                Optional<User> optionalUser = userService.findAll().stream().filter(x -> x.getLogin().equalsIgnoreCase(user.getLogin())).findFirst();
                if (optionalUser.isPresent()) {
                    User authorizedUser = optionalUser.get();
                    authorizedUser = userService.findById(authorizedUser.getId());
                    response = new Response(ResponseType.OK, "Авторизация прошла успешно!", gson.toJson(authorizedUser));
                } else {
                    response = new Response(ResponseType.ERROR, "Такого пользователя не существует!", "");
                }
            } else {
                response = new Response(ResponseType.ERROR, "Неправильный пароль!", "");
            }
        } else {
            response = new Response(ResponseType.ERROR, "Такого пользователя не существует!", "");
        }
        return response;
    }

    public Response findAllUsers() {
        Response response;
        if (! userService.findAll().isEmpty()) {
            response = new Response(ResponseType.OK, "Список пользователей успешно получен", gson.toJson(userService.findAll()));
        } else {
            response = new Response(ResponseType.ERROR, "Список пользователей не получен", "");
        }
        return response;
    }

    public Response deleteUser(User user) {
        Response response;
        if (userService.findAll().stream().anyMatch(x -> Objects.equals(x.getId(), user.getId()))) {
            List<Account> accounts = accountService.findByUserId(user.getId());
            if (accounts.isEmpty()) {
                userService.remove(user);
                response = new Response(ResponseType.OK, "Удаление прошло успешно!", "");
            } else {
                for (Account account : accounts) {
                    List<Transaction> transactions = transactionService.findByAccount(account);
                    for (Transaction transaction : transactions) {
                        if (Objects.equals(transaction.getSender().getId(), account.getId())) {
                            transaction.setSender(null);
                        } else if (Objects.equals(transaction.getReceiver().getId(), account.getId())) {
                            transaction.setReceiver(null);
                        }
                        transactionService.merge(transaction);
                    }
                    accountService.remove(account);
                }
            }
            userService.remove(user);
            response = new Response(ResponseType.OK, "Удаление прошло успешно!", "");
        } else {
            response = new Response(ResponseType.ERROR, "Такого пользователя не существует!", "");
        }
        return response;
    }
}

// TODO: 13.12.2024: Add password encryption and tests
