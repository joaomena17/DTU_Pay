Feature: Account Management

# ----------------------------- Customer -----------------------------

# ------------------------ Customer Registration ------------------------

Scenario: Customer registers and unregisters successfully
    Given a customer with name "Joao" "Afonso" and bank account with balance 1000
    When the customer registers with DTU Pay
    Then the customer is saved in the customer list
    And the customer can be retrieved from the customer list
    And the customer unregisters from DTU Pay
    And the customer is removed from the customer list

Scenario: Register customer is successful
    When a succsessful "RegisterAccountRequest" event for a customer "Joao" "Afonso" with balance 1000 is received
    Then a success "RegisterAccountRequestCompleted" event is sent
    And the customer is registered

Scenario: Register customer is unsuccessful
    When an unsuccsessful "RegisterAccountRequest" event for a customer "Joao" "Afonso" with balance 1000 is received
    Then a failure "RegisterAccountRequestFailed" event is sent
    And the customer is not registered


# ------------------------ Customer Unregistration ------------------------

#Scenario: Unregister customer is successful
#    Given a customer that is registered with DTU Pay that succeeds in unregistering
#    When a succsessful "UnregisterAccountRequested" unregister event for the customer is received
#    Then a success "UnregisterAccountSuccess" event is sent
#    And the customer is unregistered

#Scenario: Unregister customer is unsuccessful
#    Given a customer that is registered with DTU Pay
#    When an unsuccsessful "UnregisterAccountRequested" unregister event for a customer "Joao" "Afonso" is received
#    Then a failure "UnregisterAccountRequestFailed" event is sent
#    And the customer is not unregistered


# ----------------------------- Merchant -----------------------------

# ------------------------ Merchant Registration ------------------------

Scenario: Merchant registers and unregisters successfully
    Given a merchant with name "Tiago" "Silverio" and bank account with balance 1000
    When the merchant registers with DTU Pay
    Then the merchant is saved in the merchant list
    And the merchant can be retrieved from the merchant list
    And the merchant unregisters from DTU Pay
    And the merchant is removed from the merchant list

Scenario: Register merchant is successful
    When a succsessful "RegisterAccountRequest" event for a merchant "Joao" "Silva" is received
    Then a success "RegisterAccountRequestCompleted" event is sent
    And the merchant is registered

Scenario: Register merchant is unsuccessful
    When an unsuccsessful "RegisterAccountRequest" event for a merchant "Joao" "Silva" is received
    Then a failure "RegisterAccountRequestFailed" event is sent
    And the merchant is not registered

# ------------------------ Merchant Unregistration ------------------------

Scenario: Unregister merchant is successful
    Given a merchant that is registered with DTU Pay that succeeds in unregistering
    When a succsessful "UnregisterAccountRequested" unregister event for a registered merchant is received
    Then a success "UnregisterAccountSuccess" event is sent
    And the merchant is unregistered

Scenario: Unregister merchant is unsuccessful
    Given a merchant that is registered with DTU Pay that fails to register
    When an unsuccsessful "UnregisterAccountRequested" unregister event for a registered merchant is received
    Then a failure "UnregisterAccountRequestFailed" event is sent
    And the merchant is not unregistered
