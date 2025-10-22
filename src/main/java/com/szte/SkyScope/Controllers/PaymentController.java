package com.szte.SkyScope.Controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.szte.SkyScope.Config.ApplicationConfig;
import com.szte.SkyScope.Services.SearchStore;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
  private final ApplicationConfig applicationConfig;
  private final SearchStore searchStore;

  @Autowired
  public PaymentController(ApplicationConfig applicationConfig, SearchStore searchStore) {
    this.applicationConfig = applicationConfig;
    this.searchStore = searchStore;
  }

  @PostMapping("/createOrder/payment/{searchId}")
  public void payment(@PathVariable String searchId, HttpServletResponse response)
      throws StripeException {
    Stripe.apiKey = applicationConfig.getStripeSecret();
    SessionCreateParams params =
        SessionCreateParams.builder()
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
    Session session = Session.create(params);
    response.setStatus(303);
    response.setHeader("Location", session.getUrl());
  }

  public long getPriceAmount(String searchId) {
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
