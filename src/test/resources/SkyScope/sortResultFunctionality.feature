Feature: Sort results functionality

  @sort-results
  Scenario Outline: Sort by price
    Given I have searched for a return flight from "London" to "New York" with valid dates
    When I sort the results by "<sortOption>"
    Then the results should be sorted by "<sortOption>"

    Examples:
      | sortOption      |
      | priceAsc        |
      | priceDsc        |
      | flyTimeAsc      |
      | flyTimeDsc      |
      | transferTimeAsc |
      | transferTimeDsc |
