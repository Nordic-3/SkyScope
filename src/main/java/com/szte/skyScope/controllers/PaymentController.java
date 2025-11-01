package com.szte.skyScope.controllers;

import com.stripe.model.checkout.Session;
import com.szte.skyScope.services.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
  private final PaymentService paymentService;

  @Autowired
  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping("/createOrder/payment/{searchId}")
  public void payment(@PathVariable String searchId, HttpServletResponse response) {
    try {
      Session session = Session.create(paymentService.createStriePaymentSession(searchId));
      response.setStatus(303);
      response.setHeader("Location", session.getUrl());
    } catch (Exception exception) {
      Logger.getLogger(PaymentController.class.getName())
          .log(Level.SEVERE, exception.getMessage(), exception);
      response.setStatus(303);
      response.setHeader(
          "Location", "http://localhost:8080/createOrder/create/" + searchId + "?success");
    }
  }
}
