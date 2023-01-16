Feature: tokenMicroservice

Scenario: Customer requests token successfully
    Given a customer "John Doe" creates an account on DTU PAY and the token micro service creates him as a user
    When a customer with name "John Doe" requests 1 tokens
    Then a customer with name "John Doe" requests 3 more tokens

Scenario: Customer requests token while having 2 token
    Given a customer "Jonas Doe" has an account on DTU pay with 2 token
    When the customer "Jonas Doe" request 1 token
    Then customer "Jonas Doe" does not receive more tokens

Scenario: Customer request to few tokens
    Given Customer "Jon Doe" has an account and 1 tokens
    When The customer "Jon Doe" requests to few or 0 tokens

Scenario: Customer requests a token, uses the token, the token is marked used and then the customer tries to use it again
    Given Customer "Johann Doe" creates a new account
    Then The customer "Johann Doe" requests 1 new token
    Then The customer "Johann Doe" requests the token, uses the token and checks if he gets the same token again
