package com.TechMoveSystems.urbango.services;

import com.TechMoveSystems.urbango.dto.PaymentPreferenceRequest;
import com.TechMoveSystems.urbango.dto.PreferenceResponse;
import com.TechMoveSystems.urbango.models.Reserva;
import com.TechMoveSystems.urbango.repositories.ReservaRepository;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MercadoPagoService {

    private final ReservaRepository reservaRepository;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${app.backend.url}")
    private String backendUrl;

    public PreferenceResponse createPreference(PaymentPreferenceRequest request) {
        try {
            // Crear item
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .id(String.valueOf(request.getPedidoId()))
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .quantity(request.getQuantity())
                    .currencyId("COP")
                    .unitPrice(request.getPrice())
                    .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(item);

            // Configurar URLs de retorno
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(frontendUrl + "/payment/success")
                    .failure(frontendUrl + "/payment/failure")
                    .pending(frontendUrl + "/payment/pending")
                    .build();

            // Crear la preferencia (usando la clase de MercadoPago)
            com.mercadopago.client.preference.PreferenceRequest preferenceRequest =
                    com.mercadopago.client.preference.PreferenceRequest.builder()
                            .items(items)
                            .backUrls(backUrls)
                            .autoReturn("approved")
                            .statementDescriptor("UrbanGo")
                            .externalReference(String.valueOf(request.getPedidoId()))
                            .notificationUrl(backendUrl + "/api/payments/webhook")
                            .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            log.info("Preferencia creada: {}", preference.getId());

            return new PreferenceResponse(
                    preference.getId(),
                    preference.getInitPoint(),
                    preference.getSandboxInitPoint()
            );

        } catch (MPException | MPApiException e) {
            log.error("Error al crear preferencia de MercadoPago", e);
            throw new RuntimeException("Error al crear preferencia de pago: " + e.getMessage());
        }
    }

    public void processPayment(Long paymentId) {
        try {
            PaymentClient client = new PaymentClient();
            Payment payment = client.get(paymentId);

            log.info("Procesando pago: {} con estado: {}", paymentId, payment.getStatus());

            if ("approved".equals(payment.getStatus())) {
                String externalReference = payment.getExternalReference();
                if (externalReference != null && !externalReference.isEmpty() && !"null".equals(externalReference)) {
                    Integer reservaId = Integer.valueOf(externalReference);
                    Reserva reserva = reservaRepository.findById(reservaId)
                            .orElseThrow(() -> new RuntimeException("Reserva no encontrada: " + reservaId));

                    reserva.setEstado("PAGADO"); // O el estado que corresponda en tu lógica de negocio
                    reservaRepository.save(reserva);
                    log.info("Reserva {} actualizada a estado PAGADO", reservaId);
                } else {
                    log.warn("Pago {} sin referencia externa (ID de reserva)", paymentId);
                }
            }

        } catch (MPException | MPApiException e) {
            log.error("Error al procesar el pago de MercadoPago: {}", paymentId, e);
            // No lanzamos excepción para evitar que MP reintente infinitamente si es un error de lógica nuestra
            // Solo si es un error de conexión valdría la pena lanzar excepción
        }
    }
}