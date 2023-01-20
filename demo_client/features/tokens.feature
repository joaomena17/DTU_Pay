### Author: Tiago Machado s222963 ###

Feature: Token test

  Scenario: Get tokens as a customer
    Given the customer "Cristiano" "Ronaldo" with CPR "0502871234" has a bank account with balance 100000
    And the customer registers at DTUPay
    When the customer asks for 5 tokens
    Then the customer receives 5 tokens

  Scenario: Get tokens as a customer that already has too many
    Given the customer "Dolores" "Aveiro" with CPR "0806670908" has a bank account with balance 1000
    And the customer registers at DTUPay
    When the customer asks for 3 tokens
    Then the customer receives 3 tokens
    When the customer asks for 3 tokens
    Then the customer receives an exception error with the message "Customer has too many tokens left"

  Scenario: Customer asks for too many tokens
    Given the customer "Olga" "Silva" with CPR "3103890123" has a bank account with balance 1000
    And the customer registers at DTUPay
    When the customer asks for 7 tokens
    Then the customer receives an exception error with the message "Customer can only request up to 5 tokens"