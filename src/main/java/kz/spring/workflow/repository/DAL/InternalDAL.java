package kz.spring.workflow.repository.DAL;

import kz.spring.workflow.domain.internal.Internal;
import kz.spring.workflow.request.internal.InternalSaveRequest;
import kz.spring.workflow.request.internal.InternalTableRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

//https://www.baeldung.com/queries-in-spring-data-mongodb
public interface InternalDAL {

    List<Internal> getAllMainOfAllReaders(String profileId, Pageable pageable);
    List<Internal> getAllMainOfRolesOrAllReadersAndNumber(InternalTableRequest internalTableRequest);
    List<Internal> getAllMainOfRoles(Set<String> Roles, Pageable pageable);
    List<Internal> getAllMainOfRolesOrAllReaders(Collection<String> roles, Collection<String> profiles, Pageable pageable);
    List<Internal> getAllMainOfDraft(String profileId, Pageable pageable);
   // List<Internal> getAllMainOfSearch(String profileId, String searchText, Pageable pageable);
    List<Internal> getAllInternals();
    Integer getTotalCountForProfile(String profileId);
    Integer getTotalCountForRole(Set<String> Roles);
    Internal getInternal(String id);

    Boolean isCurrentVersion(Internal internal, Integer version);

    Boolean saveInternalisCurrentVersion(Internal internal, InternalSaveRequest internalSaveRequest);
    Internal saveInternal(Internal internal );
}
