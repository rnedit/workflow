package kz.spring.workflow.events.eventHandler.numerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/*
https://www.baeldung.com/spring-events
 */
@Component
public class NumeratorEventHandlerPublisher {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    public void doWorkflowEventHandler (
            final String eventName,
            String userNameCreation,
            String documentId
    ) {
        System.out.println("Publishing new event. ");
        String documentJobId = null;
        /*
        Создать в базе таблицу обработчик событий и работать с событиями там
         */

        NumeratorEventHandler numeratorEventHandler =
                new NumeratorEventHandler(this, eventName, userNameCreation, documentId, documentJobId);
        applicationEventPublisher.publishEvent(numeratorEventHandler);
    }
}
