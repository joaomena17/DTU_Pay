### Author: Tiago Machado s222963###

Feature: Payment
  @BankAccountRetirement
  Scenario: Create a new payment between customer and merchant
    Given the customer "Tim" "Hansen" with CPR "099030-1006" has a bank account with balance 1000
    And the customer registers at DTUPay
    And the merchant "Ole" "Frederik" with CPR "099029-1007" has a bank account with balance 2000
    And the merchant registers at DTUPay
    And the customer asks for 5 tokens
    And the customer receives 5 tokens
    And the merchant receives a token for the payment
    When the merchant initiates a payment for 10 kr by the customer
    Then the payment is successful