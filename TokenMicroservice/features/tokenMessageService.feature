Feature: tokenMicroservice


  Scenario: Customer requests token while having 2 token
    Given a customer "Jonas Doe" has an account on DTU pay with 2 token
    When the customer "Jonas Doe" request 1 token
    Then customer "Jonas Doe" does not receive more tokens