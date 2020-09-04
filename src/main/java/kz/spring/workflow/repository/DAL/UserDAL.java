package kz.spring.workflow.repository.DAL;

import kz.spring.workflow.domain.User;

import java.util.List;

public interface UserDAL {

    List<User> getAllUsers();

    User getUserById(String userId);

    User addNewUser(User user);

//    Object getAllUserSettings(String userId);

}
