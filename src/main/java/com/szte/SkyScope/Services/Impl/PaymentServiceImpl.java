package com.szte.SkyScope.Services.Impl;

import com.stripe.Stripe;
import com.stripe.param.checkout.SessionCreateParams;
import com.szte.SkyScope.Config.ApplicationConfig;
import com.szte.SkyScope.Services.PaymentService;
import com.szte.SkyScope.Services.SearchStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

  private final SearchStore searchStore;
  private final ApplicationConfig applicationConfig;

  @Autowired
  public PaymentServiceImpl(SearchStore searchStore, ApplicationConfig applicationConfig) {
    this.searchStore = searchStore;
    this.applicationConfig = applicationConfig;
  }

  @Override
  public SessionCreateParams createStriePaymentSession(String searchId) {
    Stripe.apiKey = applicationConfig.getStripeSecret();
    return SessionCreateParams.builder()
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl("http://localhost:8080/createOrder/create/" + searchId + "?success")
        .setCancelUrl("http://localhost:8080/createOrder/sumup/" + searchId)
        .addLineItem(
            SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("huf")
                        .setUnitAmount(getPriceAmount(searchId))
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("Repülőjegy")
                                .build())
                        .build())
                .build())
        .build();
  }

  private long getPriceAmount(String searchId) {
    return Long.parseLong(
            searchStore
                .getSearchDatas(searchId)
                .getValidatedOffers()
                .getFirst()
                .getPrice()
                .getTotal()
                .split("\\.")[0])
        * 100;
  }
}
