
#Feature: Account Management Service

# @author: Joao Silva s222961

# ------------------------ Account Registration and Unregistration ------------------------


#Scenario: Register and Unregister customer are successful
 #   Given a customer that is not registered with DTU Pay that succeeds in registering and unregistering
  #  When a successful "RegisterAccountRequest" register event for the customer is received
    #Then a TokenUserRequest event is sent
    #And a successful "RegisterUserTokenSuccess" event is received
   # Then a success "RegisterAccountRequestCompleted" event is ssent
    #And a successful "UnregisterAccountRequest" unregister event for the customer is received
   # And a success "UnregisterAccountSuccess" event is sent


#Scenario: Register and Unregister customer are unsuccessful
 #   Given a customer that is not registered with DTU Pay that fails to register
  #  When an unsuccessful "RegisterAccountRequest" register event for the customer is received
   # Then a failure "RegisterAccountRequestFailed" event is ssent
    #And an unsuccessful "UnregisterAccountRequest" unregister event for the customer is received
    #And a failure "UnregisterAccountFailed" event is sent

# ------------------------ Bank Account Request------------------------


#Scenario: Bank Account Request is successful
 #   Given a user called "Tiago" is registered at DTU Pay
  #  When a successful "BankAccountIdRequest" event is received asking for bank account
   # Then a success "BankAccountIdRequestCompleted" event is sent for the payment service


#Scenario: Bank Account Request is unsuccessful
 #   When a unsuccessful "BankAccountIdRequest" event is received asking for a not existing bank account
  #  Then a success "BankAccountIdRequestFailed" event is sent for the payment service failing
