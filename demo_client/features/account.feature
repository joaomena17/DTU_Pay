### Author: Tiago Machado s222963 ###

Feature: Account

  Scenario: Create customer DTUPay account
    Given the customer "TestcCc" "TecsctC" with CPR "22711183" has a bank account with balance 1000
    When the customer registers at DTUPay
    Then the customer account is created

  Scenario: Create merchant DTUPay account
    Given the merchant "Mancucel" "Joaqccuim" with CPR "01760908" has a bank account with balance 1000
    When the merchant registers at DTUPay
    Then the merchant account is created




