package kz.spring.workflow.request;

import lombok.Data;

@Data
public class UsersRequest {
    Integer perpage;
    Integer page;
}
