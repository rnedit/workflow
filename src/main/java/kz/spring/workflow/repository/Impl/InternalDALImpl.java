package kz.spring.workflow.repository.Impl;

import kz.spring.workflow.domain.User;
import kz.spring.workflow.domain.internal.Internal;
import kz.spring.workflow.graphql.pojo.Internals;
import kz.spring.workflow.repository.DAL.InternalDAL;
import kz.spring.workflow.repository.InternalRepository;
import kz.spring.workflow.request.internal.InternalSaveRequest;
import kz.spring.workflow.request.internal.InternalTableRequest;
import kz.spring.workflow.service.InternalService;
import kz.spring.workflow.service.UserServiceImpl;
import org.bson.BsonRegularExpression;
import org.bson.types.ObjectId;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.data.mongodb.core.ExecutableFindOperation;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Repository
public class InternalDALImpl implements InternalDAL {

    @Autowired
    InternalRepository internalRepository;

    final private MongoTemplate mongoTemplate;
    final private UserServiceImpl userService;

    @Autowired
    public InternalDALImpl(MongoTemplate mongoTemplate, UserServiceImpl userService) {
        this.mongoTemplate = mongoTemplate;
        this.userService = userService;
    }

    @Override
    public List<Internal> getAllInternals() {
        return mongoTemplate.findAll(Internal.class);
    }

    @Override
    public List<Internal> getAllMainOfRolesOrAllReadersAndNumber(InternalTableRequest internalTableRequest) {
        if (internalTableRequest == null) {
            throw new IllegalArgumentException("internalTableRequest cannot be null");
        }
        if (internalTableRequest.getSearchText().isBlank() || internalTableRequest.getSearchText()==null) {
            throw new IllegalArgumentException("internalTableRequest.getSearchText() cannot be null or blank");
        }
        User user = userService.getById(internalTableRequest.getUserId());
        Pageable pageble = PageRequest.of(internalTableRequest.getPage(),
                internalTableRequest.getPageSize(),
                Sort.by("creationDate").descending());
        Set<String> roles = new HashSet<>();
        user.getRoles().forEach(r -> {
            roles.add(r.getId());
        });


        return getInternalsForRegex(internalTableRequest.getSearchText(), internalTableRequest.getSearchText());
    }

//    @Override
//    public List<Internal> getAllMainOfSearch(String profileId, String searchText, Pageable pageable) {
//        if (profileId == null) {
//            throw new IllegalArgumentException("profileId cannot be null");
//        }
//        if (searchText == null) {
//            throw new IllegalArgumentException("searchText cannot be null");
//        }
//        Query query = new Query();
//
//        query.with(pageable);
//
//        query.addCriteria(Criteria
//                .where("allReadersRoles").all(Roles)
//
//        );
//        return getInternals(query);
//    }

    @Override
    public Boolean isCurrentVersion(Internal internal, Integer version) {
        if (version==null)
            return true;
        Internal curInternal = getInternal(internal.getId());
        if (curInternal.getVersion()==null) {
            return true;
        }
        if (curInternal.getVersion()==version)
            return true;

        return false;
    }

    @Override
    public Boolean saveInternalisCurrentVersion(Internal internal, InternalSaveRequest internalSaveRequest ) {
        if (internal == null) {
            throw new IllegalArgumentException("Internal internal cannot be null");
        }
        if (isCurrentVersion(internal, internalSaveRequest.getVersion())) {
            internal.setVersion(internal.getVersion()+1);
            mongoTemplate.save(internal);
            return true;
        }
        return false;
    }

    @Override
    public Internal saveInternal(Internal internal ) {
        if (internal == null) {
            throw new IllegalArgumentException("Internal internal cannot be null");
        }
            mongoTemplate.save(internal);
            return internal;
    }
//Не поддерживается мульти сохранение
//    @Transactional
//    @Override
//    public List<Internal> saveAllInternal(List<Internal> internals) {
//        if (internals == null) {
//            throw new IllegalArgumentException("Internals internal cannot be null");
//        }
//        try {
//            mongoTemplate.save(internals, mongoTemplate.getCollectionName(List.class));
//        } catch (MongoTransactionException mte) {
//
//        }
//
//        return internals;
//    }

