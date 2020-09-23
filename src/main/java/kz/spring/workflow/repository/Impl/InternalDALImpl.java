package kz.spring.workflow.repository.Impl;

import kz.spring.workflow.domain.internal.Internal;
import kz.spring.workflow.repository.DAL.InternalDAL;
import kz.spring.workflow.request.internal.InternalSaveRequest;
import kz.spring.workflow.service.InternalService;
import org.bson.types.ObjectId;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class InternalDALImpl implements InternalDAL {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public InternalDALImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Internal> getAllInternals() {
        return mongoTemplate.findAll(Internal.class);
    }

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

        if (id == null) {
            throw new IllegalArgumentException("Internal id cannot be null");
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
                .and("draft").is(false)
                .and("number").ne(null)

        );
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
        final List<Internal> internalsPage = mongoTemplate.find(query, Internal.class);
        final List<Internal> internalsFacadeData = new ArrayList<>();

        if (internalsPage != null && !internalsPage.isEmpty()) {
            internalsPage.forEach(i -> {
                final Internal internalData = Internal.setNewInternalFromCurrent(i);
                internalsFacadeData.add(internalData);
            });
        }
        return internalsFacadeData;
    }
}
