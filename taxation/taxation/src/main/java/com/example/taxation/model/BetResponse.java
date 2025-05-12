package com.example.taxation.model;

import java.math.BigDecimal;

/**
 *
 * @author Uros
 */
public class BetResponse {
    public BigDecimal possibleReturnAmount;
    public BigDecimal possibleReturnAmountBefTax;
    public BigDecimal possibleReturnAmountAfterTax;
    public BigDecimal taxRate;
    public BigDecimal taxAmount;


    public BigDecimal getPossibleReturnAmount() {
        return possibleReturnAmount;
    }
    public void setPossibleReturnAmount(BigDecimal possibleReturnAmount) {
        this.possibleReturnAmount = possibleReturnAmount;
    }
    public BigDecimal getPossibleReturnAmountBefTax() {
        return possibleReturnAmountBefTax;
    }
    public void setPossibleReturnAmountBefTax(BigDecimal possibleReturnAmountBefTax) {
        this.possibleReturnAmountBefTax = possibleReturnAmountBefTax;
    }
    public BigDecimal getPossibleReturnAmountAfterTax() {
        return possibleReturnAmountAfterTax;
    }
    public void setPossibleReturnAmountAfterTax(BigDecimal possibleReturnAmountAfterTax) {
        this.possibleReturnAmountAfterTax = possibleReturnAmountAfterTax;
    }
    public BigDecimal getTaxRate() {
        return taxRate;
    }
    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
}
