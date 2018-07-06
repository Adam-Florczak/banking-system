package banking.system.product;

import banking.system.account.Account;
import banking.system.common.Currency;

import java.math.BigDecimal;

public class CreditDTO {
    private String accountNumber;

    private Currency currency;

    private BigDecimal amount;

    private int installmentsQuantity;

    private BigDecimal interest;

    public CreditDTO(Credit credit){
        this.accountNumber=credit.getAccount().getNumber();
        this.currency=credit.getCurrency();
        this.amount=credit.getAmount();
        this.installmentsQuantity=credit.getInstallmentsQuantity();
        this.interest=credit.getInterest();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getInstallmentsQuantity() {
        return installmentsQuantity;
    }

    public void setInstallmentsQuantity(int installmentsQuantity) {
        this.installmentsQuantity = installmentsQuantity;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
}
