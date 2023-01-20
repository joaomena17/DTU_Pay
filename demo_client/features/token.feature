### Author: Tiago Machado s222963###

Feature: Token test

  Scenario: Get tokens as a customer
    Given the customer "Luis" "Andrade" with CPR "93109085454" has a bank account with balance 1000
    And the customer registers at DTUPay
    When the customer asks for 5 tokens
    Then the customer receives 5 tokens


  Scenario: Get tokens as a customer that already has too many
    Given the customer "Clara" "Silva" with CPR "090801" has a bank account with balance 1000
    And the customer registers at DTUPay
    When the customer asks for 2 tokens
    Then the customer receives 2 tokens
    When the customer asks for 5 tokens
    Then the customer receives an error message "Customer can't have that many tokens"

  Scenario: Get tokens as a customer that already has too many
    Given the customer "Francisca" "Alves" with CPR "050989" has a bank account with balance 1000
    And the customer registers at DTUPay
    When the customer asks for 6 tokens
    Then the customer receives an error message "Customer can only request 5 tokens"