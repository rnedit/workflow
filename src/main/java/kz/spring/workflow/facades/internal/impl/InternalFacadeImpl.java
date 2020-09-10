package kz.spring.workflow.facades.internal.impl;

import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.domain.User;
import kz.spring.workflow.domain.eventqueue.EventQueue;
import kz.spring.workflow.domain.internal.Internal;
import kz.spring.workflow.events.eventHandler.EventQueueHandlerPublisher;
import kz.spring.workflow.facades.internal.InternalFacade;
import kz.spring.workflow.repository.Impl.InternalDALImpl;
import kz.spring.workflow.repository.ProfileRepository;
import kz.spring.workflow.repository.UserRepository;
import kz.spring.workflow.request.internal.InternalSaveRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static kz.spring.workflow.tasks.types.InternalTaskType.*;

@Slf4j
@Service
public class InternalFacadeImpl implements InternalFacade {

    private final UserRepository userRepository;

    private final InternalDALImpl internalDAL;

    private final EventQueueHandlerPublisher eventQueueHandlerPublisher;

    @Value("${mongodb.rootIdAllReaders}")
    String allReadersRootUser;

    private final ProfileRepository profileRepository;

    public InternalFacadeImpl(UserRepository userRepository, InternalDALImpl internalDAL,
                              EventQueueHandlerPublisher eventQueueHandlerPublisher,
                              ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.internalDAL = internalDAL;
        this.eventQueueHandlerPublisher = eventQueueHandlerPublisher;
        this.profileRepository = profileRepository;
    }

    @Override
    public Internal getInternal(String id) {
       return internalDAL.getInternal(id);
    }

    @Override
    public Internal saveInternal(InternalSaveRequest internalSaveRequest) {
        Internal internal = new Internal();
        internal.setSubject(internalSaveRequest.getSubject());
        internal.setRecipient(internalSaveRequest.getRecipient());
        internal.setTypeAgreement(Integer.valueOf(internalSaveRequest.getTypeAgreement()));
        internal.setDraft(Boolean.valueOf(internalSaveRequest.getDraft()));
        internal.setProfileRecipient(profileRepository.getById(internalSaveRequest.getRecipient()));

        List<String> genAllReaders = new ArrayList<>();
        genAllReaders.add(internal.getProfileRecipient().getId());

        Profile сreatorProfile = profileRepository.getById(internalSaveRequest.getСreatorProfileId());

        if (сreatorProfile==null) {
            сreatorProfile = new Profile("Профайла нет");
            сreatorProfile.setId("0");
        } else {
            if (!genAllReaders.contains(сreatorProfile.getId()))
                genAllReaders.add(сreatorProfile.getId());
        }
        internal.setСreatorProfile(сreatorProfile);
        User сreatorUser = userRepository.getById(internalSaveRequest.getСreatorUserId());

        if (сreatorUser == null) {
            throw new ObjectNotFoundException("User Id " + internalSaveRequest.getСreatorUserId() + " not found!", User.class.getName());
        }
        internal.setСreatorUser(сreatorUser);

        Set<Profile> allReadersProfiles = new HashSet<>();
        genAllReaders.forEach(o->{
            allReadersProfiles.add(profileRepository.getById(o));
        });
        internal.setProfilesAllReaders(allReadersProfiles);

        List<String> genAllReadersRoles = new ArrayList<>();

        allReadersProfiles.forEach(p->{
            p.getUser().getRoles().forEach(r->{
                if (!genAllReadersRoles.contains(r.getId()))
                    genAllReadersRoles.add(r.getId());
            });
        });

        internalSaveRequest.getСreatorRolesId().forEach(r->{
            if (!genAllReadersRoles.contains(r))
                genAllReadersRoles.add(r);
        });

        //права на доступ обычным пользователям "ROLE_USER"
        internal.setAllReaders(genAllReaders);
        //права на доступ по ролям если роль не соответствует "ROLE_USER"
        internal.setAllReadersRoles(genAllReadersRoles);

        Internal newInternal = internalDAL.saveInternal(internal);

        if (newInternal == null) {
            throw new IllegalArgumentException("Internal newInternal cannot be null");
        }

        EventQueue eventQueue = new EventQueue();
        eventQueue.setCreatorUserId(internalSaveRequest.getСreatorUserId());
        eventQueue.setDocumentId(newInternal.getId());
        eventQueue.setTaskName(TASK_SAVEINTERNAL);

        eventQueueHandlerPublisher.doEventHandler(eventQueue);

        return newInternal;
    }
}
