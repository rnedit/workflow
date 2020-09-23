package kz.spring.workflow.components.tasks.internal;

import kz.spring.workflow.domain.eventqueue.EventQueue;

public interface UpdateInternalDAO {
    Boolean Execute(EventQueue eventQueue);
}
