package kz.spring.workflow.components.tasks.internal;

import kz.spring.workflow.domain.eventqueue.EventQueue;

public interface SaveInternalDAO {

    Boolean setNumberAndSave(EventQueue eventQueue);

    Boolean Execute(EventQueue eventQueue);

}
