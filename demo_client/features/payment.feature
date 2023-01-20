### Author: Tiago Machaso s222963 ###

Feature: Payment
  Scenario: Create a new payment between customer and merchant
    Given the customer "Manuel" "Joaquim" with CPR "0908768787" has a bank account with balance 1000
    And the customer registers at DTUPay
    And the merchant "Jose" "Maria" with CPR "0706014563" has a bank account with balance 100
    And the merchant registers at DTUPay
    And the customer asks for 5 tokens
    And the customer receives 5 tokens
    And the merchant has received a token from the customer
    When the merchant initiates a payment for 10 kr by the customer
    Then the payment is successful