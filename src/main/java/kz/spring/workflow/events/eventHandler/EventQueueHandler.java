package kz.spring.workflow.events.eventHandler;

import kz.spring.workflow.domain.eventqueue.EventQueue;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventQueueHandler extends ApplicationEvent {
    EventQueue eventQueue;

    public EventQueueHandler(Object source,
                             EventQueue eventQueue
    ) {
        super(source);
        this.eventQueue = eventQueue;
    }

}
