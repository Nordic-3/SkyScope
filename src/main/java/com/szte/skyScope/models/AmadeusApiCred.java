package com.szte.skyScope.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class AmadeusApiCred {
  private String access_token;
  private String expires_in;
}
