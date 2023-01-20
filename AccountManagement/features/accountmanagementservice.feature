Feature: Account Management Service

# @author: Joao Silva s222961

# ------------------------ Account Registration and Unregistration ------------------------

Scenario: Register and Unregister customer are successful
    Given a customer that is not registered with DTU Pay
    When a register event for the customer is received
    #Then a TokenUserRequest event is sent
    #And a successful "RegisterUserTokenSuccess" event is received
    Then a success register event is sent
    And an unregister event for the customer is received
    And an unregister success event is sent


Scenario: Register and Unregister customer are unsuccessful
    Given a customer that is not registered with DTU Pay
    When a register event for the customer is received
    Then a failure register event is sent
    And an unregister event for the customer is received
    And an unregister failure event is sent

# ------------------------ Bank Account Request------------------------


Scenario: Bank Account Request is successful
    Given a user called "Tiago" is registered at DTU Pay
    When a successful "BankAccountIdRequest" event is received asking for bank account
    Then a success "BankAccountIdRequestCompleted" event is sent for the payment service


Scenario: Bank Account Request is unsuccessful
    When a unsuccessful "BankAccountIdRequest" event is received asking for a not existing bank account
    Then a success "BankAccountIdRequestFailed" event is sent for the payment service failing
