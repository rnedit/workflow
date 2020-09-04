package kz.spring.workflow.repository.Impl;

import kz.spring.workflow.domain.User;
import kz.spring.workflow.repository.DAL.UserDAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

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

    @Override
    public User addNewUser(User user) {
        mongoTemplate.save(user);
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
