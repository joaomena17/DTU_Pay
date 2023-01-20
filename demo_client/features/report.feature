
### Author: Tiago Machado s222963  ###

Feature: Reports return
  @BankAccountRetirement
  Scenario:  Manager retrieves all transactions
    Given the customer "fdd1f111221ddf" "sad12aff121df11f23d1rgasa" with CPR "5215111111223213" has a bank account with balance 1000
    And the customer registers at DTUPay
    And the merchant "fsa7f122111137f" "fag123rafss1f1f12hf1hf1f" with CPR "141211411152" has a bank account with balance 900
    And the merchant registers at DTUPay
    And the customer asks for 5 tokens
    And the customer receives 5 tokens
    And the merchant has received a token from the customer
    And the merchant initiates a payment for 10 kr by the customer
    When the manager requests a DTU Pay report
    Then the manager gets a list with minimum 1 transaction
    When the customer requests a report
    Then the customer gets a list with 1 transaction
    When the merchant requests a report
    Then the merchant gets a list with 1 transaction



