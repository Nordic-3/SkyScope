Feature: Sort results functionality

  @sort-results
  Scenario Outline: Sort by price
    Given I have searched for a return flight from "<origin>" to "<destination>" with valid dates
    When I sort the results by "<sortOption>"
    Then the results should be sorted by "<sortOption>"

    Examples:
      | origin | destination | sortOption      |
      | London | New York    | priceAsc        |
      | London | New York    | priceDsc        |
      | London | New York    | flyTimeAsc      |
      | London | New York    | flyTimeDsc      |
      | London | New York    | transferTimeAsc |
      | London | New York    | transferTimeDsc |
