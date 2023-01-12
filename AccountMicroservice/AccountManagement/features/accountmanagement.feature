Feature: Account Management

Scenario: Customer registers and unregisters successfully
    Given a customer with name "John" "Doe" and bank account with balance 1000
    When the customer registers with DTU Pay
    Then the customer is saved in the customer list
    And the customer can be retrieved from the customer list
    And the customer unregisters from DTU Pay
    And the customer is removed from the customer list
