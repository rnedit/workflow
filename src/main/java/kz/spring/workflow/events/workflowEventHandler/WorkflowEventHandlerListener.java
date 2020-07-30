package kz.spring.workflow.events.workflowEventHandler;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
/*
https://www.baeldung.com/spring-events
 */
@Component
public class WorkflowEventHandlerListener implements ApplicationListener<WorkflowEventHandler> {
    @Override
    public void onApplicationEvent(WorkflowEventHandler workflowEventHandler) {
        System.out.println("Received new event - " + workflowEventHandler.getEventName());
        /*
        в зависимости от результатов выполнения найти в базе обработчик по jobId и записать состояние в задание
        Если состояние false то создать уведомление для администрации о проблеме с заданием через другой прослушиватель
         */
    }
}
