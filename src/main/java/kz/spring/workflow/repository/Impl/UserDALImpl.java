package kz.spring.workflow.repository.Impl;

import kz.spring.workflow.domain.User;
import kz.spring.workflow.repository.DAL.UserDAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@Repository
public class UserDALImpl implements UserDAL {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<User> getAllUsers() {
        return mongoTemplate.findAll(User.class);
    }

    @Override
    public User getUserById(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query, User.class);
    }

    @Transactional
    @Override
    public User addNewUser(User user) {
        try {
        mongoTemplate.save(user);
        } catch (MongoTransactionException mte) {

        }
        // Now, user object will contain the ID as well
        return user;
    }

//    @Override
//    public Object getAllUserSettings(String userId) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("userId").is(userId));
//        User user = mongoTemplate.findOne(query, User.class);
//        return user != null ? user.getUserSettings() : "User not found.";
//    }

}
