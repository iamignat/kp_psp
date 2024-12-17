package ignat.malko.handler;

import ignat.malko.model.TCP.Response;
import ignat.malko.model.User;
import ignat.malko.model.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.IOException;
public class UserHandler extends Handler<User> {
    @Override
    public Response handle(RequestType type, User message) throws IOException {
        return super.handle(type, message);
    }
}
