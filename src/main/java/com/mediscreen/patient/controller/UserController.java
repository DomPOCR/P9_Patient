package com.mediscreen.patient.controller;

import com.mediscreen.patient.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class UserController {

    @Autowired
    private UserDao userDao;
    /**
     * Endpoint to show the list of user
     * @param model
     * @return the user list
     */
    @GetMapping ("/user/list")
    @ResponseStatus(HttpStatus.OK)
    public String listUser(Model model)
    {
        model.addAttribute("user", userDao.findAll());
        return "user/list";
    }

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public String home(Model model) {
        return "home";
    }

}
