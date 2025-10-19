package com.szte.SkyScope.StepDefinitions;

import static org.junit.jupiter.api.Assertions.*;

import com.szte.SkyScope.Enums.FlightOffersSortOptions;
import com.szte.SkyScope.Helper.WebElementHelper;
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
  }

  @When("I fill in the payment details with valid information")
  public void fillThePaymentDetails() {
    webElementHelper.fillInputField(By.id("cardNumber"), "4111111111111111");
    webElementHelper.fillInputField(By.id("expiry"), "12/30");
    webElementHelper.fillInputField(By.id("cvv"), "123");
    webElementHelper.fillInputField(By.id("name"), "Teszt Elek");
    webElementHelper.clickButton(By.id("payBtn"));
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

  @Then("I should see the success confirmation message")
  public void shouldSeeTheSuccessConfirmationMessage() {
    webElementHelper.waitForTextInElement(By.id("successModel"), "Sikeres foglalás");
    assertTrue(webElementHelper.isTextVisibleInElement(By.id("successModel"), "Sikeres foglalás"));
  }

  @Then("I should see the payment page")
  public void shouldSeeThePaymentPage() {
    webElementHelper.waitForTextInElement(By.className("text-info"), "Fizetendő összeg");
    assertTrue(
        webElementHelper.isTextVisibleInElement(By.className("text-info"), "Fizetendő összeg"));
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
    webElementHelper.clickButton(By.name("details"));
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

  @And("I submit the registration form")
  public void submitTheRegistrationForm() {
    fillRegistrationForm(
        RandomGenerator.getDefault().nextInt(0, 1000) + "@gmail.com", "01234567", "01234567");
  }

  @And("I submit the registration form with too short password")
  public void submitTheRegistrationFormWithInvalidData() {
    fillRegistrationForm("invalid@invalid", "0123", "0123");
  }

  @And("I submit the registration form with not matching passwords")
  public void submitTheRegistrationFormWithNotMatchingPasswords() {
    fillRegistrationForm("test@test", "01234567", "12345678");
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
    webElementHelper.clickButton(By.id("signup"));
  }
}
