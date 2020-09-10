package kz.spring.workflow.tasks.internal;

import kz.spring.workflow.domain.eventqueue.EventQueue;

public interface SaveInternalDAO {

    Boolean setNumberAndSave(EventQueue eventQueue);

}
