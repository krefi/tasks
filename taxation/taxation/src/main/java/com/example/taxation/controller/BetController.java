package com.example.taxation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taxation.model.BetRequest;
import com.example.taxation.model.BetResponse;
import com.example.taxation.service.BetService;

import jakarta.validation.Valid;

/**
 *
 * @author Uros
 */
@RestController
@RequestMapping("/api/bet")
public class BetController {

    private final BetService betService;

    public BetController(BetService betService) {
        this.betService = betService;
    }

    
    /**
     * Calculates the return amount and applicable tax for a bet, based on a trader-specific tax rule.
     * @param req {@link BetRequest} containing trader ID, played amount, and odd
     * @return a {@link BetResponse} containing possible return amounts before and after tax, tax rate (if applicable), and tax amount
     */
    @PostMapping("/calculateTax")
    public ResponseEntity<BetResponse> calculateTax(@Valid @RequestBody BetRequest request) {
        return ResponseEntity.ok(betService.calculateTax(request));
    }

}
