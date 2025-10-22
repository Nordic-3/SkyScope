@traveller-detail-form
Feature: traveller detail form

  @traveller-detail-form-valid-inputs
  Scenario: Fill in traveller details and proceed to payment
    Given I have selected a flight from "London" to "New York" and logged in
    When I fill in the traveller details with valid information
    Then I should see the sumumary page

    @traveller-detail-form-missing-inputs
    Scenario: Fill in traveller details with missing inputs
      Given I have selected a flight from "London" to "New York" and logged in
      When I fill in the traveller details with missing information
      Then I should see error message for missing fields