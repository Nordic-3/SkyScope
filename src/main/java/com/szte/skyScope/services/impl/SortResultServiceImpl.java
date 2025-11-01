package com.szte.skyScope.services.impl;

import com.szte.skyScope.dtos.FlightOfferDTO;
import com.szte.skyScope.services.SortResultService;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SortResultServiceImpl implements SortResultService {

  @Override
  public List<FlightOfferDTO> sortOffersByDeffault(List<FlightOfferDTO> flightOffers) {
    return flightOffers.stream()
        .sorted(
            Comparator.comparingInt(
                offer -> offer.getItineraries().getFirst().getSegments().size()))
        .toList();
  }

  @Override
  public List<FlightOfferDTO> sortOffersByPriceASC(List<FlightOfferDTO> flightOffers) {
    return flightOffers.stream()
        .sorted(
            (o1, o2) -> {
              if (!o1.getPrice().getTotal().isEmpty() && !o2.getPrice().getTotal().isEmpty()) {
                if (Integer.parseInt(o1.getPrice().getTotal().split("\\.")[0])
                    < Integer.parseInt(o2.getPrice().getTotal().split("\\.")[0])) {
                  return -1;
                }
                if (Integer.parseInt(o1.getPrice().getTotal().split("\\.")[0])
                    == Integer.parseInt(o2.getPrice().getTotal().split("\\.")[0])) {
                  return 0;
                }
              }
              return 1;
            })
        .toList();
  }

  @Override
  public List<FlightOfferDTO> sortOffersByPriceDSC(List<FlightOfferDTO> flightOffers) {
    return flightOffers.stream()
        .sorted(
            (o1, o2) -> {
              if (!o1.getPrice().getTotal().isEmpty() && !o2.getPrice().getTotal().isEmpty()) {
                if (Integer.parseInt(o1.getPrice().getTotal().split("\\.")[0])
                    > Integer.parseInt(o2.getPrice().getTotal().split("\\.")[0])) {
                  return -1;
                }
                if (Integer.parseInt(o1.getPrice().getTotal().split("\\.")[0])
                    == Integer.parseInt(o2.getPrice().getTotal().split("\\.")[0])) {
                  return 0;
                }
              }
              return 1;
            })
        .toList();
  }

  @Override
  public List<FlightOfferDTO> sortOffersByFlyTimeAsc(List<FlightOfferDTO> flightOffers) {
    return flightOffers.stream()
        .sorted(
            (o1, o2) -> {
              if (!o1.getItineraries().getFirst().getDuration().isEmpty()
                  && !o2.getItineraries().getFirst().getDuration().isEmpty()) {
                if (Duration.parse(o1.getItineraries().getFirst().getDuration())
                        .compareTo(Duration.parse(o2.getItineraries().getFirst().getDuration()))
                    < 0) {
                  return -1;
                }
                if (Duration.parse(o1.getItineraries().getFirst().getDuration())
                        .compareTo(Duration.parse(o2.getItineraries().getFirst().getDuration()))
                    == 0) {
                  return 0;
                }
              }
              return 1;
            })
        .toList();
  }

  @Override
  public List<FlightOfferDTO> sortOffersByFlyTimeDsc(List<FlightOfferDTO> flightOffers) {
    return flightOffers.stream()
        .sorted(
            (o1, o2) -> {
              if (!o1.getItineraries().getFirst().getDuration().isEmpty()
                  && !o2.getItineraries().getFirst().getDuration().isEmpty()) {
                if (Duration.parse(o1.getItineraries().getFirst().getDuration())
                        .compareTo(Duration.parse(o2.getItineraries().getFirst().getDuration()))
                    > 0) {
                  return -1;
                }
                if (Duration.parse(o1.getItineraries().getFirst().getDuration())
                        .compareTo(Duration.parse(o2.getItineraries().getFirst().getDuration()))
                    == 0) {
                  return 0;
                }
              }
              return 1;
            })
        .toList();
  }

  @Override
  public List<FlightOfferDTO> sortOffersByTransferTimeDsc(List<FlightOfferDTO> flightOffers) {
    return flightOffers.stream()
        .sorted(
            Comparator.comparingInt(
                    (FlightOfferDTO offer) ->
                        offer.getItineraries().getFirst().getSegments().size())
                .reversed())
        .toList();
  }
}
