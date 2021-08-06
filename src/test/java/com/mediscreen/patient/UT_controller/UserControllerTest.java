package com.mediscreen.patient.UT_controller;

import com.mediscreen.patient.web.controller.UserController;
import com.mediscreen.patient.model.User;
import com.mediscreen.patient.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Configuration
    static class ContextConfiguration {
        @Bean
        public UserController getUserController() {
            return new UserController();
        }
    }
    // Données de test

    String username = "usernameTest";
    String IncorrectUsername = null;
    String password = "Password1@";
    String role = "USER";

    /* Show the list of user */
    @Test
    void listUser() throws Exception{

        List<User> userList = new ArrayList<>();

        //GIVEN
        User userTest = new User(username,password,role);
        userList.add(userTest);
        Mockito.when(userService.findAll()).thenReturn(userList);

        //WHEN //THEN return the list page
        mockMvc.perform(get("/user/list"))
                .andDo(print())
                .andExpect(view().name("user/list"))
                .andExpect(status().isOk());
        Mockito.verify(userService,Mockito.times(1)).findAll();
    }

    /* Display user adding form*/
    @Test
    void addUser() throws Exception{

        //GIVEN

        //WHEN //THEN return the add page
        mockMvc.perform(get("/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(view().name("user/add"))
                .andExpect(status().isOk());
    }

    /* Validate correct user */
    @Test
    void validateUser_CorrectUser() throws Exception{

        //GIVEN : Give a new user
        User userTest = new User(username,password,role);
        Mockito.when(userService.findById(anyInt())).thenReturn(Optional.of(userTest));

        //WHEN //THEN return the list page
        mockMvc.perform(post("/user/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("username", username)
                .param("password", password)
                .param("role",role))
                .andDo(print())
                .andExpect(view().name("redirect:/user/list"))
                .andExpect(status().is3xxRedirection());

        Mockito.verify(userService,Mockito.times(1)).saveUser(any(User.class));
    }

    /* Display user updating form */
    @Test
    void updateUser_ShowUpdateForm() throws Exception{

        //GIVEN : Give an exiting user
        User userTest = new User(username,password,role);
        Mockito.when(userService.findById(anyInt())).thenReturn(Optional.of(userTest));

        //WHEN //THEN return the update page
        mockMvc.perform(get("/user/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(view().name("user/update"))
                .andExpect(status().isOk());
    }

    /* Update an existing user and return to the list    */
    @Test
    public void updateUser_CorrectUser() throws Exception {

        //GIVEN : Give an exiting user
        User userTest = new User(username,password,role);
        Mockito.when(userService.findById(anyInt())).thenReturn(Optional.of(userTest));

        //WHEN //THEN return the list page
        mockMvc.perform(post("/user/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("username", username)
                .param("password", password)
                .param("role",role))
                .andDo(print())
                .andExpect(view().name("redirect:/user/list"))
                .andExpect(status().is3xxRedirection());

        Mockito.verify(userService,Mockito.times(1)).saveUser(any(User.class));
    }

    /* Update an incorrect user and stay to update    */
    @Test
    public void updateUser_IncorrectUser() throws Exception {

        //GIVEN : Give an exiting user
        User userTest = new User(username,password,role);
        Mockito.when(userService.findById(anyInt())).thenReturn(Optional.of(userTest));

        //WHEN //THEN return the update page
        mockMvc.perform(post("/user/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("username", IncorrectUsername)
                .param("password", password)
                .param("role",role))
                .andDo(print())
                .andExpect(view().name("user/update"))
                .andExpect(status().isOk());

        Mockito.verify(userService,Mockito.times(0)).saveUser(any(User.class));
    }

    /* Display delete a user */
    @Test
    void deleteUser_UserListIsReturn() throws Exception{

        //GIVEN : Give an exiting Person
        User userTest = new User(username,password,role);
        Mockito.when(userService.findById(anyInt())).thenReturn(Optional.of(userTest));

        //WHEN //THEN return the list page
        mockMvc.perform(get("/user/delete/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));

        Mockito.verify(userService,Mockito.times(1)).deleteUser(any(User.class));
    }

}
