
Feature: BankId request for payment

Scenario: Bank Account Request is successful
Given a customer "Ana" that is not registered with DTU Pay
And a register request event is received
When a event is received asking for user bank account
Then a "BankAccountIdRequestCompleted" event is sent for the payment service


Scenario: Bank Account Request is unsuccessful
Given a customer "" that is not registered with DTU Pay
And a register request event is received
When a event is received asking for user bank account
Then a "BankAccountIdRequestFailed" event is sent for the payment service