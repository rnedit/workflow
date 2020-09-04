package kz.spring.workflow.repository.Impl;

import kz.spring.workflow.domain.internal.InternalPerformed;
import kz.spring.workflow.repository.DAL.InternalPerformedDAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

public class InternalPerformedDALImpl implements InternalPerformedDAL {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public InternalPerformed saveInternalPerformed(InternalPerformed internalPerformed) {
        mongoTemplate.save(internalPerformed);
        return internalPerformed;
    }

    @Override
    public List<InternalPerformed> saveAllInternalPerformed(List<InternalPerformed> internalPerformeds) {
        mongoTemplate.save(internalPerformeds, mongoTemplate.getCollectionName(List.class));
        return internalPerformeds;
    }
}
