package kz.spring.workflow.components.tasks.internal.impl;

import kz.spring.workflow.domain.eventqueue.EventQueue;
import kz.spring.workflow.components.tasks.internal.UpdateInternalDAO;
import org.springframework.stereotype.Service;

@Service
public class UpdateInternalDAOImpl implements UpdateInternalDAO {
    @Override
    public Boolean Execute(EventQueue eventQueue) {
        return null;
    }
}
