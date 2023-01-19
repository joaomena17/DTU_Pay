### Author: Tiago Machado s222963 ###

Feature: Account

  Scenario: Create customer DTUPay account
    Given the customer "test" "test" with CPR "121924" has a bank account with balance 1000
    When the customer registers at DTUPay
    Then the customer account is created




