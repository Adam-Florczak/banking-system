package banking.system.transaction;

import banking.system.account.Account;
import banking.system.common.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {

    private String fromNumber;
    private String toNumber;
    private BigDecimal amount;

    public String getFromNumber() {
        return fromNumber;
    }

    public void setFromNumber(String fromNumber) {
        this.fromNumber = fromNumber;
    }

    public String getToNumber() {
        return toNumber;
    }

    public void setToNumber(String toNumber) {
        this.toNumber = toNumber;
    }

    private Currency currency;
    private String title;
    private LocalDateTime dueDate;

    public TransactionDTO() {
    }

    public String getFrom() {
        return fromNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal value) {
        this.amount = value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}
