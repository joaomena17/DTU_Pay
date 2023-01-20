

# @author: Joao Silva s222961

# ------------------------ Account Registration and Unregistration ------------------------

Feature: Account Register and unregister
  Scenario: Register and Unregister customer are successful
    Given a customer "Ana" that is not registered with DTU Pay
    When a register request event is received
    Then a success register event is sent
    When an unregister event for the customer is received
    And an unregister success event is sent


  Scenario: Register and Unregister customer are unsuccessful
    Given a customer "" that is not registered with DTU Pay
    When a register request event is received
    Then a failure register event is sent
    When an unregister event for the customer is received
    Then an unregister failure event is sent