### Author: Tiago Machado s222963 ###

  Feature: Get tokens

    @DontRun
Scenario: Get tokens as a customer
Given the customer "10980564" creates account
When the customer asks for 5 tokens
Then the event "RequestToken" is published asking for tokens
When the tokens are received from the account management
Then the tokens are received

@DontRun
Scenario: Customer asks for too many tokens
  Given the customer "78695412" creates account
  When the customer asks for 7 tokens
  Then the event "RequestToken" is published asking for tokens
  When the tokens are not received from the account management
  Then the tokens are not received
