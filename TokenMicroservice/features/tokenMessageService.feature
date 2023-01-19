Feature: tokenMicroservice


  Scenario: Customer requests token while having 2 token
    Given A customer is created with the username "Jonas Doe"
    Then a customer with name "Jonas Doe" then exists

  Scenario: Customer requests token while having 2 token
    Given A customer "John Doe" has an account on DTU pay
    When The customer "John Doe" requests 2 tokens
    Then Customer "John Doe" he should have 2 tokens
    When The customer "John Doe" requests again 2 tokens
    Then Customer "John Doe" he should still have 2 tokens

  Scenario: Customer validates a token
    Given A customer "Johanna Doe" has an account on DTU pay with 1 token
    When The customer "Johanna Doe" validates a tokens
    Then Customer "Johanna Doe" he should have 0 unused tokens