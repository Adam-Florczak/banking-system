package banking.system.exchangerate;

import banking.system.common.BaseEntity;
import banking.system.common.Currency;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class ExchangeRate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Currency from;

    @Enumerated(EnumType.STRING)
    private Currency to;

    private BigDecimal rate;
//
//    private BigDecimal eur;
//    private BigDecimal chf;
//    private BigDecimal gbp;
//    private BigDecimal usd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency getFrom() {
        return from;
    }

    public void setFrom(Currency from) {
        this.from = from;
    }

    public Currency getTo() {
        return to;
    }

    public void setTo(Currency to) {
        this.to = to;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    //    public BigDecimal getEur() {
//        return eur;
//    }
//
//    public void setEur(BigDecimal eur) {
//        this.eur = eur;
//    }
//
//    public BigDecimal getChf() {
//        return chf;
//    }
//
//    public void setChf(BigDecimal chf) {
//        this.chf = chf;
//    }
//
//    public BigDecimal getGbp() {
//        return gbp;
//    }
//
//    public void setGbp(BigDecimal gbp) {
//        this.gbp = gbp;
//    }
//
//    public BigDecimal getUsd() {
//        return usd;
//    }
//
//    public void setUsd(BigDecimal usd) {
//        this.usd = usd;
//    }
}


