package kz.spring.workflow.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.repository.ProfileRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class ProfileQuery implements GraphQLQueryResolver {

    @Autowired
    ProfileRepository profileRepository;

    public List<Profile> getProfiles(){
        return profileRepository.findAll();
    }

    public Profile getProfile(String id){
        return profileRepository.getById(id);
    }

    public List<Profile> getProfilesByParentIdNotNull() {
        return profileRepository.getProfilesByParentIdNotNull();
    }
}
