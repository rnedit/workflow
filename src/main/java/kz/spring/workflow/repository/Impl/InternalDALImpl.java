package kz.spring.workflow.repository.Impl;

import kz.spring.workflow.domain.ERole;
import kz.spring.workflow.domain.Internal;
import kz.spring.workflow.domain.Types.InternalType;
import kz.spring.workflow.repository.DAL.InternalDAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

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

    @Override
    public List<Internal> getAllMainOfDraft(String profileId, Pageable pageable) {
        Query query = new Query();
        query.with(pageable);
        query.addCriteria(Criteria
                .where("AllReaders").all(profileId)
                .and("Type").regex(InternalType.MAIN)
                .and("Draft").is(true)

        );
        List<Internal> internalsPage = mongoTemplate.find(query, Internal.class);
        return internalsPage;
    }

    @Override
    public List<Internal> getAllMainOfRoles(Set<String> Roles, Pageable pageable) {
        Query query = new Query();
        query.with(pageable);
        query.addCriteria(Criteria
                .where("AllReadersRoles").all(Roles)
                .and("Type").regex(InternalType.MAIN)
                .and("Draft").is(false)

        );
        List<Internal> internalsPage = mongoTemplate.find(query, Internal.class);
        return internalsPage;
    }

    @Override
    public List<Internal> getAllMainOfAllReaders(String profileId, Pageable pageable) {
        Query query = new Query();
        query.with(pageable);
        query.addCriteria(Criteria
                .where("AllReaders").all(profileId)
                .and("Type").regex(InternalType.MAIN)
                .and("Draft").is(false)

        );
        List<Internal> internalsPage = mongoTemplate.find(query, Internal.class);
        return internalsPage;
    }
}
