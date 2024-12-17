package ignat.malko.util;



import com.google.gson.Gson;
import ignat.malko.model.TCP.Request;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.enums.RequestType;

import java.io.IOException;

public class GetService<T> {
    private final Class<T> ClassType;

    public GetService(Class<T> classType) {
        ClassType = classType;
    }

    public String GetEntities(RequestType requestType) {
        Request request = new Request();
        request.setRequestMessage("");
        request.setRequestType(requestType);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
        ClientSocket.getInstance().getOut().flush();
        String answer = null;
        try {
            answer = ClientSocket.getInstance().getIn().readLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        Response response = new Gson().fromJson(answer, Response.class);
        return response.getResponseData();
    }

    public T GetEntity(RequestType requestType, T entity) throws IOException {
        Request request = new Request();

        request.setRequestMessage(new Gson().toJson(entity));
        request.setRequestType(requestType);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
        ClientSocket.getInstance().getOut().flush();
        String answer;
        answer = ClientSocket.getInstance().getIn().readLine();
        Response response = new Gson().fromJson(answer, Response.class);
        return new Gson().fromJson(response.getResponseData(), ClassType);
    }
}

