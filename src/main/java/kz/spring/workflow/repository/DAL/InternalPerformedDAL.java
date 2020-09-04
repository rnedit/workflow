package kz.spring.workflow.repository.DAL;

import kz.spring.workflow.domain.internal.InternalPerformed;

import java.util.List;

public interface InternalPerformedDAL {
    InternalPerformed saveInternalPerformed(InternalPerformed internalPerformed);
    List<InternalPerformed> saveAllInternalPerformed(List<InternalPerformed> internalPerformeds);
}
