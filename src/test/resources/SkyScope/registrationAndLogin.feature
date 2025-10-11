@registration-and-login
Feature: Registration and login

  @registration
  Scenario: user registration
    Given I have selected an flgiht offer
    When I am on login page I click the registration button
    And I submit the registration form
    Then the registration is successful

  @login
  Scenario: user login
    Given I have selected an flgiht offer
    When I am on login page I enter valid credentials
    Then the login is successful

  @invalid-registration
  Scenario: user registration with invalid data
    Given I have selected an flgiht offer
    When I am on login page I click the registration button
    And I submit the registration form with too short password
    Then the registration is not successful
    And I submit the registration form with not matching passwords
    Then the registration is not successful

  @invalid-login
  Scenario: user login with invalid data
    Given I have selected an flgiht offer
    When I am on login page I enter invalid credentials
    Then the login is not successful