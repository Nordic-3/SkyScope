@fill-payment-page
Feature: payment page

  Scenario: Fill in payment details with valid inputs
    Given I am on the payment page
    When I fill in the payment details with valid information
    Then I should see the success confirmation message
