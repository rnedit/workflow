package kz.spring.workflow.repository.Impl;

import kz.spring.workflow.domain.internal.Internal;
import kz.spring.workflow.repository.DAL.InternalDAL;
import org.bson.types.ObjectId;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Internal> getAllInternals() {
        return mongoTemplate.findAll(Internal.class);
    }

    @Transactional
    @Override
    public Internal saveInternal(Internal internal) {
        if (internal == null) {
            throw new IllegalArgumentException("Internal internal cannot be null");
        }
        mongoTemplate.save(internal);
        return internal;
    }

    @Override
    public List<Internal> saveAllInternal(List<Internal> internal) {
        if (internal == null) {
            throw new IllegalArgumentException("Internal internal cannot be null");
        }
        mongoTemplate.save(internal, mongoTemplate.getCollectionName(List.class));
        return internal;
    }

    @Override
    public Internal getInternal(String id) {

        if (id == null) {
            throw new IllegalArgumentException("Internal id cannot be null");
        }

        Query query = new Query();
        query.addCriteria(Criteria
                .where("id").is(new ObjectId(id))
        );

        final Internal internal = mongoTemplate.findOne(query, Internal.class);

        if (internal == null) {
            throw new ObjectNotFoundException("Internal Id " + id + " not found!", Internal.class.getName());
        }

        final Internal internalData = Internal.setNewInternal(internal);

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
                .where("AllReaders").all(profileId)
                .and("Draft").is(true)

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
                .where("AllReadersRoles").all(Roles)
                .and("Draft").is(false)

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
                .where("AllReaders").all(profileId)
                .and("Draft").is(false)

        );
        return getInternals(query);
    }

    private List<Internal> getInternals(Query query) {
        final List<Internal> internalsPage = mongoTemplate.find(query, Internal.class);
        final List<Internal> internalsFacadeData = new ArrayList<>();

        if (internalsPage != null && !internalsPage.isEmpty()) {
            internalsPage.forEach(i -> {
                final Internal internalData = Internal.setNewInternal(i);
                internalsFacadeData.add(internalData);
            });
        }
        return internalsFacadeData;
    }
}
