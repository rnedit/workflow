package kz.spring.workflow.components.tasks.types;

import java.io.Serializable;

public class InternalTaskType implements Serializable {
    public static final String TASK_SAVEINTERNAL = "saveInternal";
    public static final String TASK_UPDATEINTERNAL = "updateInternal";
    public static final String TASK_SENDEMAILINTERNAL = "sendEmailInternal";
}
