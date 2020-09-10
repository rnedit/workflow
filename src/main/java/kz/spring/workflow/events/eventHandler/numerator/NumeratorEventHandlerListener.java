package kz.spring.workflow.events.eventHandler.numerator;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/*
https://www.baeldung.com/spring-events
 */
@Component
public class NumeratorEventHandlerListener implements ApplicationListener<NumeratorEventHandler> {
    @Override
    public void onApplicationEvent(NumeratorEventHandler numeratorEventHandler) {
        System.out.println("Received new event - " + numeratorEventHandler.getEventName());
        /*
        в зависимости от результатов выполнения найти в базе обработчик по jobId и записать состояние в задание
        Если состояние false то создать уведомление для администрации о проблеме с заданием через другой прослушиватель
         */
    }
}
