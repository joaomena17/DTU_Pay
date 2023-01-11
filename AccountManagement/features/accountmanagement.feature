Feature: Account Management

Scenario: Customer registers successfully
    Given a customer with name "John" "Doe" and bank account with balance 1000
    When the customer registers with DTU Pay
    Then the customer is saved in the customer list
    And the customer can be correctly retrieved from the list
