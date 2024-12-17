package ignat.malko.controller.manager;

import ignat.malko.model.InterestRate;
import ignat.malko.model.enums.AccountType;
import ignat.malko.service.InterestRateService;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class RatesController implements Initializable {
    private final SpinnerValueFactory<Double> valueMainFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 1.00, 0.01, 0.01);
    private final SpinnerValueFactory<Integer> valueSavingsFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(15, 30);
    private final InterestRateService interestRateService = new InterestRateService();
    public Label errorLabel;
    public Spinner<Double> mainSpinner;
    public Spinner<Integer> savingsSpinner;
    private List<InterestRate> rates;

    public void onUpdate() throws IOException {
        InterestRate mainRate = rates.stream().filter(rate -> rate.getType() == AccountType.MAIN).findFirst().orElse(null);
        InterestRate savingsRate = rates.stream().filter(rate -> rate.getType() == AccountType.SAVINGS).findFirst().orElse(null);
        if (mainRate != null && mainRate.getValue() != mainSpinner.getValue() / 100) {
            DecimalFormat df = new DecimalFormat("#.####");
            mainRate.setValue(Double.parseDouble(df.format(mainSpinner.getValue() / 100)));
            interestRateService.updateInterestRate(mainRate);
        }
        if (savingsRate != null && savingsRate.getValue() != (double) savingsSpinner.getValue() / 100) {
            savingsRate.setValue((double) savingsSpinner.getValue() / 100);
            interestRateService.updateInterestRate(savingsRate);
        }
        init();
    }

    public void onEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            try {
                onUpdate();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            init();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void init() throws IOException {
        valueMainFactory.setValue(0.01);
        valueSavingsFactory.setValue(15);
        mainSpinner.setValueFactory(valueMainFactory);
        savingsSpinner.setValueFactory(valueSavingsFactory);
        rates = interestRateService.getAllInterestRates();
        if (rates != null) {
            double mainValue = rates.stream().filter(rate -> rate.getType() == AccountType.MAIN).findFirst().map(InterestRate::getValue).orElse((double) 0);
            double savingsValue = rates.stream().filter(rate -> rate.getType() == AccountType.SAVINGS).findFirst().map(InterestRate::getValue).orElse((double) 0);
            mainSpinner.getValueFactory().setValue(mainValue * 100);
            savingsSpinner.getValueFactory().setValue((int) (savingsValue * 100));
        }
    }
}
