package kz.spring.workflow.repository.Impl;

import kz.spring.workflow.domain.internal.InternalAssignment;
import kz.spring.workflow.repository.DAL.InternalAssignmentDAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InternalAssignmentDALImpl implements InternalAssignmentDAL {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public InternalAssignment saveInternalAssignment(InternalAssignment internalAssignment) {
        mongoTemplate.save(internalAssignment);
        return internalAssignment;
    }

}
