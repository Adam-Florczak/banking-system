package banking.system.account;


import banking.system.client.Client;
import banking.system.common.BaseEntity;
import banking.system.common.Currency;
import banking.system.product.Credit;
import banking.system.product.Investment;
import banking.system.transaction.Transaction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String number;

    @ManyToOne
    @NotNull
    private Client owner;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @NotNull
    private BigDecimal balance;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToMany
    @JoinTable(
            name = "Account_Transaction",
            joinColumns = {@JoinColumn(name = "account_id")},
            inverseJoinColumns = {@JoinColumn(name = "transaction_id")}
    )
    private Set<Transaction> transactions;

    private BigDecimal interest;

    private BigDecimal provision;

    @OneToMany(mappedBy = "account")
    private Set<Credit> credits;

    @OneToMany(mappedBy = "account")
    private Set<Investment> investments;

    public Account() {
    }

    public Account(String number, Client owner, AccountType type, BigDecimal balance, Currency currency, Set<Transaction> transactions, BigDecimal interest, BigDecimal provision, Set<Credit> credits, Set<Investment> investments) {
        this.number = number;
        this.owner = owner;
        this.type = type;
        this.balance = balance;
        this.currency = currency;
        this.transactions = transactions;
        this.interest = interest;
        this.provision = provision;
        this.credits = credits;
        this.investments = investments;
    }

    public Long getId() {
        return id;
    }

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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
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

    public Set<Credit> getCredits() {
        return credits;
    }

    public void setCredits(Set<Credit> credits) {
        this.credits = credits;
    }

    public Set<Investment> getInvestments() {
        return investments;
    }

    public void setInvestments(Set<Investment> investments) {
        this.investments = investments;
    }
}