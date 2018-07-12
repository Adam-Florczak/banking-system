package banking.system.account;

import banking.system.client.Client;
import banking.system.common.Currency;

import java.math.BigDecimal;

public class AccountCreateDTO {

    private Currency currency;
    private AccountType accountType;

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
