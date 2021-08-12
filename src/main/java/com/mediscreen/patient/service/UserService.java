package com.mediscreen.patient.service;

import com.mediscreen.patient.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    /**
     * get a user by Id
     * @param id user id
     * @return user
     */
    Optional<User> findById(int id);

    /**
     * get the list of user
     * @return list of user
     * */
    List<User> findAll();

    /**
     * save user
     * @param user user to save
     * @return user saved
     */
    User saveUser(User user);

    /**
     * delete user
     * @param user user to delete
     * @return user deleted
     */
    User deleteUser(User user);

}
