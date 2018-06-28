package banking.system.transaction;

import banking.system.account.Account;
import banking.system.common.Currency;
import banking.system.product.Credit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Account from;

    @NotNull
    @ManyToOne
    private Account to;

    @NotNull
    private LocalDateTime dueDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String title;



    public Transaction() {
    }

    public Transaction(Account from, Account to, LocalDateTime dueDate, Currency currency, BigDecimal amount, String title) {
        this.from = from;
        this.to = to;
        this.dueDate = dueDate;
        this.currency = currency;
        this.amount = amount;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public Account getFrom() {
        return from;
    }

    public void setFrom(Account from) {
        this.from = from;
    }

    public Account getTo() {
        return to;
    }

    public void setTo(Account to) {
        this.to = to;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

/*    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }*/

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
