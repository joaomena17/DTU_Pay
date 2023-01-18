package service;

import entities.PaymentReport;
import messaging.MessageQueue;
import messaging.Event;
import utils.CorrelationID;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class TokenService {
    private MessageQueue queue;
    private Map<CorrelationID, CompletableFuture<List<PaymentReport>>> correlations = new ConcurrentHashMap<>();
}
