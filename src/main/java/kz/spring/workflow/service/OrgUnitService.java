package kz.spring.workflow.service;

import kz.spring.workflow.domain.OrgUnit;
import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class OrgUnitService {

    private final ProfileRepository profileRepository;

    public OrgUnitService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Transactional
    public void setSuffixAllChaild(OrgUnit orgUnit, String s) {
        Set<Profile> profileSet = orgUnit.getProfiles();
        profileSet.forEach(p->{
            p.setSuffix(s);
        });
        profileRepository.saveAll(profileSet);
    }

}
