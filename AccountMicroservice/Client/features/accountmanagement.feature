Feature: Account Management

Scenario: Customer registers and unregisters successfully
    Given a customer with name "John" "Doe" and bank account with balance 1000
    When the customer registers with DTU Pay using "json"
    Then the register customer request is successful
    And the customer unregisters from DTU Pay using "json"
    And the unregister customer request is successful

Scenario: Merchant registers and unregisters successfully
    Given a merchant with name "John" "Doe" and bank account with balance 1000
    When the merchant registers with DTU Pay using "json"
    Then the register merchant request is successful
    And the merchant unregisters from DTU Pay using "json"
    And the unregister merchant request is successful
