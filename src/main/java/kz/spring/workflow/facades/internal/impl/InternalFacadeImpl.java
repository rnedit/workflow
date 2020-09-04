package kz.spring.workflow.facades.internal.impl;

import kz.spring.workflow.domain.internal.Internal;
import kz.spring.workflow.facades.internal.InternalFacade;
import kz.spring.workflow.repository.Impl.InternalDALImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InternalFacadeImpl implements InternalFacade {

    @Autowired
    InternalDALImpl internalDAL;

    @Override
    public Internal getInternal(String id) {
       return internalDAL.getInternal(id);
    }
}
