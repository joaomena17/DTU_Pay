Feature: ReportFeature
  Scenario: Return customer report
    Given there is a registered payment with customer id cid and merchant id mid
    When a customer requests a report
    Then the report is created containing only customers payments is created
    And event CustomerReportReturnEvent is sent
