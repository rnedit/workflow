package kz.spring.workflow.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import kz.spring.workflow.domain.ERole;
import kz.spring.workflow.domain.User;
import kz.spring.workflow.domain.internal.Internal;
import kz.spring.workflow.graphql.pojo.Internals;
import kz.spring.workflow.repository.Impl.InternalDALImpl;
import kz.spring.workflow.request.internal.InternalTableRequest;
import kz.spring.workflow.service.UserServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j2
@Component
public class InternalQuery implements GraphQLQueryResolver {

    final
    private
    InternalDALImpl internalDAL;

    final private UserServiceImpl userService;


    @Autowired
    public InternalQuery(InternalDALImpl internalDAL, UserServiceImpl userService) {
        this.internalDAL = internalDAL;
        this.userService = userService;
    }

    public Internals getInternals(InternalTableRequest internalRequest) {
        User user = userService.getById(internalRequest.getUserId());
        Pageable pageble = PageRequest.of(internalRequest.getPage(),
                internalRequest.getPageSize(),
                Sort.by("creationDate").descending());
        Internals internals = new Internals();
        if (user.getRoles().contains(ERole.ROLE_USER)) {
            List<Internal> internalList = internalDAL.getAllMainOfAllReaders(user.getParentIdProfile(), pageble);
            internals.setInternalList(internalList);
            internals.setTotalCount(internalDAL.getTotalCountForProfile(user.getParentIdProfile()));
        } else {
            Set<String> roles = new HashSet<>();
            user.getRoles().forEach(r -> {
                roles.add(r.getId());
            });
            List<Internal> internalList = internalDAL.getAllMainOfRoles(roles, pageble);
            internals.setInternalList(internalList);
            internals.setTotalCount(internalDAL.getTotalCountForRole(roles));
        }

        return internals;
    };

    public Internal getInternal(String id) {
        return internalDAL.getInternal(id);
    }

    public List<Internal> internals() {
        return internalDAL.getAllInternals();
    }
}
