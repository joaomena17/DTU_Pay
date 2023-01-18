Feature: tokenMicroservice


  Scenario: Customer requests token while having 2 token
    Given A customer is created with the username "Jonas Doe"
    Then a customer with name "Jonas Doe" then exists

  Scenario: Customer requests token while having 2 token
    Given A customer "Jonas Doe" has an account on DTU pay with 2 token
    When The customer "Jonas Doe" request 1 token
    Then Customer "Jonas Doe" does not receive more tokens
