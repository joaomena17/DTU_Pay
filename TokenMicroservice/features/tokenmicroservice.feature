Feature: tokenMicroservice

Scenario: Customer requests token successfully
    Given a customer "John Doe" creates an account on DTU PAY and the token micro service creates him as a user
    When a customer with name "John Doe" requests 3 tokens
