package kz.spring.workflow.events.eventHandler;

import kz.spring.workflow.domain.eventqueue.EventQueue;
import kz.spring.workflow.repository.EventQueueRepository;
import kz.spring.workflow.components.tasks.internal.impl.SaveInternalDAOImpl;
import kz.spring.workflow.components.tasks.internal.impl.SendEmailInternalDAOImpl;
import kz.spring.workflow.components.tasks.internal.impl.UpdateInternalDAOImpl;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static kz.spring.workflow.components.tasks.types.InternalTaskType.*;

/*
https://www.baeldung.com/spring-events
 */
@Slf4j
@Component
public class EventQueueHandlerListener implements ApplicationListener<EventQueueHandler> {
    final
    private EventQueueRepository eventQueueRepository;

    final
    private SaveInternalDAOImpl saveInternalDAO;

   private final SendEmailInternalDAOImpl sendEmailInternalDAO;

    private final UpdateInternalDAOImpl updateInternalDAO;

    @Autowired
    public EventQueueHandlerListener(EventQueueRepository eventQueueRepository,
                                     SaveInternalDAOImpl saveInternalDAO,
                                     SendEmailInternalDAOImpl sendEmailInternalDAO,
                                     UpdateInternalDAOImpl updateInternalDAO) {
        this.eventQueueRepository = eventQueueRepository;
        this.saveInternalDAO = saveInternalDAO;
        this.sendEmailInternalDAO = sendEmailInternalDAO;
        this.updateInternalDAO = updateInternalDAO;
    }

    @Override
    public void onApplicationEvent(EventQueueHandler eventQueueHandler) {
        EventQueue eventQueue = eventQueueHandler.eventQueue;
        log.info("Received new event - " + eventQueue.getTaskName());

        if (eventQueue == null){
            throw new ObjectNotFoundException("EventQueue eventQueue null!", EventQueue.class.getName());
        }

        EventQueue eventQ = eventQueueRepository.findEventQueueById(eventQueue.getId());

        if (eventQ == null) {
            throw new ObjectNotFoundException("EventQueue Id " + eventQueue.getId() + " not found!", EventQueue.class.getName());
        }

        Boolean result = switch(eventQ.getTaskName()) {
            case TASK_SAVEINTERNAL -> saveInternalDAO.Execute(eventQ);
            case TASK_SENDEMAILINTERNAL -> sendEmailInternalDAO.Execute(eventQ);
            case TASK_UPDATEINTERNAL -> updateInternalDAO.Execute(eventQ);
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
