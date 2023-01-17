Feature: Account Management Service

# ----------------------------- Customer -----------------------------

# ------------------------ Customer Unregistration ------------------------

Scenario: Unregister customer is successful
    Given a customer that is registered with DTU Pay that succeeds in unregistering
    When a successful "UnregisterAccountRequest" unregister event for the customer is received
    Then a success "UnregisterAccountSuccess" event is sent
    And the customer is unregistered

Scenario: Unregister customer is unsuccessful
    Given a customer that is registered with DTU Pay that fails to unregister
    When an unsuccsessful "UnregisterAccountRequest" unregister event for a customer is received
    Then a failure "UnregisterAccountRequestFailed" event is sent
    And the customer is not unregistered

# ------------------------ Merchant Unregistration ------------------------

Scenario: Unregister merchant is successful
    Given a merchant that is registered with DTU Pay that succeeds in unregistering
    When a succsessful "UnregisterAccountRequest" unregister event for the merchant is received
    Then a success "UnregisterAccountSuccess" event is sent
    And the merchant is unregistered

Scenario: Unregister merchant is unsuccessful
    Given a merchant that is registered with DTU Pay that fails to unregister
    When an unsuccsessful "UnregisterAccountRequest" unregister event for a merchant is received
    Then a failure "UnregisterAccountRequestFailed" event is sent
    And the merchant is not unregistered
    