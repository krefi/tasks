package com.example.taxation.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Uros
 */
public class BetRequest {

    @NotNull
    private Integer traderId;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal playedAmount;

    @NotNull
    @DecimalMin("1.0")
    private BigDecimal odd;

    public Integer getTraderId() {
        return traderId;
    }

    public void setTraderId(Integer traderId) {
        this.traderId = traderId;
    }

    public BigDecimal getPlayedAmount() {
        return playedAmount;
    }

    public void setPlayedAmount(BigDecimal playedAmount) {
        this.playedAmount = playedAmount;
    }

    public BigDecimal getOdd() {
        return odd;
    }

    public void setOdd(BigDecimal odd) {
        this.odd = odd;
    }
}
