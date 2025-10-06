package com.szte.SkyScope.StepDefinitions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.szte.SkyScope.Helper.WebElementHelper;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class StepDefinitions {
  private final WebDriver driver = new FirefoxDriver();
  private final WebElementHelper webElementHelper = new WebElementHelper(driver);

  @Given("I am on the search page")
  public void anExampleScenario() {
    driver.get("http://localhost:8080/search");
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
    webElementHelper.fillInputField("originCity", origin);
    webElementHelper.fillInputField("destinationCity", destination);
    webElementHelper.checkCheckboxById("onlyOneWay");
    webElementHelper.fillInputField("outGoingDate", LocalDate.now().plusDays(15).toString());
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
    webElementHelper.fillInputField("numberOfChildren", "1");
    webElementHelper.fillInputField("numberOfInfants", "1");
    webElementHelper.clickButton(By.id("searchFlightsButton"));
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
    webElementHelper.waitForTextInElement(By.cssSelector("div[class='collapse show']"), "ADULT");
    webElementHelper.waitForTextInElement(By.cssSelector("div[class='collapse show']"), "CHILD");
    webElementHelper.waitForTextInElement(
        By.cssSelector("div[class='collapse show']"), "HELD_INFANT");
  }

  @After
  public void closeBrowser() {
    driver.quit();
  }

  private void fillSearchBar(
      String origin, String destination, String departureDate, String returnDate) {
    webElementHelper.fillInputField("originCity", origin);
    webElementHelper.fillInputField("destinationCity", destination);
    webElementHelper.fillInputField("outGoingDate", departureDate);
    webElementHelper.fillInputField("inGoingDate", returnDate);
  }
}
