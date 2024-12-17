package ignat.malko.util;

import net.andreinc.mockneat.MockNeat;
import net.andreinc.mockneat.types.enums.CreditCardType;

public class AccountNumberGenerator {
    public static String generateAccountNumber() {
        MockNeat mock = MockNeat.threadLocal();
        return mock.creditCards().types(CreditCardType.MASTERCARD, CreditCardType.VISA_16).get();
    }
}
