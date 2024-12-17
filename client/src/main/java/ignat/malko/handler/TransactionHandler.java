package ignat.malko.handler;

import ignat.malko.model.TCP.Response;
import ignat.malko.model.Transaction;
import ignat.malko.model.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.IOException;

public class TransactionHandler extends Handler<Transaction> {
    @Override
    public Response handle(RequestType type, Transaction message) throws IOException {
        return super.handle(type, message);
    }
}
