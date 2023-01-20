### Author: Tiago Machado s222963 ###

Feature: Account

  Scenario: Create customer DTUPay account
    Given the customer "Test12cCc" "Tecs12ctC" with CPR "22711321183" has a bank account with balance 1000
    When the customer registers at DTUPay
    Then the customer account is created

  Scenario: Create merchant DTUPay account
    Given the merchant "Mancu123cel" "Joaq123ccuim" with CPR "01760123908" has a bank account with balance 1000
    When the merchant registers at DTUPay
    Then the merchant account is created




