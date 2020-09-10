package kz.spring.workflow.events.eventHandler.numerator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class NumeratorEventHandler extends ApplicationEvent {
    private Boolean state = false;
    private String eventName;
    private String userNameCreation;
    private String jobId;
    private String documentId;
    private String statusExec = "";

    public NumeratorEventHandler(Object source,
                                 String eventName,
                                 String userNameCreation,
                                 String documentId,
                                 String jobId) {
        super(source);
        this.eventName = eventName;
        this.userNameCreation = userNameCreation;
        this.documentId = documentId;
        this.jobId = jobId;
    }

}
