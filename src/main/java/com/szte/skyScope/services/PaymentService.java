package com.szte.skyScope.services;

import com.stripe.param.checkout.SessionCreateParams;

public interface PaymentService {
  SessionCreateParams createStriePaymentSession(String searchId);
}
