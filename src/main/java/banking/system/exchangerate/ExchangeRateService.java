package banking.system.exchangerate;

import banking.system.common.Currency;

public interface ExchangeRateService {

    ExchangeRate findLast(Currency from, Currency to);
    void createCurrent(Currency from, Currency to);
    void updateRates();

}
