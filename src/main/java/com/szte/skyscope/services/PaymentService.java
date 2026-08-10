package com.szte.skyscope.services;

import com.stripe.param.checkout.SessionCreateParams;

public interface PaymentService {
  SessionCreateParams createStripePaymentSession(String searchId);
}
