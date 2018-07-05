package banking.system.exchangerate;

import banking.system.common.BaseEntity;
import banking.system.common.Currency;

import javax.persistence.*;

import java.math.BigDecimal;

@Entity
public class ExchangeRate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    private Currency fromCurrency;

    @Enumerated(EnumType.STRING)
    private Currency toCurrency;

    private BigDecimal rate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(Currency fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public Currency getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(Currency toCurrency) {
        this.toCurrency = toCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;

    }

    @Override
    public String toString() {

        return "\nID: " + id +
                "\nfrom: " + fromCurrency.name() +
                "\nto: " + toCurrency.name() +
                "\nratio: " + rate.toString();
    }
}


