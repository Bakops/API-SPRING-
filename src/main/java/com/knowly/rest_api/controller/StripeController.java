package com.knowly.rest_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.knowly.rest_api.service.StripeService;

@RestController
@RequestMapping("/stripe")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/checkout")
    public ResponseEntity<String> createCheckout(@RequestParam Long amount) {
        try {
            String url = stripeService.createCheckoutSession(
                    amount, "eur",
                    "https://ton-frontend/success",
                    "https://ton-frontend/cancel");
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur Stripe: " + e.getMessage());
        }
    }
}