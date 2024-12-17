package ignat.malko.handler;

import ignat.malko.model.InterestRate;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.enums.RequestType;

import java.io.IOException;

public class InterestRateHandler extends Handler<InterestRate> {
    @Override
    public Response handle(RequestType type, InterestRate message) throws IOException {
        return super.handle(type, message);
    }
}
