Feature: Flight search functionality

  @search-for-return-flight
  Scenario: Search for return flight
    Given I am on the search page
    When I search for a return flight from "London" to "New York" with valid dates
    Then 100 flights should be displayed

  @search-for-one-way-flight
  Scenario: Search for one way flight
    Given I am on the search page
    When I search for a one way flight from "Budapest" to "Barcelona" with valid date
    Then 100 flights should be displayed

  @search-with-missing-fields
  Scenario Outline: If not every required field is filled, an error message is displayed
    Given I am on the search page
    When I search for a return flight from "<origin>" to "<destination>" from "<departureDate>" until "<returnDate>"
    Then an error message should be displayed

    Examples:
      | origin | destination | departureDate | returnDate |
      |        |             |               |            |
      | London |             |               |            |
      | London | Budapest    |               |            |
      | London | Budapest    | 2026-03-30    |            |

@advanced-search
  Scenario: Search for return flight for 1 adult 1 child and 1 infant
    Given I am on the search page
    When I use advanced search for a return flight from "London" to "New York" with valid dates
    Then I see offers for 1 adult, 1 child and 1 infant
