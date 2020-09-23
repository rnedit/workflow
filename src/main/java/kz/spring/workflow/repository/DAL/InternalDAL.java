package kz.spring.workflow.repository.DAL;

import kz.spring.workflow.domain.internal.Internal;
import kz.spring.workflow.request.internal.InternalSaveRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

//https://www.baeldung.com/queries-in-spring-data-mongodb
public interface InternalDAL {

    List<Internal> getAllMainOfAllReaders(String profileId, Pageable pageable);
    List<Internal> getAllMainOfRoles(Set<String> Roles, Pageable pageable);
    List<Internal> getAllMainOfDraft(String profileId, Pageable pageable);
    List<Internal> getAllInternals();

    Internal getInternal(String id);

    Boolean isCurrentVersion(Internal internal, Integer version);

    Boolean saveInternalisCurrentVersion(Internal internal, InternalSaveRequest internalSaveRequest);
    Internal saveInternal(Internal internal );
}
