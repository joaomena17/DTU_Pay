### Author: Tiago Machado s222963 ###



Feature: Payment


  Scenario: Successful payment
    Given a Customer registered
    And a Merchant registered
    When the merchant initiates a payment for 10 kr by customer
    Then the "MerchantPaymentRequest" is published requesting payment
    When the payment service notifies the success of the payment
    Then the payment was successful