package kz.spring.workflow.events.eventHandler;

import kz.spring.workflow.domain.eventqueue.EventQueue;
import kz.spring.workflow.repository.EventQueueRepository;
import kz.spring.workflow.tasks.internal.impl.SaveInternalDAOImpl;
import kz.spring.workflow.tasks.types.InternalTaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/*
https://www.baeldung.com/spring-events
 */
@Slf4j
@Component
public class EventQueueHandlerListener implements ApplicationListener<EventQueueHandler> {
    @Autowired
    EventQueueRepository eventQueueRepository;

    @Autowired
    SaveInternalDAOImpl saveInternalDAO;

    @Override
    public void onApplicationEvent(EventQueueHandler eventQueueHandler) {
        EventQueue eventQueue = eventQueueHandler.eventQueue;
        log.info("Received new event - " + eventQueue.getTaskName());

        EventQueue eventQ = eventQueueRepository.findEventQueueById(eventQueue.getId());


        Boolean result = switch(eventQ.getTaskName()) {
            case InternalTaskType.TASK_SAVEINTERNAL -> saveInternalDAO.setNumberAndSave(eventQ);
            case InternalTaskType.TASK_ANOTHERINTERNAL -> false;
            default -> throw new IllegalArgumentException("EventQueueHandlerListener eventQ.getTaskName()="+eventQ.getTaskName()+" Seriously?!");
        };

        eventQ.setExecuteSuccess(result);
        eventQueueRepository.save(eventQ);
        /*
        в зависимости от результатов выполнения найти в базе обработчик по jobId и записать состояние в задание
        Если состояние false то создать уведомление для администрации о проблеме с заданием через другой прослушиватель
         */
    }
}
