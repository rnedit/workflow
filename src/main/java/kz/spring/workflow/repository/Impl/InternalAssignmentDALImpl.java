package kz.spring.workflow.repository.Impl;

import kz.spring.workflow.domain.internal.InternalAssignment;
import kz.spring.workflow.repository.DAL.InternalAssignmentDAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

public class InternalAssignmentDALImpl implements InternalAssignmentDAL {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public InternalAssignment saveInternalAssignment(InternalAssignment internalAssignment) {
        mongoTemplate.save(internalAssignment);
        return internalAssignment;
    }

    @Override
    public List<InternalAssignment> saveAllInternalAssignment(List<InternalAssignment> internalAssignments) {
        mongoTemplate.save(internalAssignments, mongoTemplate.getCollectionName(List.class));
        return internalAssignments;
    }
}
