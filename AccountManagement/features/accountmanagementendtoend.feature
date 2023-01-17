Feature: Account Management Service

# ------------------------ Account Registration and Unregistration ------------------------

Scenario:
    Given a customer that is not registered with DTU Pay
    When the customer is being registered
    Then the customer is registered
    And the customer is being unregistered
    And the customer is unregistered

#Scenario: Customer Registration Race Condition (???)
