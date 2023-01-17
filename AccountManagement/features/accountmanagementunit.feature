Feature: Account Management

# ------------------------ Account Registration and Unregistration ------------------------

"""Scenario: Customer registers and unregisters successfully
    Given a customer with name "Joao" "Afonso" and bank account with balance 1000
    When the customer registers with DTU Pay
    Then the customer is saved in the customer list
    And the customer can be retrieved from the customer list
    And the customer unregisters from DTU Pay
    And the customer is removed from the customer list"""

Scenario: Register customer is successful
    When a successful "RegisterAccountRequest" event for a customer "Joao" "Afonso" with balance 1000 is received
    And a successful "RegisterUserTokenSuccess" event is received
    Then a success "RegisterAccountRequestCompleted" event is sent

Scenario: Register customer is unsuccessful
    When an unsuccessful "RegisterAccountRequest" event for a customer "Afonso" "Joao" with balance 1000 is received
    Then a failure "RegisterAccountRequestFailed" event is sent
