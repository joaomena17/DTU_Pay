### Author: Tiago Machado s222963 ###

Feature:Report
  @DontRun
  Scenario: Customer asks for report
  Given the user has a DTUPay account with id "123"
  When the customer asks for a report
  Then the event "generateCustomerReport" is published asking for report
  When the costumer report is received from the report service
  Then the report is created
  @DontRun
  Scenario: Merchant asks for report
    Given the user has a DTUPay account with id "456"
    When the merchant asks for a report
    Then the event "generateMerchantReport" is published asking for report
    When the merchant report is received from the report service
    Then the report is created

  @DontRun
  Scenario: Manager asks for report
    When the manager asks for a report
    Then the event "generateManagerReport" is published asking for manager report
    When the manager report is received from the report service
    Then the report is created