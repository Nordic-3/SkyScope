package com.szte.skyScope.controllers;

import com.stripe.model.checkout.Session;
import com.szte.skyScope.services.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
  private final PaymentService paymentService;

  @PostMapping("/createOrder/payment/{searchId}")
  public void payment(@PathVariable String searchId, HttpServletResponse response) {
    try {
      Session session = Session.create(paymentService.createStripePaymentSession(searchId));
      response.setStatus(303);
      response.setHeader("Location", session.getUrl());
    } catch (Exception exception) {
      log.error(
          "Error while creating Stripe payment session {}", exception.getMessage(), exception);
      response.setStatus(303);
      response.setHeader(
          "Location", "http://localhost:8080/createOrder/create/" + searchId + "?success");
    }
  }
}
