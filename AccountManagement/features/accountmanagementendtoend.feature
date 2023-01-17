Feature: Account Management Service

# ------------------------ Account Registration and Unregistration ------------------------

@DontRun
Scenario:
    Given a customer that is not registered with DTU Pay
    When the customer is being registered
    Then the customer is registered in the system
    And the customer is being unregistered
    And the customer is unregistered from the system

#Scenario: Customer Registration Race Condition (???)
