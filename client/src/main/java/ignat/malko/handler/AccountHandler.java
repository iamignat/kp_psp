package ignat.malko.handler;

import ignat.malko.model.Account;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.IOException;
public class AccountHandler extends Handler<Account> {
    @Override
    public Response handle(RequestType type, Account message) throws IOException {
        return super.handle(type, message);
    }
}
