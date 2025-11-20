package com.TechMoveSystems.urbango.controller;

import com.TechMoveSystems.urbango.dto.PaymentPreferenceRequest;
import com.TechMoveSystems.urbango.dto.PreferenceResponse;
import com.TechMoveSystems.urbango.services.MercadoPagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final MercadoPagoService mercadoPagoService;

    @PostMapping("/create-preference")
    public ResponseEntity<PreferenceResponse> createPreference(
            @Valid @RequestBody PaymentPreferenceRequest request) {
        PreferenceResponse response = mercadoPagoService.createPreference(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> receiveWebhook(
            @RequestBody Map<String, Object> payload) {

        System.out.println("Webhook recibido: " + payload);

        if (payload.containsKey("data") && payload.get("data") instanceof Map) {
            Map data = (Map) payload.get("data");
            if (data.containsKey("id")) {
                String idStr = String.valueOf(data.get("id"));
                Long paymentId = Long.valueOf(idStr);
                mercadoPagoService.processPayment(paymentId);
            }
        } else if (payload.containsKey("id") && "payment".equals(payload.get("type"))) {
             // A veces MP envía el ID directamente en la raíz o en otro formato dependiendo la versión del webhook
             String idStr = String.valueOf(payload.get("id"));
             Long paymentId = Long.valueOf(idStr);
             mercadoPagoService.processPayment(paymentId);
        }

        return ResponseEntity.ok().build();
    }
}