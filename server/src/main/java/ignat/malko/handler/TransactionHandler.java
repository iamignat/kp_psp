package ignat.malko.handler;

import com.google.gson.Gson;
import ignat.malko.model.Account;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.Transaction;
import ignat.malko.model.User;
import ignat.malko.model.enums.AccountType;
import ignat.malko.model.enums.ResponseType;
import ignat.malko.service.AccountService;
import ignat.malko.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TransactionHandler {
    private TransactionService transactionService;
    private AccountService accountService;
    private Gson gson;

    public Response findAllUserTransactions(User user) {
        List<Transaction> transactions = transactionService.findByAccount(accountService.findByUserAndType(user.getId(), AccountType.MAIN));
        if (transactions != null) {
            return new Response(ResponseType.OK, "Транзакции клиента успешно найдены", gson.toJson(transactions));
        }
        return new Response(ResponseType.ERROR, "Транзакции клиента не найдены", "");
    }

    public Response findAllTransactions() {
        List<Transaction> transactions = transactionService.findAll();
        if (transactions != null) {
            return new Response(ResponseType.OK, "Список транзакций успешно получен", gson.toJson(transactions));
        }
        return new Response(ResponseType.ERROR, "Список транзакций не получен", "");
    }

    public Response rollbackTransaction(Transaction transaction) {
        if (transaction.getReceiver() != null) {
            Account receiver = accountService.findById(transaction.getReceiver().getId());
            receiver.setBalance(receiver.getBalance() - transaction.getAmount());
            accountService.merge(receiver);
        }
        if (transaction.getSender() != null) {
            Account sender = accountService.findById(transaction.getSender().getId());
            sender.setBalance(sender.getBalance() + transaction.getAmount());
            accountService.merge(sender);
        }
        transaction.setReceiver(null);
        transaction.setSender(null);
        transactionService.remove(transaction);
        return new Response(ResponseType.OK, "Транзакция успешно отменена", "");
    }
}
