Feature: Payment
  Scenario: Successful Payment
    Given a customer with a bank account with balance 1000
    And a merchant with a bank account with balance 1000
    When the merchant initiates a payment for 100 kr by the customer
    Then the balance of the customer at the bank is 900 kr
    And the balance of the merchant at the bank is 1100 kr




