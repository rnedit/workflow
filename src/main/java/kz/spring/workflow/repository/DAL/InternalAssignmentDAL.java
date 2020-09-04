package kz.spring.workflow.repository.DAL;

import kz.spring.workflow.domain.internal.InternalAssignment;

import java.util.List;

public interface InternalAssignmentDAL {
    InternalAssignment saveInternalAssignment(InternalAssignment internalAssignment);
    List<InternalAssignment> saveAllInternalAssignment(List<InternalAssignment> internalAssignments);
}
