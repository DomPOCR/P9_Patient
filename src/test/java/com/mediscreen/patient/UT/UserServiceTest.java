package com.mediscreen.patient.UT;

import com.mediscreen.patient.dao.UserDao;
import com.mediscreen.patient.model.User;
import com.mediscreen.patient.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    UserDao userDao;
    @Autowired
    UserService userService;

    User userTest = new User("Test","mdp","ADMIN");

    // SAVE
        @Test
    public void saveUserTest() {

        //GIVEN

        //WHEN
        when(userDao.save(userTest)).thenReturn(userTest);

        //THEN
        assertThat(userService.saveUser(userTest)).isNotNull();
    }

    // Delete
    @Test
    public void deleteUserTest() {

        //GIVEN
        List<User> userList = new ArrayList<>();
        userList.add(userTest);

        //WHEN
        Mockito.doNothing().when(userDao).delete(userTest);

        //THEN
        assertThat(userService.deleteUser(userTest)).isNotNull();
    }

    // Find All
    @Test
    public void findAllUserTest() {

        //GIVEN
        List<User> userList = new ArrayList<>();
        userList.add(userTest);

        //WHEN
        when(userDao.findAll()).thenReturn(userList);

        //THEN
        assertThat(userService.findAll().size()).isEqualTo(1);
    }

    // Find by id

    @Test
    public void findByUserId() {
        //GIVEN
        when(userDao.findById(0)).thenReturn(java.util.Optional.ofNullable(userTest));

        //WHEN
        Optional<User> userResult =  userService.findById(0);
        //THEN
        assertThat(userResult).isNotNull();
    }

    }
