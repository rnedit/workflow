package kz.spring.workflow.events.eventHandler;

import kz.spring.workflow.domain.eventqueue.EventQueue;
import kz.spring.workflow.repository.EventQueueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/*
https://www.baeldung.com/spring-events
 */
@Slf4j
@Component
public class EventQueueHandlerPublisher {

    final
    private EventQueueRepository eventQueueRepository;

    final
    private ApplicationEventPublisher applicationEventPublisher;

    public EventQueueHandlerPublisher(EventQueueRepository eventQueueRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.eventQueueRepository = eventQueueRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void doEventHandler (
            EventQueue eventQueue
    ) {
        log.info("Publishing new event - " + eventQueue.getTaskName());

        eventQueueRepository.save(eventQueue);

        /*
        Создать в базе таблицу обработчик событий и работать с событиями там
         */

        EventQueueHandler eventQueueHandler =
                new EventQueueHandler(
                        this,
                        eventQueue
                );
        applicationEventPublisher.publishEvent(eventQueueHandler);
    }
}
