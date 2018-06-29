package banking.system.account;

import banking.system.client.Client;
import banking.system.common.Currency;

import java.math.BigDecimal;

public class AccountCreateDTO {

    private String number;
    private Client owner;
    private Currency currency;
    private BigDecimal interest;
    private BigDecimal provision;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getProvision() {
        return provision;
    }

    public void setProvision(BigDecimal provision) {
        this.provision = provision;
    }
}
