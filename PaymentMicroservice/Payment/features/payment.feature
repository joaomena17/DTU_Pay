Feature: Payment
  Scenario: Payment is successful
    Given A request for payment is sent by merchant with id "mid", token "token", amount 1000
    Then The validatetoken event is pushed
    When The token is validated and returns the userid "userId"
    Then The Get_bank_accountId_request event is pushed to get the bank account id
    When The account is verified and returns "customerBankId" and payment is created
    Then The success event is pushed

    Scenario: Payment is unsuccessful due to token
      Given A request for payment is sent by merchant with id "mid", token "token", amount 1000
      Then The validatetoken event is pushed
      When the token is validated and returns the validateTokenFailed event
      Then The failed event is pushed with error message "error"

  Scenario: Payment is unsuccessful due to bank
    Given A request for payment is sent by merchant with id "mid", token "token", amount 1000
    Then The validatetoken event is pushed
    When The token is validated and returns the userid "userId"
    Then The Get_bank_accountId_request event is pushed to get the bank account id
    When The account is verified and returns "customerBankId" and payment is created
    Then The failed event is pushed with error message "error"

