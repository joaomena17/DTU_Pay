### Author: Tiago Machado s222963 ###

Feature: Account

  @DontRun
  Scenario: Create customer DTUPay account
    Given the "customer" "John" "Johansen" has a bank account
    When the user registers at DTUPay
    Then the event "RegisterAccountRequest" is published
    When the account is received from account management
    Then the account is created

  @DontRun
  Scenario: Create merchant DTUPay account
    Given the "merchant" "Manuel" "Magalhaes" has a bank account
    When the user registers at DTUPay
    Then the event "RegisterAccountRequest" is published
    When the account is received from account management
    Then the account is created

  @DontRun
  Scenario: Create account with typo on role
    Given the "customers" "Lourdes" "Maria" has a bank account
    When the user registers at DTUPay
    Then the event "RegisterAccountRequest" is published
    When the account is not received from account management
    Then the account is not created

