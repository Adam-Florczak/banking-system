package banking.system.product;

import banking.system.common.Currency;
import banking.system.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InvestmentDTO {

    private String accountNumber;

    private Currency currency;

    private BigDecimal amount;

    private LocalDateTime term;

    private BigDecimal interest;

    private Transaction payment;

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

    public LocalDateTime getTerm() {
        return term;
    }

    public void setTerm(LocalDateTime term) {
        this.term = term;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public Transaction getPayment() {
        return payment;
    }

    public void setPayment(Transaction payment) {
        this.payment = payment;
    }
}
