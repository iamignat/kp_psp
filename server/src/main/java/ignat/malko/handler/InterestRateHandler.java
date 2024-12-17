package ignat.malko.handler;

import com.google.gson.Gson;
import ignat.malko.model.Account;
import ignat.malko.model.InterestRate;
import ignat.malko.model.TCP.Response;
import ignat.malko.model.enums.AccountStatus;
import ignat.malko.model.enums.ResponseType;
import ignat.malko.service.InterestRateService;
import ignat.malko.util.AccountNumberGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterestRateHandler {
    private InterestRateService interestRateService;
    private Gson gson;

    public Response updateInterestRate(InterestRate interestRate) {
        interestRateService.merge(interestRate);
        return new Response(ResponseType.OK, "Процентная ставка успешно обновлена", gson.toJson(interestRateService.findById(interestRate.getId())));
    }

    public Response findAllInterestRates() {
        List<InterestRate> interestRates = interestRateService.findAll();
        if (interestRates != null) {
            return new Response(ResponseType.OK, "Список процентных ставок успешно получен", gson.toJson(interestRates));
        }
        return new Response(ResponseType.ERROR, "Список процентных ставок не получен", "");
    }
}
