package com.szte.skyScope.stepDefinitions;

import static org.junit.jupiter.api.Assertions.*;

import com.szte.skyScope.dataStore.DataStore;
import com.szte.skyScope.enums.FlightOffersSortOptions;
import com.szte.skyScope.helper.WebElementHelper;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.util.List;
import java.util.random.RandomGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class StepDefinitions {
  private final WebDriver driver = new FirefoxDriver();
  private final WebElementHelper webElementHelper = new WebElementHelper(driver);
  private final DataStore dataStore = new DataStore();

  @Given("I am on the search page")
  public void onTheSearchPage() {
    driver.get("http://localhost:8080/search");
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
    webElementHelper.clickButton(By.id("book"));
  }

  @Given("I have selected a flight from {string} to {string} and logged in")
  public void selectFlightAndLogin(String origin, String destination) {
    haveSearchedForReturnFlightWithValidDates(origin, destination);
    webElementHelper.clickButton(By.id("book"));
    enterValidCredentials();
  }

  @Given("I am on the payment page")
  public void onThePaymentPage() {
    selectFlightAndLogin("Budapest", "London");
    fillTheTravellerDetails();
    webElementHelper.waitForElementToBeVisible(By.id("payBtn"));
    webElementHelper.clickButton(By.id("payBtn"));
  }

  @Given("I am on profile page after successful login")
  public void onProfilePageAfterSuccessfulLogin() {
    driver.get("http://localhost:8080/profile");
    webElementHelper.waitForElementToBeVisible(By.id("reg"));
    onLoginPageClickRegistrationButton();
    submitTheRegistrationForm();
    webElementHelper.waitForElementToBeVisible(By.cssSelector(".card-title"));
  }

  @When("I click on delete account button")
  public void clickOnDeleteAccountButton() {
    webElementHelper.clickButton(By.id("deleteBtn"));
  }

  @When("I fill password update form with {string}, {string} and {string}")
  public void fillPasswordUpdateForm(
      String currentPassword, String newPassword, String rePassword) {
    webElementHelper.fillInputField(By.id("currentPassword"), currentPassword);
    webElementHelper.fillInputField(By.id("newPassword"), newPassword);
    webElementHelper.fillInputField(By.id("rePassword"), rePassword);
    webElementHelper.clickButton(By.id("pswChange"));
  }

  @When("I filter results by max price option")
  public void filterResultsByMaxPriceOption() {
    int maxPrice =
        Integer.parseInt(
            String.join(
                "",
                webElementHelper
                    .getValueOfAnAttribute(By.cssSelector("input[aria-label='Max']"), "placeholder")
                    .split("Ft")[0]
                    .split(" ")));
    dataStore.putData("filteredPrice", Integer.toString(maxPrice / 2));
    webElementHelper.fillInputField(
        By.cssSelector("input[aria-label='Max']"),
        dataStore.getValue("filteredPrice", String.class));
    webElementHelper.clickButton(By.id("filterBtn"));
  }

  @When("I filter results by number of transfers")
  public void filterResultsByNumberOfTransfers() {
    dataStore.putData(
        "transferNumber",
        webElementHelper.getFirstNotDefaultValueOfAnSelectOption(By.id("transferNumber")));
    webElementHelper.selectOptionFromAllDropDownsByValue(
        By.id("transferNumber"), dataStore.getValue("transferNumber", String.class));
    webElementHelper.clickButton(By.id("filterBtn"));
  }

  @When("I filter results by layover duration")
  public void filterResultsByLayoverDuration() {
    webElementHelper.selectOptionFromAllDropDownsByValue(
        By.id("transferDuration"),
        webElementHelper.getFirstNotDefaultValueOfAnSelectOption(By.id("transferDuration")));
    dataStore.putData(
        "transferDuration",
        webElementHelper
            .getElementsTextInList(By.cssSelector("#transferDuration > option:checked"))
            .getFirst());
    webElementHelper.clickButton(By.id("filterBtn"));
  }

  @When("I filter results by airlines")
  public void filterResultsByAirlines() {
    webElementHelper.clickButton(By.cssSelector("div[aria-controls='airlineFilter']"));
    webElementHelper.checkCheckbox(By.name("airlines"));
    dataStore.putData(
        "filteredAirline",
        webElementHelper
            .getElementsTextInList(By.cssSelector("input[name='airlines']:checked ~ label"))
            .getFirst());
    webElementHelper.clickButton(By.id("filterBtn"));
  }

  @When("I filter results by airplane")
  public void filterResultsByAirplane() {
    webElementHelper.clickButton(By.cssSelector("div[aria-controls='airplaneFilter']"));
    webElementHelper.checkCheckbox(By.name("airplanes"));
    dataStore.putData(
        "filteredAirplane",
        webElementHelper
            .getElementsTextInList(By.cssSelector("input[name='airplanes']:checked ~ label"))
            .getFirst());
    webElementHelper.clickButton(By.id("filterBtn"));
  }

  @When("I delete applied filters")
  public void deleteAppliedFilters() {
    webElementHelper.clickButton(By.id("deleteFilterBtn"));
  }

  @When("I fill in the payment details with valid information")
  public void fillThePaymentDetails() {
    webElementHelper.fillInputField(By.id("email"), "automataTest@test.hu");
    webElementHelper.fillInputField(By.id("cardNumber"), "4242 424242424242");
    webElementHelper.fillInputField(By.id("cardExpiry"), "09/30");
    webElementHelper.fillInputField(By.id("cardCvc"), "999");
    webElementHelper.fillInputField(By.id("billingName"), "Test Elek");
    webElementHelper.clickButton(By.cssSelector("button[type='submit']"));
  }

  @When("I fill in the traveller details with valid information")
  public void fillTheTravellerDetails() {
    webElementHelper.fillAllinputFieldsByLocator(
        By.cssSelector("input[placeholder='Vezetéknév']"), "Test");
    webElementHelper.fillAllinputFieldsByLocator(
        By.cssSelector("input[placeholder='Keresztnév']"), "Elek");
    webElementHelper.fillAllinputFieldsByLocator(
        By.cssSelector("input[placeholder='születési dátum']"), "1990-01-01");
    webElementHelper.selectOptionFromAllDropDownsByValue(
        By.cssSelector("#gender > select"), "MALE");
    webElementHelper.fillInputField(By.id("countryCode"), "36");
    webElementHelper.fillInputField(By.id("phoneNumber"), "123456789");
    webElementHelper.selectOptionFromAllDropDownsByValue(
        By.cssSelector("#docType > select"), "PASSPORT");
    webElementHelper.fillAllinputFieldsByLocator(
        By.cssSelector("input[placeholder='Okmány száma']"),
        Integer.toString(RandomGenerator.getDefault().nextInt(100000, 999999)));
    webElementHelper.fillAllinputFieldsByLocator(
        By.cssSelector("input[placeholder='kiállítás dátuma']"), "2020-01-01");
    webElementHelper.fillAllinputFieldsByLocator(
        By.cssSelector("input[placeholder='lejárat dátuma']"), "2030-01-01");
    webElementHelper.fillAllinputFieldsByLocator(
        By.cssSelector("input[placeholder='Kiállító ország kódja (pl. HU)']"), "hu");
    webElementHelper.fillAllinputFieldsByLocator(
        By.cssSelector("input[placeholder='Kiállítás helye']"), "Budapest");
    webElementHelper.fillAllinputFieldsByLocator(
        By.cssSelector("input[placeholder='Születési hely']"), "Budapest");
    webElementHelper.fillAllinputFieldsByLocator(
        By.cssSelector("input[placeholder='Állampolgárság (pl. HU)']"), "hu");
    webElementHelper.clickButton(By.id("next"));
  }

  @When("I fill in the traveller details with missing information")
  public void fillTheTravellerDetailsWithMissingInformation() {
    webElementHelper.fillAllinputFieldsByLocator(
        By.cssSelector("input[placeholder='Vezetéknév']"), "Test");
    webElementHelper.fillAllinputFieldsByLocator(
        By.cssSelector("input[placeholder='Keresztnév']"), "Elek");
    webElementHelper.clickButton(By.id("next"));
  }

  @When("I am on login page I click the registration button")
  public void onLoginPageClickRegistrationButton() {
    webElementHelper.clickButton(By.id("reg"));
  }

  @When("I search for a return flight from {string} to {string} with valid dates")
  public void searchForReturnFlightWithValidDates(String origin, String destination) {
    fillSearchBar(
        origin,
        destination,
        LocalDate.now().plusDays(15).toString(),
        LocalDate.now().plusDays(25).toString());
    webElementHelper.clickButton(By.id("searchFlightsButton"));
  }

  @When("I search for a one way flight from {string} to {string} with valid date")
  public void searchForOneWayFlightWithValidDate(String origin, String destination) {
    webElementHelper.fillInputField(By.id("originCity"), origin);
    webElementHelper.fillInputField(By.id("destinationCity"), destination);
    webElementHelper.checkCheckboxById("onlyOneWay");
    webElementHelper.fillInputField(By.id("outGoingDate"), LocalDate.now().plusDays(15).toString());
    webElementHelper.clickButton(By.id("searchFlightsButton"));
  }

  @When("I search for a return flight from {string} to {string} from {string} until {string}")
  public void searchForReturnFlightWithGivenInputs(
      String origin, String destination, String departureDate, String returnDate) {
    fillSearchBar(origin, destination, departureDate, returnDate);
    webElementHelper.clickButton(By.id("searchFlightsButton"));
  }

  @When("I use advanced search for a return flight from {string} to {string} with valid dates")
  public void advancedSearchForReturnFlightWithValidDates(String origin, String destination) {
    fillSearchBar(
        origin,
        destination,
        LocalDate.now().plusDays(15).toString(),
        LocalDate.now().plusDays(25).toString());
    webElementHelper.clickButton(By.id("advancedSearchButton"));
    webElementHelper.fillInputField(By.id("numberOfChildren"), "1");
    webElementHelper.fillInputField(By.id("numberOfInfants"), "1");
    webElementHelper.clickButton(By.id("searchFlightsButton"));
  }

  @When("I sort the results by {string}")
  public void sortTheResultsBy(String sortBy) {
    switch (FlightOffersSortOptions.getOptionFromValue(sortBy)) {
      case PRICE_ASC -> webElementHelper.clickButton(By.id("priceAsc"));
      case PRICE_DSC -> webElementHelper.clickButton(By.id("priceDsc"));
      case FLYTIME_ASC -> webElementHelper.clickButton(By.id("flyTimeAsc"));
      case FLYTIME_DSC -> webElementHelper.clickButton(By.id("flyTimeDsc"));
      case TRANSFERTIME_ASC -> webElementHelper.clickButton(By.id("transferTimeAsc"));
      case TRANSFERTIME_DSC -> webElementHelper.clickButton(By.id("transferTimeDsc"));
    }
    flightsShouldBeDisplayed(100);
  }

  @When("I am on login page I enter valid credentials")
  public void enterValidCredentials() {
    webElementHelper.fillInputField(By.id("email"), "automataTest@test.hu");
    webElementHelper.fillInputField(By.id("password"), "automatatest");
    webElementHelper.clickButton(By.id("login"));
  }

  @When("I am on login page I enter invalid credentials")
  public void enterInvalidCredentials() {
    webElementHelper.fillInputField(By.id("email"), "invalid@invalid");
    webElementHelper.fillInputField(By.id("password"), "invalid");
    webElementHelper.clickButton(By.id("login"));
  }

  @Then("I should see cheaper or equals offers than the specified max price")
  public void shouldSeeCheaperOrEqualsOffersThanTheSpecifiedMaxPrice() {
    int filteredPrice = Integer.parseInt(dataStore.getValue("filteredPrice", String.class));
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
    webElementHelper.clickAllButton(By.name("details"));
    webElementHelper
        .getElementsTextInList(By.name("layoverTime"))
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
        .getElementsByLocator(By.name("details"))
        .forEach(
            element -> {
              element.click();
              assertTrue(
                  webElementHelper.getElementsTextInList(By.name("carrierAirline")).stream()
                      .anyMatch(
                          s -> s.equals(dataStore.getValue("filteredAirline", String.class))));
              element.click();
            });
  }

  @Then("I should see offers with at least one flight of the specified airplane type")
  public void shouldSeeOffersWithAtLeastOneFlightOfTheSpecifiedAirplaneType() {
    webElementHelper
        .getElementsByLocator(By.name("details"))
        .forEach(
            element -> {
              element.click();
              assertTrue(
                  webElementHelper.getElementsTextInList(By.name("aircraftType")).stream()
                      .anyMatch(
                          s -> s.equals(dataStore.getValue("filteredAirplane", String.class))));
              element.click();
            });
  }

  @Then("I should see the success confirmation message")
  public void shouldSeeTheSuccessConfirmationMessage() {
    webElementHelper.waitForTextInElement(By.id("successModel"), "Sikeres foglalás");
    assertTrue(webElementHelper.isTextVisibleInElement(By.id("successModel"), "Sikeres foglalás"));
  }

  @Then("I should see the sumumary page")
  public void shouldSeeTheSummaryPage() {
    webElementHelper.waitForTextInElement(By.id("payBtn"), "Fizetés");
    assertTrue(webElementHelper.isTextVisibleInElement(By.id("payBtn"), "Fizetés"));
  }

  @Then("I should see the payment page")
  public void shouldSeeThePaymentPage() {
    webElementHelper.waitForTextInElement(
        By.cssSelector("div[class='PaymentHeader] > div'"), "Pay with card");
    assertTrue(
        webElementHelper.isTextVisibleInElement(
            By.cssSelector("div[class='PaymentHeader] > div'"), "Pay with card"));
  }

  @Then("I should see error message for missing fields")
  public void shouldSeeErrorMessageForMissingFields() {
    assertTrue(webElementHelper.isElementDisplayed(By.className("alert-danger")));
  }

  @Then("{int} flights should be displayed")
  public void flightsShouldBeDisplayed(int numberOfResults) {
    webElementHelper.waitForGivenNumberOfElements(By.name("resultCard"), numberOfResults);
  }

  @Then("an error message should be displayed")
  public void errorMessageShouldBeDisplayed() {
    assertTrue(webElementHelper.isElementDisplayed(By.className("text-danger")));
  }

  @Then("I see offers for 1 adult, 1 child and 1 infant")
  public void seeOffersForAllTypeOfPassengers() {
    webElementHelper.waitForGivenNumberOfElements(By.name("details"), 100);
    webElementHelper.clickAllButton(By.name("details"));
    webElementHelper.waitForTextInElement(By.cssSelector("div[class='collapse show']"), "Felnőtt");
    webElementHelper.waitForTextInElement(By.cssSelector("div[class='collapse show']"), "Gyerek");
    webElementHelper.waitForTextInElement(
        By.cssSelector("div[class='collapse show']"), "Csecsemő, ölben");
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
    assertFalse(webElementHelper.isElementDisplayed(By.className("text-danger")));
  }

  @Then("the registration is not successful")
  public void theRegistrationIsNotSuccessful() {
    webElementHelper.waitForElementToBeVisible(By.cssSelector("div#registration .text-danger"));
    assertTrue(
        webElementHelper.isElementDisplayed(By.cssSelector("div#registration .text-danger")));
  }

  @Then("the login is not successful")
  public void loginIsNotSuccessful() {
    webElementHelper.waitForElementToBeVisible(By.cssSelector("div[id='error']"));
    assertTrue(webElementHelper.isElementDisplayed(By.cssSelector("div[id='error']")));
  }

  @Then("my account should be deleted")
  public void myAccountShouldBeDeleted() {
    webElementHelper.waitForTextInElement(By.cssSelector("a[href='/flighttracker']"), "Explore");
    assertTrue(webElementHelper.isElementDisplayed(By.cssSelector("a[href='/flighttracker']")));
  }

  @Then("my password should be updated successfully")
  public void myPasswordShouldBeUpdatedSuccessfully() {
    webElementHelper.waitForElementToBeVisible(By.cssSelector(".text-success"));
    assertTrue(webElementHelper.isElementDisplayed(By.cssSelector(".text-success")));
  }

  @And("I confirm the deletion in the popup")
  public void confirmTheDeletionInThePopup() {
    webElementHelper.waitForElementToBeVisible(By.id("confirmPassword"));
    webElementHelper.fillInputField(By.id("confirmPassword"), "Ab01234567");
    webElementHelper.clickButton(By.id("confirmBtn"));
  }

  @And("I submit the registration form")
  public void submitTheRegistrationForm() {
    fillRegistrationForm(
        RandomGenerator.getDefault().nextInt(0, 1000) + "@gmail.com", "Ab01234567", "Ab01234567");
  }

  @And("I submit the registration form with too short password")
  public void submitTheRegistrationFormWithInvalidData() {
    fillRegistrationForm("invalid@invalid", "0123", "0123");
  }

  @And("I submit the registration form with not matching passwords")
  public void submitTheRegistrationFormWithNotMatchingPasswords() {
    fillRegistrationForm("test@test", "01234567", "12345678");
  }

  @And("I submit the registration form with weak password")
  public void submitTheRegistrationFormWithWeakPassword() {
    fillRegistrationForm("test@test", "01234567", "01234567");
  }

  @After
  public void closeBrowser() {
    driver.quit();
  }

  private void fillSearchBar(
      String origin, String destination, String departureDate, String returnDate) {
    webElementHelper.fillInputField(By.id("originCity"), origin);
    webElementHelper.fillInputField(By.id("destinationCity"), destination);
    webElementHelper.fillInputField(By.id("outGoingDate"), departureDate);
    webElementHelper.fillInputField(By.id("inGoingDate"), returnDate);
  }

  private List<Integer> getPrices() {
    return webElementHelper.getElementsTextInList(By.name("price")).stream()
        .map(s -> s.replace(" ", ""))
        .map(Integer::valueOf)
        .toList();
  }

  private List<Integer> getDurations() {
    return webElementHelper.getElementsTextInList(By.name("durationAndTransfers")).stream()
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
    return webElementHelper.getElementsTextInList(By.name("durationAndTransfers")).stream()
        .map(s -> s.split(" ")[4])
        .map(Integer::valueOf)
        .toList();
  }

  private void fillRegistrationForm(String email, String password, String rePassword) {
    webElementHelper.fillInputField(By.id("signupEmail"), email);
    webElementHelper.fillInputField(By.id("signupPassword"), password);
    webElementHelper.fillInputField(By.id("signupRePassword"), rePassword);
    webElementHelper.checkCheckbox(By.id("gdpr"));
    webElementHelper.clickButton(By.id("signup"));
  }
}
