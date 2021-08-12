package com.mediscreen.patient.service;

import com.mediscreen.patient.dao.UserDao;
import com.mediscreen.patient.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl (UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User saveUser(User user) {
        return userDao.save(user);
    }

    @Override
    public User deleteUser(User user) {
         userDao.delete(user);
        return user;
    }
}
