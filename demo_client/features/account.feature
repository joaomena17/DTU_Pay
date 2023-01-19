### Author: Tiago Machado s222963 ###

Feature: Account

  Scenario: Create customer DTUPay account
    Given the customer "John" "Johansen" with CPR "1304560908" has a bank account with balance 1000
    When the customer registers at DTUPay
    Then the customer account is created




