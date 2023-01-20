### Author: Tiago Machado s222963 ###

Feature: Reports return

  Scenario:  Manager retrieves all transactions
    Given the customer "Tim" "Hansen" with CPR "099028-1006" has a bank account with balance 1000
    And the customer registers at DTUPay
    And the merchant "Tom" "Hanson" with CPR "009927-1007" has a bank account with balance 900
    And the merchant registers at DTUPay
    And the customer asks for 5 tokens
    And the customer receives 5 tokens
    And the merchant registers at DTUPay
    And the merchant initiates a payment for 10 kr by the customer
    And the merchant receives a token for the payment
    When manager requests a DTU Pay report
    Then the manager gets a list with 1 transaction
    When customer requests a report of his account
    Then the customer gets a list with 1 transaction
    When merchant requests a report of his account
    Then the merchant gets a list with 1 transaction


