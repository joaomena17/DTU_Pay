Feature: Account Management Service

# ----------------------------- Customer -----------------------------

# ------------------------ Customer Unregistration ------------------------

Scenario:
    Given a customer that is registered with DTU Pay
    When the customer is being unregistered
    Then the customer is unregistered

# ------------------------ Merchant Unregistration ------------------------