    @Override
    public Internal getInternal(String id) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Internal id cannot be null or Blank");
        }

        Query query = new Query();
        query.addCriteria(Criteria
//                .where("id").is(new ObjectId(id))
                        .where("_id").is(id)
        );

        final Internal internal = mongoTemplate.findOne(query, Internal.class);

        if (internal == null) {
            throw new ObjectNotFoundException("Internal Id " + id + " not found!", Internal.class.getName());
        }

        final Internal internalData = Internal.setNewInternalFromCurrent(internal);

        return internalData;
    }

    @Override
    public List<Internal> getAllMainOfDraft(String profileId, Pageable pageable) {

        if (profileId == null) {
            throw new IllegalArgumentException("profileId cannot be null");
        }
        if (pageable == null) {
            throw new IllegalArgumentException("pageable cannot be null");
        }
        Query query = new Query();
        query.with(pageable);
        query.addCriteria(Criteria
                .where("allReaders").all(profileId)
                .and("draft").is(true)

        );
        return getInternals(query);
    }

    @Override
    public List<Internal> getAllMainOfRoles(Set<String> Roles, Pageable pageable) {
        if (Roles == null) {
            throw new IllegalArgumentException("Roles cannot be null");
        }
        if (pageable == null) {
            throw new IllegalArgumentException("pageable cannot be null");
        }
        Query query = new Query();

        query.with(pageable);

        query.addCriteria(Criteria
                .where("allReadersRoles").all(Roles)

        );
        System.out.println(query.toString());
        return getInternals(query);
    }

    @Override
    public List<Internal> getAllMainOfAllReaders(String profileId, Pageable pageable) {
        if (profileId == null) {
            throw new IllegalArgumentException("profileId cannot be null");
        }
        if (pageable == null) {
            throw new IllegalArgumentException("pageable cannot be null");
        }
        Query query = new Query();
        query.with(pageable);
        query.addCriteria(Criteria
                .where("allReaders").all(profileId)
                .and("draft").is(false)
                .and("number").ne(null)

        );
        return getInternals(query);
    }

    private List<Internal> getInternals(Query query) {
//        final List<Internal> internals = mongoTemplate.find(query, Internal.class);
//        final List<Internal> internalsFacadeData = new ArrayList<>();
//
//        if (internals != null && !internals.isEmpty()) {
//            internals.forEach(i -> {
//                final Internal internalData = Internal.setNewInternalFromCurrent(i);
//                internalsFacadeData.add(internalData);
//            });
//        }

        return mongoTemplate.find(query, Internal.class);
    }

    private List<Internal> getInternalsForRegex(String number, String subject) {
//        final List<Internal> internals = mongoTemplate.find(query, Internal.class);
//        final List<Internal> internalsFacadeData = new ArrayList<>();
//
//        if (internals != null && !internals.isEmpty()) {
//            internals.forEach(i -> {
//                final Internal internalData = Internal.setNewInternalFromCurrent(i);
//                internalsFacadeData.add(internalData);
//            });
//        }

        return internalRepository.findAllByNumberRegexOrSubjectRegex(number, subject);
    }
    @Override
    public Integer getTotalCountForProfile(String profileId) {
        if (profileId == null) {
            throw new IllegalArgumentException("profileId cannot be null");
        }
        Query query = new Query();
        query.addCriteria(Criteria
                .where("allReaders").all(profileId)
                .and("draft").is(false)
                .and("number").ne(null)

        );
        return getInternals(query).size();
    }
    @Override
    public Integer getTotalCountForRole(Set<String> Roles) {
        if (Roles == null) {
            throw new IllegalArgumentException("Roles cannot be null");
        }
        Query query = new Query();
        query.addCriteria(Criteria
                .where("allReadersRoles").all(Roles)

        );
        return getInternals(query).size();
    }
}
