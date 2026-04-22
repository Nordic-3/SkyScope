@profile-page-tests
Feature: profile page functionality

  @delete-account
  Scenario: account deletion
    Given I am on profile page after successful login
    When I click on delete account button
    And I confirm the deletion in the popup
    Then my account should be deleted

  @update-password
  Scenario: password update
    Given I am on profile page after successful login
    When I fill password update form with "Cd01234567" and "Cd01234567"
    Then my password should be updated successfully

  @update-password-invalid-datas
  Scenario: password update with invalid data
    Given I am on profile page after successful login
    When I fill password update form with "Cd01234567" and "different01"
    Then the password update is not successful
