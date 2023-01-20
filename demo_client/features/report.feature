### Author: Tiago Machado s222963  ###

Feature: Reports return
  @BankAccountRetirement
  Scenario:  Manager retrieves all transactions
    Given the customer "Tiago" "Silverio" with CPR "1004017591" has a bank account with balance 1000
    And the customer registers at DTUPay
    And the merchant "Joana" "Cristina" with CPR "3004904343" has a bank account with balance 900
    And the merchant registers at DTUPay
    And the customer asks for 5 tokens
    And the customer receives 5 tokens
    And the merchant has received a token from the customer
    And the merchant initiates a payment for 10 kr by the customer
    When the manager requests a DTU Pay report
    Then the manager gets a list with 1 transaction
    When the customer requests a report
    Then the customer gets a list with 1 transaction
    When the merchant requests a report
    Then the merchant gets a list with 1 transaction



