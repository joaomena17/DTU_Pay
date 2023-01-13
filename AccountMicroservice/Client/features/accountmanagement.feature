Feature: Account Management

Scenario: Customer registers and unregisters successfully
    Given a customer with name "John" "Doe" and bank account with balance 1000
    When the customer registers with DTU Pay using "json"
    Then the register request is successful
    And the customer unregisters from DTU Pay using "json"
    And the unregister request is successful
