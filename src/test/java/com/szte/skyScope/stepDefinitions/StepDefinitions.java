package com.szte.skyScope.stepDefinitions;

import static org.junit.jupiter.api.Assertions.*;

import com.szte.skyScope.dataStore.DataStore;
import com.szte.skyScope.enums.FlightOffersSortOptions;
import com.szte.skyScope.helper.WebElementHelper;
import com.szte.skyScope.webDriver.FirefoxWebDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.util.List;
import java.util.random.RandomGenerator;

public class StepDefinitions {
  private final WebElementHelper webElementHelper =
      new WebElementHelper(FirefoxWebDriver.getPage());
  private final DataStore dataStore = new DataStore();

  @Given("I am on the search page")
  public void onTheSearchPage() {
    FirefoxWebDriver.navigateTo("http://localhost:8080/search");
  }

  @Given("I have searched for a return flight from {string} to {string} with valid dates")
  public void haveSearchedForReturnFlightWithValidDates(String origin, String destination) {
    onTheSearchPage();
    searchForReturnFlightWithValidDates(origin, destination);
    flightsShouldBeDisplayed(100);
  }

  @Given("I have selected an flgiht offer")
  public void selectedAFlightOffer() {
    haveSearchedForReturnFlightWithValidDates("Budapest", "London");
    webElementHelper.clickButton("a#book");
  }

  @Given("I have selected a flight from {string} to {string} and logged in")
  public void selectFlightAndLogin(String origin, String destination) {
    haveSearchedForReturnFlightWithValidDates(origin, destination);
    webElementHelper.clickButton("a#book");
    enterValidCredentials();
  }

  @Given("I am on the payment page")
  public void onThePaymentPage() {
    selectFlightAndLogin("Budapest", "London");
    fillTheTravellerDetails();
    webElementHelper.waitForElementToBeVisible("button#payBtn");
    webElementHelper.clickButton("button#payBtn");
  }

  @Given("I am on profile page after successful login")
  public void onProfilePageAfterSuccessfulLogin() {
    FirefoxWebDriver.navigateTo("http://localhost:8080/profile");
    webElementHelper.waitForElementToBeVisible(
        "a[href^='/realms/sky-scope/login-actions/registration']");
    onLoginPageClickRegistrationButton();
    submitTheRegistrationForm();
  }

  @When("I click on delete account button")
  public void clickOnDeleteAccountButton() {
    webElementHelper.clickButton("button#deleteBtn");
  }

  @When("I fill password update form with {string} and {string}")
  public void fillPasswordUpdateForm(String newPassword, String rePassword) {
    webElementHelper.clickButton("button#pswChange");
    webElementHelper.fillInputField("input#password-new", newPassword);
    webElementHelper.fillInputField("input#password-confirm", rePassword);
    webElementHelper.clickButton("button[name='login']");
  }

  @When("I filter results by max price option")
  public void filterResultsByMaxPriceOption() {
    int maxPrice =
        Integer.parseInt(
            String.join(
                "",
                webElementHelper
                    .getValueOfAnAttribute("input[aria-label='Max']", "placeholder")
                    .split("Ft")[0]
                    .split(" ")));
    dataStore.putData("filteredPrice", Double.toString(maxPrice / 1.5));
    webElementHelper.fillInputField(
        "input[aria-label='Max']", dataStore.getValue("filteredPrice", String.class));
    webElementHelper.clickButton("button#filterBtn");
  }

  @When("I filter results by number of transfers")
  public void filterResultsByNumberOfTransfers() {
    dataStore.putData(
        "transferNumber",
        webElementHelper.getFirstNotDefaultValueOfAnSelectOption("select#transferNumber"));
    webElementHelper.selectOptionFromAllDropDownsByValue(
        "select#transferNumber", dataStore.getValue("transferNumber", String.class));
    webElementHelper.clickButton("button#filterBtn");
  }

  @When("I filter results by layover duration")
  public void filterResultsByLayoverDuration() {
    webElementHelper.selectOptionFromAllDropDownsByValue(
        "select#transferDuration",
        webElementHelper.getFirstNotDefaultValueOfAnSelectOption("select#transferDuration"));
    dataStore.putData(
        "transferDuration",
        webElementHelper.getElementsTextInList("#transferDuration > option:checked").getFirst());
    webElementHelper.clickButton("button#filterBtn");
  }

