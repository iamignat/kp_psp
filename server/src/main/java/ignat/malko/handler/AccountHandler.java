package ignat.malko.handler;

import com.google.gson.Gson;
import ignat.malko.model.Account;
import ignat.malko.model.PersonData;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.Transaction;
import ignat.malko.model.User;
import ignat.malko.model.enums.AccountStatus;
import ignat.malko.model.enums.AccountType;
import ignat.malko.model.enums.ResponseType;
import ignat.malko.service.AccountService;
import ignat.malko.service.InterestRateService;
import ignat.malko.service.PersonDataService;
import ignat.malko.service.TransactionService;
import ignat.malko.util.AccountNumberGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountHandler {
    private AccountService accountService;
    private TransactionService transactionService;
    private PersonDataService personDataService;
    private InterestRateService interestRateService;
    private Gson gson;
    private final double TRANSFER_LIMIT = 10431.00;

    public Response createAccount(Account account) {
        account.setBalance(0);
        account.setStatus(AccountStatus.PENDING);
        account.setNumber(AccountNumberGenerator.generateAccountNumber());
        account.setInterestRate(interestRateService.findByAccountType(account.getType()));
        accountService.persist(account);
        return new Response(ResponseType.OK, "Вы успешно оформили заявку на открытие счета!", gson.toJson(account));
    }

    public Response findAllUserAccounts(User user) {
        List<Account> accounts = accountService.findByUserId(user.getId());
        if (accounts != null) {
            return new Response(ResponseType.OK, "Аккаунты клиента успешно найдены", gson.toJson(accounts));
        }
        return new Response(ResponseType.ERROR, "Аккаунты клиента не найдены", "");
    }

    public Response closeAccount(Account account) {
        List<Account> accounts = accountService.findByUserId(account.getOwner().getId());
        if (accounts.size() == 2) {
            if (accounts.getFirst().getType() == account.getType()) {
                double balance = accounts.getFirst().getBalance();
                accounts.getFirst().setBalance(0);
                accounts.getFirst().setCreated(LocalDate.now());
                if (accounts.get(1).getStatus() != AccountStatus.CLOSED) {
                    accounts.get(1).setBalance(accounts.get(1).getBalance() + balance);
                }
                accounts.getFirst().setStatus(AccountStatus.CLOSED);
            } else {
                double balance = accounts.get(1).getBalance();
                accounts.get(1).setBalance(0);
                accounts.get(1).setCreated(LocalDate.now());
                if (accounts.getFirst().getStatus() != AccountStatus.CLOSED) {
                    accounts.getFirst().setBalance(accounts.getFirst().getBalance() + balance);
                }
                accounts.get(1).setStatus(AccountStatus.CLOSED);
            }
        } else {
            accounts.getFirst().setStatus(AccountStatus.CLOSED);
            accounts.getFirst().setBalance(0);
            accounts.getFirst().setCreated(LocalDate.now());
        }
        for (Account a : accounts) {
            accountService.merge(a);
        }
        return new Response(ResponseType.OK, "Вы успешно закрыли счет!", gson.toJson(accounts));
    }

    public Response transferMoneyToSavings(Transaction transaction) {
        Account mainAccount = accountService.findByUserAndType(transaction.getSender().getOwner().getId(), AccountType.MAIN);
        Account savingsAccount = accountService.findByUserAndType(transaction.getSender().getOwner().getId(), AccountType.SAVINGS);
        Transaction newTransaction = new Transaction();
        newTransaction.setSender(mainAccount);
        newTransaction.setReceiver(savingsAccount);
        newTransaction.setAmount(transaction.getAmount());
        newTransaction.setMessage(null);
        newTransaction.setDate(LocalDate.now());
        transactionService.persist(newTransaction);
        mainAccount.setBalance(mainAccount.getBalance() - newTransaction.getAmount());
        savingsAccount.setBalance(savingsAccount.getBalance() + newTransaction.getAmount());
        accountService.merge(mainAccount);
        accountService.merge(savingsAccount);
        return new Response(ResponseType.OK, "Вы положили деньги на накопительный счет", gson.toJson(List.of(mainAccount, savingsAccount)));
    }

    public Response transferMoneyByNumber(Transaction transaction) {
        Account sender = accountService.findByUserAndType(transaction.getSender().getOwner().getId(), AccountType.MAIN);
        PersonData data = personDataService.findByPhoneNumber(transaction.getReceiver().getOwner().getPersonData().getPhoneNumber());
        if (data == null) {
            return new Response(ResponseType.ERROR, "Получатель не найден", "");
        }
        Account receiver = accountService.findByUserAndType(data.getId(), AccountType.MAIN);
        if (receiver == null) {
            return new Response(ResponseType.ERROR, "Получатель не найден", "");
        }
        if (receiver.getStatus() == AccountStatus.FROZEN) {
            return new Response(ResponseType.ERROR, "Получатель заморожен", "");
        } else if (receiver.getStatus() == AccountStatus.CLOSED) {
            return new Response(ResponseType.ERROR, "Получатель заблокирован", "");
        } else if (receiver.getStatus() == AccountStatus.PENDING) {
            return new Response(ResponseType.ERROR, "Получатель ожидает подтверждения", "");
        }
        return createTransferResponse(transaction, sender, receiver);
    }

    private Response createTransferResponse(Transaction transaction, Account sender, Account receiver) {
        Transaction newTransaction = new Transaction();
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setAmount(transaction.getAmount());
        newTransaction.setMessage(transaction.getMessage());
        newTransaction.setDate(LocalDate.now());
        transactionService.persist(newTransaction);
        if (newTransaction.getAmount() > TRANSFER_LIMIT) {
            receiver.setStatus(AccountStatus.FROZEN);
        }
        sender.setBalance(sender.getBalance() - newTransaction.getAmount());
        receiver.setBalance(receiver.getBalance() + newTransaction.getAmount());
        accountService.merge(sender);
        accountService.merge(receiver);
        return new Response(ResponseType.OK, "Вы успешно перевели деньги", gson.toJson(sender));
    }

    public Response transferMoneyByAccount(Transaction transaction) {
        Account sender = accountService.findByUserAndType(transaction.getSender().getOwner().getId(), AccountType.MAIN);
        Account receiver = accountService.findByNumber(transaction.getReceiver().getNumber());
        System.out.println(receiver);
        if (receiver == null) {
            System.err.println("Получатель не нашелся");
            return new Response(ResponseType.ERROR, "Получатель не найден", "");
        }
        if (receiver.getStatus() == AccountStatus.FROZEN) {
            return new Response(ResponseType.ERROR, "Получатель заморожен", "");
        } else if (receiver.getStatus() == AccountStatus.CLOSED) {
            return new Response(ResponseType.ERROR, "Получатель заблокирован", "");
        } else if (receiver.getStatus() == AccountStatus.PENDING) {
            return new Response(ResponseType.ERROR, "Получатель ожидает подтверждения", "");
        }
        return createTransferResponse(transaction, sender, receiver);
    }

    public Response findAllAccounts() {
        List<Account> accounts = accountService.findAll();
        if (accounts != null) {
            return new Response(ResponseType.OK, "Список аккаунтов успешно получен", gson.toJson(accounts));
        }
        return new Response(ResponseType.ERROR, "Список аккаунтов не получен", "");
    }

    public Response updateStatus(Account account) {
        account.setCreated(LocalDate.now());
        accountService.merge(account);
        return new Response(ResponseType.OK, "Статус успешно обновлен", gson.toJson(accountService.findById(account.getId())));
    }

    public Response removeAccount(Account account) {
        List<Transaction> transactions = transactionService.findByAccount(account);
        for (Transaction t : transactions) {
            if (Objects.equals(t.getReceiver().getId(), account.getId())) {
                t.setReceiver(null);
            } else if (Objects.equals(t.getSender().getId(), account.getId())) {
                t.setSender(null);
            }
            transactionService.merge(t);
        }
        accountService.remove(account);
        return new Response(ResponseType.OK, "Счет успешно удален", "");
    }
}
