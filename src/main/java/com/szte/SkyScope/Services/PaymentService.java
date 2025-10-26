package com.szte.SkyScope.Services;

import com.stripe.param.checkout.SessionCreateParams;

public interface PaymentService {
  SessionCreateParams createStriePaymentSession(String searchId);
}
