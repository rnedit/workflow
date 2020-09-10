package kz.spring.workflow.tasks.internal;

import kz.spring.workflow.domain.eventqueue.EventQueue;

public interface SendEmailInternalDAO {
    Boolean send(EventQueue eventQueue);

    Boolean Execute(EventQueue eventQueue);
}
