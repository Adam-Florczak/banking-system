package banking.system.exchangerate;

import banking.system.common.BaseEntity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

public class ExchangeRate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal buyEur;
    private BigDecimal sellEur;
    private BigDecimal eur;
    private BigDecimal buyChf;
    private BigDecimal sellChf;
    private BigDecimal chf;
    private BigDecimal buyGbp;
    private BigDecimal sellGbp;
    private BigDecimal gbp;
    private BigDecimal buyUsd;
    private BigDecimal sellUsd;
    private BigDecimal usd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBuyEur() {
        return buyEur;
    }

    public void setBuyEur(BigDecimal buyEur) {
        this.buyEur = buyEur;
    }

    public BigDecimal getSellEur() {
        return sellEur;
    }

    public void setSellEur(BigDecimal sellEur) {
        this.sellEur = sellEur;
    }

    public BigDecimal getEur() {
        return eur;
    }

    public void setEur(BigDecimal eur) {
        this.eur = eur;
    }

    public BigDecimal getBuyChf() {
        return buyChf;
    }

    public void setBuyChf(BigDecimal buyChf) {
        this.buyChf = buyChf;
    }

    public BigDecimal getSellChf() {
        return sellChf;
    }

    public void setSellChf(BigDecimal sellChf) {
        this.sellChf = sellChf;
    }

    public BigDecimal getChf() {
        return chf;
    }

    public void setChf(BigDecimal chf) {
        this.chf = chf;
    }

    public BigDecimal getBuyGbp() {
        return buyGbp;
    }

    public void setBuyGbp(BigDecimal buyGbp) {
        this.buyGbp = buyGbp;
    }

    public BigDecimal getSellGbp() {
        return sellGbp;
    }

    public void setSellGbp(BigDecimal sellGbp) {
        this.sellGbp = sellGbp;
    }

    public BigDecimal getGbp() {
        return gbp;
    }

    public void setGbp(BigDecimal gbp) {
        this.gbp = gbp;
    }

    public BigDecimal getBuyUsd() {
        return buyUsd;
    }

    public void setBuyUsd(BigDecimal buyUsd) {
        this.buyUsd = buyUsd;
    }

    public BigDecimal getSellUsd() {
        return sellUsd;
    }

    public void setSellUsd(BigDecimal sellUsd) {
        this.sellUsd = sellUsd;
    }

    public BigDecimal getUsd() {
        return usd;
    }

    public void setUsd(BigDecimal usd) {
        this.usd = usd;
    }
}


