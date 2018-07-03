package banking.system.exchangerate;

public interface exchangeRateService {

    ExchangeRate findLast();
    ExchangeRate createCurrent();

}