  @When("I filter results by airlines")
  public void filterResultsByAirlines() {
    webElementHelper.clickButton("div[aria-controls='airlineFilter']");
    webElementHelper.checkCheckbox("input[name='airlines']");
    dataStore.putData(
        "filteredAirline",
        webElementHelper
            .getElementsTextInList("input[name='airlines']:checked ~ label")
            .getFirst());
    webElementHelper.clickButton("button#filterBtn");
  }

  @When("I filter results by airplane")
  public void filterResultsByAirplane() {
    webElementHelper.clickButton("div[aria-controls='airplaneFilter']");
    webElementHelper.checkCheckbox("input[name='airplanes']");
    dataStore.putData(
        "filteredAirplane",
        webElementHelper
            .getElementsTextInList("input[name='airplanes']:checked ~ label")
            .getFirst());
    webElementHelper.clickButton("button#filterBtn");
  }

  @When("I delete applied filters")
  public void deleteAppliedFilters() {
    webElementHelper.clickButton("button#deleteFilterBtn");
  }

  @When("I fill in the payment details with valid information")
  public void fillThePaymentDetails() {
    webElementHelper.fillInputField("input#email", "automataTest@test.hu");
    webElementHelper.fillInputField("input#cardNumber", "4242 424242424242");
    webElementHelper.fillInputField("input#cardExpiry", "09/30");
    webElementHelper.fillInputField("input#cardCvc", "999");
    webElementHelper.fillInputField("input#billingName", "Test Elek");
    webElementHelper.selectOptionFromAllDropDownsByValue("select#billingCountry", "Hungary");
    webElementHelper.clickButton("button[type='submit']");
  }

  @When("I fill in the traveller details with valid information")
  public void fillTheTravellerDetails() {
    webElementHelper.fillAllinputFieldsByLocator("input[placeholder='Vezetéknév']", "Test");
    webElementHelper.fillAllinputFieldsByLocator("input[placeholder='Keresztnév']", "Elek");
    webElementHelper.fillAllinputFieldsByLocator(
        "input[placeholder='születési dátum']", "1990-01-01");
    webElementHelper.selectOptionFromAllDropDownsByValue("#gender > select", "MALE");
    webElementHelper.fillInputField("input#countryCode", "36");
    webElementHelper.fillInputField("input#phoneNumber", "123456789");
    webElementHelper.selectOptionFromAllDropDownsByValue("#docType > select", "PASSPORT");
    webElementHelper.fillAllinputFieldsByLocator(
        "input[placeholder='Okmány száma']",
        Integer.toString(RandomGenerator.getDefault().nextInt(100000, 999999)));
    webElementHelper.fillAllinputFieldsByLocator(
        "input[placeholder='kiállítás dátuma']", "2020-01-01");
    webElementHelper.fillAllinputFieldsByLocator(
        "input[placeholder='lejárat dátuma']", "2030-01-01");
    webElementHelper.fillAllinputFieldsByLocator(
        "input[placeholder='Kiállító ország kódja (pl. HU)']", "hu");
    webElementHelper.fillAllinputFieldsByLocator(
        "input[placeholder='Kiállítás helye']", "Budapest");
    webElementHelper.fillAllinputFieldsByLocator("input[placeholder='Születési hely']", "Budapest");
    webElementHelper.fillAllinputFieldsByLocator(
        "input[placeholder='Állampolgárság (pl. HU)']", "hu");
    webElementHelper.clickButton("button#next");
  }

  @When("I fill in the traveller details with missing information")
  public void fillTheTravellerDetailsWithMissingInformation() {
    webElementHelper.fillAllinputFieldsByLocator("input[placeholder='Vezetéknév']", "Test");
    webElementHelper.fillAllinputFieldsByLocator("input[placeholder='Keresztnév']", "Elek");
    webElementHelper.clickButton("button#next");
  }

  @When("I am on login page I click the registration button")
  public void onLoginPageClickRegistrationButton() {
    webElementHelper.clickButton("a[href^='/realms/sky-scope/login-actions/registration']");
  }

