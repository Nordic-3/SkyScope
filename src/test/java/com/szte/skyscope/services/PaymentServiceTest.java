package com.szte.skyscope.services;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.stripe.param.checkout.SessionCreateParams;
import com.szte.skyscope.config.ApplicationConfig;
import com.szte.skyscope.dtos.FlightOfferDTO;
import com.szte.skyscope.factories.FinalPriceFactory;
import com.szte.skyscope.models.SearchData;
import com.szte.skyscope.services.impl.PaymentServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

  @Mock private SearchStore searchStore;

  @Mock private ApplicationConfig applicationConfig;

  @InjectMocks private PaymentServiceImpl paymentService;

  @Test
  void createStripePaymentSession_ShouldReturnCorrectParams() {
    String searchId = "test-search-id";
    SearchData searchData = new SearchData();
    searchData.setValidatedOffers(List.of(FinalPriceFactory.createFlightOffer("100.00")));
    searchData.setSearchResult(List.of(new FlightOfferDTO()));
    when(searchStore.getSearchDatas(searchId)).thenReturn(searchData);
    when(applicationConfig.getStripeSecret()).thenReturn("test-secret-key");
    SessionCreateParams params = paymentService.createStripePaymentSession(searchId);
    assertThat(params).isNotNull();
  }
}
