package banking.system.product;

import banking.system.account.Account;
import banking.system.common.BaseEntity;
import banking.system.common.Currency;
import banking.system.transaction.Transaction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Investment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Account account;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private BigDecimal interest;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Transaction payment;

    public Investment() {
    }

    public Investment(Account account, Currency currency, BigDecimal amount, LocalDateTime term, BigDecimal interest, Transaction payment) {
        this.account = account;
        this.currency = currency;
        this.amount = amount;
        this.interest = interest;
        this.payment = payment;
    }

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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