  @When("I search for a return flight from {string} to {string} with valid dates")
  public void searchForReturnFlightWithValidDates(String origin, String destination) {
    fillSearchBar(
        origin,
        destination,
        LocalDate.now().plusDays(15).toString(),
        LocalDate.now().plusDays(25).toString());
    webElementHelper.clickButton("button#searchFlightsButton");
  }

  @When("I search for a one way flight from {string} to {string} with valid date")
  public void searchForOneWayFlightWithValidDate(String origin, String destination) {
    webElementHelper.fillInputField("input#originCity", origin);
    webElementHelper.fillInputField("input#destinationCity", destination);
    webElementHelper.checkCheckboxById("input#onlyOneWay");
    webElementHelper.fillInputField("input#outGoingDate", LocalDate.now().plusDays(15).toString());
    webElementHelper.clickButton("button#searchFlightsButton");
  }

  @When("I search for a return flight from {string} to {string} from {string} until {string}")
  public void searchForReturnFlightWithGivenInputs(
      String origin, String destination, String departureDate, String returnDate) {
    fillSearchBar(origin, destination, departureDate, returnDate);
    webElementHelper.clickButton("button#searchFlightsButton");
  }

  @When("I use advanced search for a return flight from {string} to {string} with valid dates")
  public void advancedSearchForReturnFlightWithValidDates(String origin, String destination) {
    fillSearchBar(
        origin,
        destination,
        LocalDate.now().plusDays(15).toString(),
        LocalDate.now().plusDays(25).toString());
    webElementHelper.clickButton("button#advancedSearchButton");
    webElementHelper.fillInputField("input#numberOfChildren", "1");
    webElementHelper.fillInputField("input#numberOfInfants", "1");
    webElementHelper.clickButton("button#searchFlightsButton");
  }

  @When("I sort the results by {string}")
  public void sortTheResultsBy(String sortBy) {
    switch (FlightOffersSortOptions.getOptionFromValue(sortBy)) {
      case PRICE_ASC -> webElementHelper.clickButton("a#priceAsc");
      case PRICE_DSC -> webElementHelper.clickButton("a#priceDsc");
      case FLYTIME_ASC -> webElementHelper.clickButton("a#flyTimeAsc");
      case FLYTIME_DSC -> webElementHelper.clickButton("a#flyTimeDsc");
      case TRANSFERTIME_ASC -> webElementHelper.clickButton("a#transferTimeAsc");
      case TRANSFERTIME_DSC -> webElementHelper.clickButton("a#transferTimeDsc");
    }
    flightsShouldBeDisplayed(100);
  }

  @When("I am on login page I enter valid credentials")
  public void enterValidCredentials() {
    webElementHelper.fillInputField("input#username", "automataTest@test.hu");
    webElementHelper.fillInputField("input#password", "automatatest");
    webElementHelper.clickButton("button[name='login']");
  }

  @When("I am on login page I enter invalid credentials")
  public void enterInvalidCredentials() {
    webElementHelper.fillInputField("input#username", "invalid@invalid");
    webElementHelper.fillInputField("input#password", "invalid");
    webElementHelper.clickButton("button[name='login']");
  }

  @Then("I should see cheaper or equals offers than the specified max price")
  public void shouldSeeCheaperOrEqualsOffersThanTheSpecifiedMaxPrice() {
    int filteredPrice =
        Integer.parseInt(dataStore.getValue("filteredPrice", String.class).split("\\.")[0]);
    getPrices().forEach(price -> assertTrue(price <= filteredPrice));
  }

  @Then("I should see offers with less or equals transfers than the specified number")
  public void shouldSeeOffersWithLessOrEqualsTransfersThanTheSpecifiedNumber() {
    int transferNumber =
        Integer.parseInt(dataStore.getValue("transferNumber", String.class).split(" ")[1]);
    getTransferNumbers().forEach(number -> assertTrue(number <= transferNumber));
  }

