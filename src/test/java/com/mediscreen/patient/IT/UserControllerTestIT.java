package com.mediscreen.patient.IT;

import com.mediscreen.patient.model.User;
import com.mediscreen.patient.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) /*désactive tous les filtres dans la configuration SpringSecurity */
public class UserControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    /* Add validate user */
    @Test
    void addUser_ValidateUser() throws Exception{

        List<User> userListBeforeAdd;
        userListBeforeAdd = userService.findAll();

        //GIVEN
        String username = "usernameTest";
        String password = "Password1@";
        String role = "USER";

        // WHEN
        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/user/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("username", username)
                .param("password", password)
                .param("role",role))
                .andDo(print())
                .andExpect(view().name("redirect:/user/list"))
                .andExpect(status().is3xxRedirection());

        List<User> userListAfterAdd;
        userListAfterAdd = userService.findAll();

        assertEquals(userListAfterAdd.size(),userListBeforeAdd.size()+1);
    }

    /* Validate non compliant password */
    @Test
    void validateUser_NonCompliant_Password() throws Exception{

        List<User> userListBeforeAdd;
        userListBeforeAdd = userService.findAll();

        //GIVEN : Give a new user with non compliant password
        String username = "usernameTest";
        String badPassword = "";
        String role = "USER";

        //WHEN //THEN stay to add page
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/user/validate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .param("username", username)
                    .param("password", badPassword)
                    .param("role",role))
                    .andDo(print())
                    .andExpect(view().name("user/add"))
                    .andExpect(status().isOk());
        }
        catch (Exception e){
            assertTrue(e.getMessage().contains("Password must contain 1 or more uppercase characters"));
        }
        List<User> userListAfterAdd;
        userListAfterAdd = userService.findAll();

        assertEquals(userListAfterAdd.size(),userListBeforeAdd.size());
    }

    @Test
    void deleteUser_ExistingUser() throws Exception{

        List<User> userListBeforeDelete;
        userListBeforeDelete = userService.findAll();

        //GIVEN
        String username = "usernameTest";
        String password = "Password1@";
        String role = "USER";

        // WHEN
        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/user/delete/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("username", username)
                .param("password", password)
                .param("role", role))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));

        List<User> userListAfterDelete;
        userListAfterDelete = userService.findAll();

        assertEquals(userListAfterDelete.size(),userListBeforeDelete.size()-1);
    }

    @Test
    void deleteUser_Non_ExistingUser() throws Exception{

        List<User> userListBeforeDelete ;
        userListBeforeDelete = userService.findAll();

        //GIVEN
        // WHEN
        // THEN
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/user/delete/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/user/list"));
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Invalid user Id:999"));
        }
        List<User> userListAfterDelete;
        userListAfterDelete = userService.findAll();

        assertEquals(userListAfterDelete.size(),userListBeforeDelete.size());
    }
}
