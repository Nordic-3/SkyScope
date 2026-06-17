package com.szte.skyScope.helper;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import java.util.List;

public class WebElementHelper {
  private final Page page;

  public WebElementHelper(Page page) {
    this.page = page;
  }

  public void fillInputField(String selector, String value) {
    waitForElementToBeVisible(selector);
    page.fill(selector, value);
  }

  public void clickButton(String selector) {
    page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    page.click(selector);
  }

  public void clickAllButton(String selector) {
    waitForElementToBeVisible(selector);
    page.querySelectorAll(selector).forEach(ElementHandle::click);
  }

  public void waitForGivenNumberOfElements(String selector, int numberOfElements) {
    page.waitForCondition(() -> page.querySelectorAll(selector).size() < numberOfElements);
  }

  public void checkCheckboxById(String selector) {
    page.check(selector);
  }

  public boolean isElementDisplayed(String selector) {
    page.waitForLoadState(LoadState.NETWORKIDLE);
    return page.isVisible(selector);
  }

  public void waitForTextInElement(String selector, String text) {
    page.waitForCondition(() -> page.isVisible(selector + ":has-text('" + text + "')"));
  }

  public List<String> getElementsTextInList(String selector) {
    return page.querySelectorAll(selector).stream().map(ElementHandle::innerText).toList();
  }

  public void waitForElementToBeVisible(String selector) {
    page.waitForCondition(() -> page.isVisible(selector));
  }

  public void fillAllinputFieldsByLocator(String selector, String value) {
    page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    page.querySelectorAll(selector).forEach(e -> e.fill(value));
  }

  public boolean isTextVisibleInElement(String selector, String text) {
    page.waitForLoadState(LoadState.NETWORKIDLE);
    return page.querySelector(selector).innerText().contains(text);
  }

  public void selectOptionFromAllDropDownsByValue(String selector, String value) {
    page.querySelectorAll(selector).forEach(e -> e.selectOption(value));
  }

  public String getValueOfAnAttribute(String selector, String attribute) {
    waitForElementToBeVisible(selector);
    return page.getAttribute(selector, attribute);
  }

  public String getFirstNotDefaultValueOfAnSelectOption(String selector) {
    return page.locator(selector)
        .locator("option[value]:not([value=''])")
        .first()
        .getAttribute("value");
  }

  public List<ElementHandle> getElementsBySelector(String selector) {
    return page.querySelectorAll(selector);
  }

  public void checkCheckbox(String selector) {
    page.check(selector);
  }
}