  @Then("I should see offers with layover duration less or equals than the specified duration")
  public void shouldSeeOffersWithLayoverDurationLessThanTheSpecifiedDuration() {
    webElementHelper.clickAllButton("button[name='details']");
    webElementHelper
        .getElementsTextInList("p[name='layoverTime']")
        .forEach(
            layover -> {
              int totalMinutes =
                  Integer.parseInt(layover.split(" ")[2]) * 60
                      + Integer.parseInt(layover.split(" ")[4]);
              int filteredTotalMinutes =
                  Integer.parseInt(
                              dataStore.getValue("transferDuration", String.class).split(" ")[0])
                          * 60
                      + Integer.parseInt(
                          dataStore.getValue("transferDuration", String.class).split(" ")[2]);
              assertTrue(totalMinutes <= filteredTotalMinutes);
            });
  }

  @Then("I should see offers with at least one flight of the specified airlines")
  public void shouldSeeOffersWithAtLeastOneFlightOfTheSpecifiedAirlines() {
    webElementHelper
        .getElementsBySelector("button[name='details']")
        .forEach(
            element -> {
              element.click();
              assertTrue(
                  webElementHelper.getElementsTextInList("p[name='carrierAirline']").stream()
                      .anyMatch(
                          s -> s.equals(dataStore.getValue("filteredAirline", String.class))));
              element.click();
            });
  }

  @Then("I should see offers with at least one flight of the specified airplane type")
  public void shouldSeeOffersWithAtLeastOneFlightOfTheSpecifiedAirplaneType() {
    webElementHelper
        .getElementsBySelector("button[name='details']")
        .forEach(
            element -> {
              element.click();
              assertTrue(
                  webElementHelper.getElementsTextInList("p[name='aircraftType']").stream()
                      .anyMatch(
                          s -> s.equals(dataStore.getValue("filteredAirplane", String.class))));
              element.click();
            });
  }

  @Then("I should see the success confirmation message")
  public void shouldSeeTheSuccessConfirmationMessage() {
    webElementHelper.waitForTextInElement("h1#successModel", "Sikeres foglalás");
    assertTrue(webElementHelper.isTextVisibleInElement("h1#successModel", "Sikeres foglalás"));
  }

  @Then("I should see the sumumary page")
  public void shouldSeeTheSummaryPage() {
    webElementHelper.waitForTextInElement("button#payBtn", "Fizetés");
    assertTrue(webElementHelper.isTextVisibleInElement("button#payBtn", "Fizetés"));
  }

  @Then("I should see the payment page")
  public void shouldSeeThePaymentPage() {
    webElementHelper.waitForTextInElement("div[class='PaymentHeader] > div'", "Pay with card");
    assertTrue(
        webElementHelper.isTextVisibleInElement(
            "div[class='PaymentHeader] > div'", "Pay with card"));
  }

  @Then("I should see error message for missing fields")
  public void shouldSeeErrorMessageForMissingFields() {
    assertTrue(webElementHelper.isElementDisplayed("div.alert-danger"));
  }

  @Then("{int} flights should be displayed")
  public void flightsShouldBeDisplayed(int numberOfResults) {
    webElementHelper.waitForGivenNumberOfElements("div.resultCard", numberOfResults);
  }

  @Then("an error message should be displayed")
  public void errorMessageShouldBeDisplayed() {
    assertTrue(webElementHelper.isElementDisplayed("div.text-danger"));
  }

  @Then("I see offers for 1 adult, 1 child and 1 infant")
  public void seeOffersForAllTypeOfPassengers() {
    webElementHelper.waitForGivenNumberOfElements("button[name='details']", 100);
    webElementHelper.clickAllButton("button[name='details']");
    webElementHelper.waitForTextInElement("div[class='collapse show']", "Felnőtt");
    webElementHelper.waitForTextInElement("div[class='collapse show']", "Gyerek");
    webElementHelper.waitForTextInElement("div[class='collapse show']", "Csecsemő, ölben");
  }

