package banking.system.product;

import banking.system.account.Account;
import banking.system.common.BaseEntity;
import banking.system.common.Currency;
import banking.system.transaction.Transaction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Credit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private int installmentsQuantity;

    @NotNull
    private BigDecimal interest;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REFRESH})
    private Set<Transaction> installments;

    public Credit() {
    }

    public Credit(Account account, Currency currency, BigDecimal amount, int installmentsQuantity, BigDecimal interest, Set<Transaction> installments) {
        this.account = account;
        this.currency = currency;
        this.amount = amount;
        this.installmentsQuantity = installmentsQuantity;
        this.interest = interest;
        this.installments = installments;
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

    public Set<Transaction> getInstallments() {
        return installments;
    }

    public void setInstallments(Set<Transaction> installments) {
        this.installments = installments;
    }
}
