package kz.spring.workflow.service;

import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.domain.User;
import kz.spring.workflow.domain.internal.Internal;
import kz.spring.workflow.repository.Impl.InternalDALImpl;
import kz.spring.workflow.repository.ProfileRepository;
import kz.spring.workflow.request.internal.InternalSaveRequest;
import kz.spring.workflow.service.DAL.UserService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InternalService {

    final
    private
    ProfileRepository profileRepository;

    private final UserService userService;

    private final InternalDALImpl internalDAL;

    @Autowired
    public InternalService(ProfileRepository profileRepository,
                           UserService userService,
                           InternalDALImpl internalDAL) {
        this.profileRepository = profileRepository;
        this.userService = userService;
        this.internalDAL = internalDAL;
    }

    public Internal setInternalFromInternalSaveRequest(Internal internal, InternalSaveRequest internalSaveRequest) {
        internal.setSubject(internalSaveRequest.getSubject());
        internal.setTypeAgreement(internalSaveRequest.getTypeAgreement());
        internal.setDraft(internalSaveRequest.getDraft());
        internal.setRecipient(internalSaveRequest.getRecipient());
        internal.setProfileRecipient(profileRepository.getById(internalSaveRequest.getRecipient()));
        internal.setСreatorProfileId(internalSaveRequest.getСreatorProfileId());
        internal.setСreatorUserId(internalSaveRequest.getСreatorUserId());

        internal.setUpdateUserId(internalSaveRequest.getUpdateUserId());
        internal.setUpdateProfileId(internalSaveRequest.getUpdateProfileId());

        Profile сreatorProfile = profileRepository.getById(internalSaveRequest.getСreatorProfileId());
        List<String> genAllReaders = new ArrayList<>();
        genAllReaders.add(internal.getProfileRecipient().getId());
        if (сreatorProfile==null) {
            сreatorProfile = new Profile("Профайла нет");
            сreatorProfile.setId("0");
        } else {
            if (!genAllReaders.contains(сreatorProfile.getId()))
                genAllReaders.add(сreatorProfile.getId());
        }
        internal.setCreatorProfile(сreatorProfile);
        User сreatorUser = userService.getById(internalSaveRequest.getСreatorUserId());

        if (сreatorUser == null) {
            throw new ObjectNotFoundException("User Id " + internalSaveRequest.getСreatorUserId() + " not found!",
                    User.class.getName());
        }
        internal.setCreatorUser(сreatorUser);

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

        if (internalSaveRequest.getAttachmentIds()!=null)
            internal.setAttachments(Arrays.asList(internalSaveRequest.getAttachmentIds()));
        if (internalSaveRequest.getAttachmentNames()!=null)
            internal.setAttachmentNames(Arrays.asList(internalSaveRequest.getAttachmentNames()));

        Internal newInternal = internalDAL.saveInternal(internal);

        if (newInternal == null) {
            throw new IllegalArgumentException("Internal cannot be null");
        }

        return newInternal;
    }

}
