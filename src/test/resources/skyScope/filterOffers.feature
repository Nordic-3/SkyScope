@filters-functionality
Feature: Filter Offers Functionality

  @max-price-filter
  Scenario: filter offers by may price
    Given I have searched for a return flight from "London" to "New York" with valid dates
    When I filter results by max price option
    Then I should see cheaper or equals offers than the specified max price

  @fiter-by-transfers
  Scenario: filter offers by number of transfers
    Given I have searched for a return flight from "London" to "New York" with valid dates
    When I filter results by number of transfers
    Then I should see offers with less or equals transfers than the specified number

  @filter-by-layover-duration
  Scenario: filter offers by layover duration
    Given I have searched for a return flight from "London" to "New York" with valid dates
    When I filter results by layover duration
    Then I should see offers with layover duration less or equals than the specified duration

  @filter-by-airlines
  Scenario: filter offers by airlines
    Given I have searched for a return flight from "London" to "New York" with valid dates
    When I filter results by airlines
    Then I should see offers with at least one flight of the specified airlines

  @filter-by-airplane
  Scenario: filter offers by airplane
    Given I have searched for a return flight from "London" to "New York" with valid dates
    When I filter results by airplane
    Then I should see offers with at least one flight of the specified airplane type

    @delete-filters
    Scenario: delete applied filters
      Given I have searched for a return flight from "London" to "New York" with valid dates
      And I filter results by layover duration
      Then I should see offers with layover duration less or equals than the specified duration
      When I delete applied filters
      Then 100 flights should be displayed