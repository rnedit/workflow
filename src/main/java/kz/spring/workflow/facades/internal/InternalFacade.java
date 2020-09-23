package kz.spring.workflow.facades.internal;

import kz.spring.workflow.domain.internal.Internal;
import kz.spring.workflow.request.internal.InternalSaveRequest;

public interface InternalFacade {
    Internal getInternal(String id);
    Internal saveInternal(InternalSaveRequest internalSaveRequest);
    Boolean updateInternal(InternalSaveRequest internalSaveRequest);
}
