package com.szte.skyscope.controllers;

import static org.mockito.Mockito.*;

import com.stripe.model.checkout.Session;
import com.szte.skyscope.services.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

  @Mock private PaymentService paymentService;

  @Mock private HttpServletResponse response;

  private PaymentController paymentController;

  private static final String SEARCH_ID = "search-123";

  @BeforeEach
  void setUp() {
    paymentController = new PaymentController(paymentService);
  }

  @Test
  void payment_Success_RedirectsToStripeUrl() {
    String dummyUrl = "https://checkout.stripe.com/pay/cs_test_123";

    Session mockSession = mock(Session.class);
    when(mockSession.getUrl()).thenReturn(dummyUrl);

    try (MockedStatic<Session> mockedSession = mockStatic(Session.class)) {
      mockedSession
          .when(() -> Session.create(paymentService.createStripePaymentSession(SEARCH_ID)))
          .thenReturn(mockSession);
      paymentController.payment(SEARCH_ID, response);
      verify(paymentService, times(2)).createStripePaymentSession(SEARCH_ID);
      verify(response).setStatus(303);
      verify(response).setHeader("Location", dummyUrl);
    }
  }

  @Test
  void payment_Exception_RedirectsToFallbackUrl() {
    when(paymentService.createStripePaymentSession(SEARCH_ID))
        .thenThrow(new RuntimeException("Stripe API error"));

    paymentController.payment(SEARCH_ID, response);

    verify(paymentService).createStripePaymentSession(SEARCH_ID);
    verify(response).setStatus(303);
    verify(response)
        .setHeader(
            "Location", "http://localhost:8080/createOrder/create/" + SEARCH_ID + "?success");
  }
}
