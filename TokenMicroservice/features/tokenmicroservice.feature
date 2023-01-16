Feature: tokenMicroservice

Scenario: Customer requests token successfully
    Given a customer "John Doe" creates an account on DTU PAY and the token micro service creates him as a user
    When a customer with name "John Doe" requests 3 tokens

Scenario: Customer requests token while having 2 token
    Given a customer "John Doe" has an account on DTU pay with 2 token
    When the customer "John Doe" request more tokens
    Then customer "John Doe" receives error that he has to many tokens to make an request

Scenario: Customer request to few tokens
    Given Customer "John Doe" has an account and 1 tokens
    When The customer "John Doe" requests 0 token
    Then The customer "John Doe" has 1 tokens and receives an error message of to few token requested
