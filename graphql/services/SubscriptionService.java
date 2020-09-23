package kz.spring.workflow.graphql.services;

import kz.spring.workflow.graphql.pojo.Internal;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SubscriptionService {
    private final Map<Long, Publisher<Internal>> tablePublishers = new ConcurrentHashMap<>();
    private final Map<String, Long> subscriptionToTableMap = new ConcurrentHashMap<>();
    private final Map<Long, Map<String, Subscriber<? super Internal>>> subscriptionsByTable = new ConcurrentHashMap<>();

    public Publisher<Internal> getTableAwarePublisher(Long tableId) {
        return tablePublishers.computeIfAbsent(tableId, id -> s -> subscribeToReservations(s, tableId));
    }

    public void subscribeToReservations(Subscriber<? super Internal> subscription, Long tableId) {
        subscriptionsByTable
                .computeIfAbsent(tableId, id -> new ConcurrentHashMap<>())
                .put("sessionId", subscription);  // TODO
        subscriptionToTableMap.put("sessionId", tableId);
    }

    public void newReservation(Long tableId, Internal reservationDto) {
        Map<String, Subscriber<? super Internal>> subscriptions = subscriptionsByTable.get(tableId);
        if (subscriptions != null) {
            for (Map.Entry<String, Subscriber<? super Internal>> entry : subscriptions.entrySet()) {
                entry.getValue().onNext(reservationDto);
            }
        }
    }

    public void unsubscribe(String sessionId) {
        Long tableId = subscriptionToTableMap.remove(sessionId);
        if (tableId != null) {
            Map<String, Subscriber<? super Internal>> subscriptions = subscriptionsByTable.get(tableId);
            if (subscriptions != null) {
                subscriptions.remove(sessionId);
            }
        }
    }
}
