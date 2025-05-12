package com.example.taxation.model;

import java.math.BigDecimal;

/**
 *
 * @author Uros
 */
public class TraderTaxRule {
    private Integer traderId;
    private TaxationType taxationType;
    private TaxationMode taxMode;
    private BigDecimal value;

    public TraderTaxRule(Integer traderId, TaxationType taxationType, TaxationMode taxMode, BigDecimal value) {
        this.traderId = traderId;
        this.taxationType = taxationType;
        this.taxMode = taxMode;
        this.value = value;
    }

    public Integer getTraderId() {
        return traderId;
    }
    public void setTraderId(Integer traderId) {
        this.traderId = traderId;
    }
    public TaxationType getTaxationType() {
        return taxationType;
    }
    public void setTaxationType(TaxationType taxationType) {
        this.taxationType = taxationType;
    }
    public TaxationMode getTaxMode() {
        return taxMode;
    }
    public void setTaxMode(TaxationMode taxMode) {
        this.taxMode = taxMode;
    }
    public BigDecimal getValue() {
        return value;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
