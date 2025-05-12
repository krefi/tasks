package com.example.taxation.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.taxation.model.BetRequest;
import com.example.taxation.model.BetResponse;
import com.example.taxation.model.TaxationMode;
import com.example.taxation.model.TaxationType;
import com.example.taxation.model.TraderTaxRule;

/**
 *
 * @author Uros
 */
@Service
public class BetService {

    // for simplicity this array wil be used in place of a database for keeping trader data
    private List<TraderTaxRule> traderTaxRules = new ArrayList<>();

    public BetService() {
        traderTaxRules.add(new TraderTaxRule(1, TaxationType.GENERAL, TaxationMode.RATE, new BigDecimal("0.1")));
        traderTaxRules.add(new TraderTaxRule(2, TaxationType.GENERAL, TaxationMode.AMMOUNT, new BigDecimal("3")));
        traderTaxRules.add(new TraderTaxRule(3, TaxationType.WINNINGS, TaxationMode.RATE, new BigDecimal("0.3")));
        traderTaxRules.add(new TraderTaxRule(4, TaxationType.WINNINGS, TaxationMode.AMMOUNT, new BigDecimal("2")));
    }


    /**
     * Calculates the return amount and applicable tax for a bet, based on a trader-specific tax rule.
     * @param req {@link BetRequest} containing trader ID, played amount, and odd
     * @return a {@link BetResponse} containing possible return amounts before and after tax, tax rate (if applicable), and tax amount
     */
    public BetResponse calculateTax(BetRequest req) {
        TraderTaxRule rule = traderTaxRules.stream()
                .filter(r -> r.getTraderId().equals(req.getTraderId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No tax rule for trader"));

        BigDecimal returnBeforeTax = req.getPlayedAmount().multiply(req.getOdd());
        BigDecimal taxBase = rule.getTaxationType() == TaxationType.GENERAL
                ? returnBeforeTax
                : returnBeforeTax.subtract(req.getPlayedAmount());

        BigDecimal taxAmount = rule.getTaxMode() == TaxationMode.RATE
                ? taxBase.multiply(rule.getValue())
                : rule.getValue().min(taxBase);

        BigDecimal returnAfterTax = returnBeforeTax.subtract(taxAmount);

        BetResponse res = new BetResponse();
        res.possibleReturnAmountBefTax = returnBeforeTax;
        res.possibleReturnAmount = returnAfterTax;
        res.possibleReturnAmountAfterTax = returnAfterTax;
        res.taxRate = rule.getTaxMode() == TaxationMode.RATE ? rule.getValue() : null;
        res.taxAmount = taxAmount;

        return res;
    }

}
