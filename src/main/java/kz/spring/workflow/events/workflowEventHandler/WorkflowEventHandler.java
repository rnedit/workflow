package kz.spring.workflow.events.workflowEventHandler;

import org.springframework.context.ApplicationEvent;

public class WorkflowEventHandler extends ApplicationEvent {
    private Boolean state = false;
    private String eventName;
    private String userNameCreation;
    private String jobId;
    private String documentId;
    private String statusExec = "";

    public WorkflowEventHandler (Object source,
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

    public Boolean getState() {
        return state;
    }

    public String getEventName() {
        return eventName;
    }

    public String getUserNameCreation() {
        return userNameCreation;
    }

    public String getStatusExec() {
        return statusExec;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getJobId() {
        return jobId;
    }

    public String getDocumentId() {
        return documentId;
    }
}
