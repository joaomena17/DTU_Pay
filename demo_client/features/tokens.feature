### Author: Tiago Machado s222963 ###

Feature: Token test

  Scenario: Get tokens as a customer
    Given the customer "Test12123er" "Tes111231121ter" with CPR "0501221334" has a bank account with balance 100000
    And the customer registers at DTUPay
    When the customer asks for 5 tokens
    Then the customer receives 5 tokens

  Scenario: Get tokens as a customer that already has too many
    Given the customer "Doloasad123aaes" "Avassd123aairo" with CPR "1121123108" has a bank account with balance 1000
    And the customer registers at DTUPay
    When the customer asks for 3 tokens
    Then the customer receives 3 tokens
    When the customer asks for 3 tokens
    Then the customer receives an exception error with the message "Tokens request not completed successfully"

  Scenario: Customer asks for too many tokens
    Given the customer "Olg123a" "Silv213a" with CPR "3103890112323" has a bank account with balance 1000
    And the customer registers at DTUPay
    When the customer asks for 7 tokens
    Then the customer receives an exception error with the message "Tokens request not completed successfully"