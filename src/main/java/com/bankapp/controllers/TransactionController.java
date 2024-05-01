package com.bankapp.controllers;

import com.bankapp.dto.AuthorizationRequest;
import com.bankapp.dto.AuthorizationResponse;
import com.bankapp.dto.LoadRequest;
import com.bankapp.dto.LoadResponse;
import com.bankapp.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/ping")
    public ResponseEntity<?> pingServer() {
        return ResponseEntity.ok("Server time: " + java.time.LocalDateTime.now());
    }

    @PutMapping("/authorization")
    public ResponseEntity<AuthorizationResponse> authorizeTransaction(@RequestBody AuthorizationRequest request) {
        try {
            AuthorizationResponse response = transactionService.authorizeTransaction(request);
            if ("APPROVED".equals(response.getResponseCode())) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing request", e);
        }
    }

    @PutMapping("/load")
    public ResponseEntity<LoadResponse> loadFunds(@RequestBody LoadRequest request) {
        try {
            LoadResponse response = transactionService.loadFunds(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing request", e);
        }
    }
}
