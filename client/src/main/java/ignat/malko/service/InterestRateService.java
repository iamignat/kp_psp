package ignat.malko.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ignat.malko.handler.InterestRateHandler;
import ignat.malko.model.InterestRate;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.enums.RequestType;
import ignat.malko.model.enums.ResponseType;
import ignat.malko.util.LocalDateTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class InterestRateService {
    private final InterestRateHandler interestRateHandler = new InterestRateHandler();
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();
    public List<InterestRate> getAllInterestRates() throws IOException {
        Response response = interestRateHandler.handle(RequestType.GET_ALL_INTEREST_RATES, null);
        if (response.getResponseType() == ResponseType.OK) {
            return List.of(gson.fromJson(response.getResponseData(), InterestRate[].class));
        }
        System.err.println(response.getResponseMessage());
        return null;
    }
    public void updateInterestRate(InterestRate interestRate) throws IOException {
        Response response = interestRateHandler.handle(RequestType.UPDATE_INTEREST_RATE, interestRate);
        if (response.getResponseType() == ResponseType.OK) {
            gson.fromJson(response.getResponseData(), InterestRate.class);
            return;
        }
        System.err.println(response.getResponseMessage());
    }
}
