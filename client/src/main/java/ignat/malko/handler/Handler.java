package ignat.malko.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ignat.malko.model.TCP.Request;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.enums.RequestType;
import ignat.malko.util.ClientSocket;
import ignat.malko.util.LocalDateTypeAdapter;

import java.io.IOException;
import java.time.LocalDate;

public abstract class Handler<T> {
    protected final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate .class, new LocalDateTypeAdapter()).create();
    protected Response handle(RequestType type, T message) throws IOException {
        Request request = new Request();
        request.setRequestType(type);
        request.setRequestMessage(gson.toJson(message));
        ClientSocket.getInstance().getOut().println(gson.toJson(request));
        ClientSocket.getInstance().getOut().flush();
        String answer = ClientSocket.getInstance().getIn().readLine();
        return new Gson().fromJson(answer, Response.class);
    }
}
