package kz.spring.workflow.facades.internal.impl;

import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.domain.User;
import kz.spring.workflow.domain.eventqueue.EventQueue;
import kz.spring.workflow.domain.internal.Internal;
import kz.spring.workflow.events.eventHandler.EventQueueHandlerPublisher;
import kz.spring.workflow.facades.internal.InternalFacade;
import kz.spring.workflow.repository.Impl.InternalDALImpl;
import kz.spring.workflow.repository.ProfileRepository;
import kz.spring.workflow.request.internal.InternalSaveRequest;
import kz.spring.workflow.service.DAL.UserService;
import kz.spring.workflow.service.InternalService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static kz.spring.workflow.components.tasks.types.InternalTaskType.*;

@Slf4j
@Service
public class InternalFacadeImpl implements InternalFacade {

    private final UserService userService;

    private final InternalDALImpl internalDAL;

    private final EventQueueHandlerPublisher eventQueueHandlerPublisher;

    private final InternalService internalService;

    @Value("${mongodb.rootIdAllReaders}")
    String allReadersRootUser;

    private final ProfileRepository profileRepository;

    @Autowired
    public InternalFacadeImpl(UserService userService, InternalDALImpl internalDAL,
                              EventQueueHandlerPublisher eventQueueHandlerPublisher,
                              ProfileRepository profileRepository,
                              InternalService internalService) {
        this.userService = userService;
        this.internalDAL = internalDAL;
        this.eventQueueHandlerPublisher = eventQueueHandlerPublisher;
        this.profileRepository = profileRepository;
        this.internalService = internalService;
    }

    @Override
    public Internal getInternal(String id) {
       return internalDAL.getInternal(id);
    }

    @Override
    public Boolean updateInternal(InternalSaveRequest internalSaveRequest) {
        Internal curInternal = internalDAL.getInternal(internalSaveRequest.getId());
        if (curInternal==null) {
            throw new ObjectNotFoundException("Internal Id " + internalSaveRequest.getId() + " not found!", Internal.class.getName());
        }
        internalService.setInternalFromInternalSaveRequest(curInternal, internalSaveRequest);
        Boolean result = internalDAL.saveInternalisCurrentVersion(curInternal, internalSaveRequest);
        if (result) {
            EventQueue eventQueue = new EventQueue();
            eventQueue.setDocumentId(curInternal.getId());
            eventQueue.setTaskName(TASK_UPDATEINTERNAL);

            eventQueueHandlerPublisher.doEventHandler(eventQueue);
        }
        return result;
    }

    @Override
    public Internal saveInternal(InternalSaveRequest internalSaveRequest) {
            Internal internal = new Internal();
            Internal newInternal = internalService.setInternalFromInternalSaveRequest(internal, internalSaveRequest);

            EventQueue eventQueue = new EventQueue();
            eventQueue.setCreatorUserId(internalSaveRequest.getCreatorUserId());
            eventQueue.setDocumentId(internal.getId());
            eventQueue.setTaskName(TASK_SAVEINTERNAL);

            eventQueueHandlerPublisher.doEventHandler(eventQueue);
            return newInternal;


    }
}
