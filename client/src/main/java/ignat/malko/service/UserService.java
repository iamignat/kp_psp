package ignat.malko.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ignat.malko.handler.UserHandler;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.User;
import ignat.malko.model.enums.RequestType;
import ignat.malko.model.enums.ResponseType;
import ignat.malko.util.LocalDateTypeAdapter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class UserService {
    private final UserHandler userHandler = new UserHandler();
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();

    public List<User> findAllUsers() throws IOException {
        Response response = userHandler.handle(RequestType.GET_ALL_USERS, null);
        if (response.getResponseType() == ResponseType.OK) {
            return List.of(gson.fromJson(response.getResponseData(), User[].class));
        }
        System.err.println(response.getResponseMessage());
        return null;
    }
}