  @Then("the results should be sorted by {string}")
  public void resultsShouldBeSortedBy(String sortBy) {
    switch (FlightOffersSortOptions.getOptionFromValue(sortBy)) {
      case PRICE_ASC -> assertEquals(getPrices().stream().sorted().toList(), getPrices());
      case PRICE_DSC ->
          assertEquals(getPrices().stream().sorted((a, b) -> b - a).toList(), getPrices());
      case FLYTIME_ASC -> assertEquals(getDurations().stream().sorted().toList(), getDurations());
      case FLYTIME_DSC ->
          assertEquals(getDurations().stream().sorted((a, b) -> b - a).toList(), getDurations());
      case TRANSFERTIME_ASC ->
          assertEquals(getTransferNumbers().stream().sorted().toList(), getTransferNumbers());
      case TRANSFERTIME_DSC ->
          assertEquals(
              getTransferNumbers().stream().sorted((a, b) -> b - a).toList(), getTransferNumbers());
    }
  }

  @Then("the registration is successful")
  @Then("the login is successful")
  public void registrationIsSuccessful() {
    assertFalse(webElementHelper.isElementDisplayed("div.pf-m-error"));
  }

  @Then("the registration is not successful")
  public void theRegistrationIsNotSuccessful() {
    webElementHelper.waitForElementToBeVisible("div.pf-m-error");
    assertTrue(webElementHelper.isElementDisplayed("div.pf-m-error"));
  }

  @Then("the login is not successful")
  @Then("the password update is not successful")
  public void loginIsNotSuccessful() {
    webElementHelper.waitForElementToBeVisible("div.pf-m-error");
    assertTrue(webElementHelper.isElementDisplayed("div.pf-m-error"));
  }

  @Then("my account should be deleted")
  public void myAccountShouldBeDeleted() {
    webElementHelper.waitForTextInElement("a[href='/flighttracker']", "Explore");
    assertTrue(webElementHelper.isElementDisplayed("a[href='/flighttracker']"));
  }

  @Then("my password should be updated successfully")
  public void myPasswordShouldBeUpdatedSuccessfully() {
    webElementHelper.waitForElementToBeVisible("p:has-text('Fiókom')");
    assertTrue(webElementHelper.isElementDisplayed("p:has-text('Fiókom')"));
  }

  @And("I confirm the deletion in the popup")
  public void confirmTheDeletionInThePopup() {
    webElementHelper.waitForElementToBeVisible("button#confirmBtn");
    webElementHelper.clickButton("button#confirmBtn");
  }

  @And("I submit the registration form")
  public void submitTheRegistrationForm() {
    fillRegistrationForm(
        RandomGenerator.getDefault().nextInt(0, 1000) + "@gmail.com", "Ab01234567", "Ab01234567");
  }

  @And("I submit the registration form with not matching passwords")
  public void submitTheRegistrationFormWithNotMatchingPasswords() {
    fillRegistrationForm("test@test", "01234567", "12345678");
  }

  private void fillSearchBar(
      String origin, String destination, String departureDate, String returnDate) {
    webElementHelper.fillInputField("input#originCity", origin);
    webElementHelper.fillInputField("input#destinationCity", destination);
    webElementHelper.fillInputField("input#outGoingDate", departureDate);
    webElementHelper.fillInputField("input#inGoingDate", returnDate);
  }

  private List<Integer> getPrices() {
    return webElementHelper.getElementsTextInList("span[name='price']").stream()
        .map(s -> s.replace(" ", ""))
        .map(Integer::valueOf)
        .toList();
  }

  private List<Integer> getDurations() {
    return webElementHelper.getElementsTextInList("h5[name='durationAndTransfers']").stream()
        .map(s -> s.split(" "))
        .map(
            s -> {
              int hours = Integer.parseInt(s[0]);
              int minutes = Integer.parseInt(s[2]);
              return hours * 60 + minutes;
            })
        .toList();
  }

  private List<Integer> getTransferNumbers() {
    return webElementHelper.getElementsTextInList("h5[name='durationAndTransfers']").stream()
        .map(s -> s.split(" ")[4])
        .map(Integer::valueOf)
        .toList();
  }

  private void fillRegistrationForm(String email, String password, String rePassword) {
    webElementHelper.fillInputField("input#username", email);
    webElementHelper.fillInputField("input#password", password);
    webElementHelper.fillInputField("input#password-confirm", rePassword);
    webElementHelper.fillInputField("input#email", email);
    webElementHelper.fillInputField("input#firstName", email);
    webElementHelper.fillInputField("input#lastName", email);
    webElementHelper.clickButton("input[value='Register']");
  }
}
