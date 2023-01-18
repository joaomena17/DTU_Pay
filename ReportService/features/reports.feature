Feature: ReportFeature

  Scenario: Add payment to repository
    Given there is an unregistered payment of 10 kr with customer "cid1" and merchant "mid1"
    When a "RequestPaymentCompleted" event is received to complete a payment
    Then the payment of 10 kr from "cid1" to "mid1" is added to the repository

  #Scenario: Add payment to repository with cid2 and mid2
  #  Given there is an unregistered payment of 20 kr with customer "cid2" and merchant "mid2"
  #  When a "RequestPaymentCompleted" event is received to complete a payment
  #  Then the payment of 20 kr from "cid2" to "mid2" is added to the repository

  Scenario: Return customer report
    Given there is a registered payment of 20 kr with customer "cid2" and merchant "mid2"
    When a "generateCustomerReport" event is received for customer report
    Then the report is created containing only customer "cid2"'s payments
    And event "CustomerReportReturnEvent" is sent with the list of customer "cid2"'s payments

  Scenario: Return merchant report
    Given there is a registered payment of 30 kr with customer "cid3" and merchant "mid3"
    When a "generateMerchantReport" event is received for merchant report
    Then the report is created containing only merchant "mid3"'s received payments
    And event "MerchantReportReturnEvent" is sent with the list of merchant "mid3"'s received payments

  Scenario: Return manager report
    Given there is a registered payment of 40 kr with customer "cid4" and merchant "mid4"
    And there is a registered payment of 50 kr with customer "cid5" and merchant "mid5"
    When a "generateManagerReport" event is received for manager report
    Then the report is created containing customer "cid4" and "cid5"'s payments
    And event "ManagerReportReturnEvent" is sent with the list of customer "cid4" and "cid5"'s payments




