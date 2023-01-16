Feature: Account Management

Scenario: Customer registers and unregisters successfully
    Given a customer with name "Joao" "Afonso" and bank account with balance 1000
    When the customer registers with DTU Pay
    Then the customer is saved in the customer list
    And the customer can be retrieved from the customer list
    And the customer unregisters from DTU Pay
    And the customer is removed from the customer list

Scenario: Merchant registers and unregisters successfully
    Given a merchant with name "Tiago" "Silverio" and bank account with balance 1000
    When the merchant registers with DTU Pay
    Then the merchant is saved in the merchant list
    And the merchant can be retrieved from the merchant list
    And the merchant unregisters from DTU Pay
    And the merchant is removed from the merchant list
