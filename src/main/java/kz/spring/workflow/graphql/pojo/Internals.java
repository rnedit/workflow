package kz.spring.workflow.graphql.pojo;

import kz.spring.workflow.domain.internal.Internal;
import lombok.Data;

import java.util.List;

@Data
public class Internals {
    private List<Internal> internalList;
    private Integer totalCount;
}
