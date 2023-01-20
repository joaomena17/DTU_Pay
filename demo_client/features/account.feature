### Author: Tiago Machado s222963 ###

Feature: Account

  Scenario: Create customer DTUPay account
    Given the customer "John" "Johansen" with CPR "1304560908" has a bank account with balance 1000
    When the customer registers at DTUPay
    Then the customer account is created

  Scenario: Create merchant DTUPay account
    Given the merchant "Manuel" "Joaquim" with CPR "0908760908" has a bank account with balance 1000
    When the merchant registers at DTUPay
    Then the merchant account is created





