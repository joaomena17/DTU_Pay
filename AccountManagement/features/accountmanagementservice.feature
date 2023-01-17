Feature: Account Management Service

# ------------------------ Account Registration and Unregistration ------------------------

Scenario: Register and Unregister customer are successful
    Given a customer that is not registered with DTU Pay that succeeds in registering and unregistering
    When a successful "RegisterAccountRequest" register event for the customer is received
    Then a success "RegisterAccountSuccess" event is sent
    And the customer is registered
    And a successful "UnregisterAccountRequest" unregister event for the customer is received
    And a success "UnregisterAccountSuccess" event is sent
    And the customer is unregistered

Scenario: Register customer is successful and Unregister customer is unsuccessful
    Given a customer that is not registered with DTU Pay that succeeds in registering but not unregistering
    When a successful "RegisterAccountRequest" register event for the customer that cannot unregister is received
    Then a success "RegisterAccountSuccess" event is sent for the customer that cannot unregister
    And the customer that cannot unregister is registered
    And an unsuccessful "UnregisterAccountRequest" unregister event for the registered customer is received
    And a failure "UnregisterAccountFailed" event is sent to the registered customer
    And the registered customer is registered

Scenario: Register and Unregister customer are unsuccessful
    Given a customer that is not registered with DTU Pay that fails to register
    When an unsuccessful "RegisterAccountRequest" register event for the customer is received
    Then a failure "RegisterAccountSuccess" event is sent
    And the customer that cannot register is unregistered
    And an unsuccessful "UnregisterAccountRequest" unregister event for the customer is received
    And a failure "UnregisterAccountFailed" event is sent
    And the customer that could not register is unregistered

# ------------------------ Bank Account Request------------------------

Scenario: Bank Account Request is successful
    Given a user called "Tiago" is registered at DTU Pay
    When a successful "BankAccountIdRequest" event is received asking for bank account
    Then a success "BankAccountIdRequestCompleted" event is sent for the payment service

Scenario: Bank Account Request is unsuccessful
    When a unsuccessful "BankAccountIdRequest" event is received asking for a not existing bank account
    Then a success "BankAccountIdRequestFailed" event is sent for the payment service failing
