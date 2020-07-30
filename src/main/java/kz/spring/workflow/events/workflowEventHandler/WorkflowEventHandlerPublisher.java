package kz.spring.workflow.events.workflowEventHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/*
https://www.baeldung.com/spring-events
 */
@Component
public class WorkflowEventHandlerPublisher {
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

        WorkflowEventHandler workflowEventHandler =
                new WorkflowEventHandler(this, eventName, userNameCreation, documentId, documentJobId);
        applicationEventPublisher.publishEvent(workflowEventHandler);
    }
}
