package com.szte.skyScope.helper;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebElementHelper {
  private final WebDriver driver;

  public WebElementHelper(WebDriver driver) {
    this.driver = driver;
  }

  public void fillInputField(By locator, String value) {
    waitForElementToBeVisible(locator);
    WebElement element = driver.findElement(locator);
    element.sendKeys(value);
  }

  public void clickButton(By locator) {
    new WebDriverWait(driver, Duration.ofSeconds(90))
        .until(ExpectedConditions.elementToBeClickable(locator));
    driver.findElement(locator).click();
  }

  public void clickAllButton(By locator) {
    driver.findElements(locator).forEach(WebElement::click);
  }

  public void waitForGivenNumberOfElements(By locator, int numberOfElements) {
    new WebDriverWait(driver, Duration.ofSeconds(90))
        .until(ExpectedConditions.numberOfElementsToBe(locator, numberOfElements));
  }

  public void checkCheckboxById(String id) {
    driver.findElement(By.id(id)).click();
  }

  public boolean isElementDisplayed(By locator) {
    try {
      WebElement element = driver.findElement(locator);
      return element.isDisplayed();
    } catch (Exception e) {
      return false;
    }
  }

  public void waitForTextInElement(By locator, String text) {
    new WebDriverWait(driver, Duration.ofSeconds(90))
        .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
  }

  public List<String> getElementsTextInList(By locator) {
    return driver.findElements(locator).stream().map(WebElement::getText).toList();
  }

  public void waitForElementToBeVisible(By locator) {
    new WebDriverWait(driver, Duration.ofSeconds(90))
        .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
  }

  public void fillAllinputFieldsByLocator(By locator, String value) {
    driver.findElements(locator).forEach(element -> element.sendKeys(value));
  }

  public boolean isTextVisibleInElement(By locator, String text) {
    try {
      WebElement element = driver.findElement(locator);
      return element.getText().contains(text);
    } catch (Exception e) {
      return false;
    }
  }

  public void selectOptionFromAllDropDownsByValue(By locator, String value) {
    driver.findElements(locator).forEach(element -> new Select(element).selectByValue(value));
  }

  public String getValueOfAnAttribute(By locator, String attribute) {
    waitForElementToBeVisible(locator);
    return driver.findElement(locator).getDomAttribute(attribute);
  }

  public String getFirstNotDefaultValueOfAnSelectOption(By locator) {
    return new Select(driver.findElement(locator))
        .getOptions().stream()
            .filter(webElement -> !webElement.getDomAttribute("value").isEmpty())
            .findFirst()
            .get()
            .getDomAttribute("value");
  }

  public List<WebElement> getElementsByLocator(By locator) {
    return driver.findElements(locator);
  }

  public void checkCheckbox(By locator) {
    driver.findElement(locator).click();
  }
}
