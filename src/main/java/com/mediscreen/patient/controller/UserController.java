package com.mediscreen.patient.controller;

import com.mediscreen.patient.model.User;

import com.mediscreen.patient.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@Controller
public class UserController {

    // Pour le log4j2
    final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    private UserService userService;

   /* @Autowired
    public UserController (UserService userService) {
        this.userService = userService;
    }*/

    public UserController() {

    }

    /**
     * HomePage
     *
     * @param model
     * @return HomePage
     */
    @GetMapping("/home")
    @ResponseStatus(HttpStatus.OK)
    public String home(Model model) {
        return "home";
    }

    /**
     * Endpoint to show the list of user
     *
     * @param model
     * @return the user list
     */
    @GetMapping(value ="/user/list")
    @ResponseStatus(HttpStatus.OK)
    public String listUser(Model model) {
        model.addAttribute("userList", userService.findAll());
        logger.info("user/list : OK");
        return "user/list";
    }

    /**
     * Endpoint to validate the info of user
     *
     * @param user,  user to be added
     * @param result technical result
     * @param model  public interface model, model can be accessed and attributes can be added
     * @return user/list if OK or stay user/add if KO
     */
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userService.saveUser(user);
            model.addAttribute("users", userService.findAll());
            logger.info("user/validate : ended for user : " + user.toString());
            return "redirect:/user/list";
        }
        logger.error("user/validate : error for user : " + user.toString());
        return "user/add";
    }

    /**
     * @param user
     * @return Add user page
     */
    @GetMapping(value ="user/add")
    @ResponseStatus(HttpStatus.OK)
    public String addUser(User user) {
        logger.info("GET /user/add : Start");
        return "user/add";
    }

    /**
     * Endpoint to display user updating form
     *
     * @param id    the user id
     * @param model public interface model, model can be accessed and attributes can be added
     * @return user/update if OK
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        logger.info("showUpdateForm start for id " + id);
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setPassword("");
        model.addAttribute("user", user);
        logger.info("showUpdateForm ended for id " + id);
        return "user/update";
    }

    /**
     * Endpoint to validate the user updating form
     *
     * @param id
     * @param user   the user id
     * @param result technical result
     * @param model  public interface model, model can be accessed and attributes can be added
     * @return user/list if ok or user/update if ko
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {

        user.setId(id);

        if (result.hasErrors()) {
            String error = result.getFieldErrors().get(0).getDefaultMessage();
            String field = result.getFieldErrors().get(0).getField();
            logger.error("trade/update : error for user : " + user.toString() + " : " + field + " " + error);
            return "user/update";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

        userService.saveUser(user);
        model.addAttribute("users", userService.findAll());
        logger.info("user//update : ended for user :" + user.toString());
        return "redirect:/user/list";
    }

    /**
     * Endpoint to delete a user
     *
     * @param id    the user id to delete
     * @param model public interface model, model can be accessed and attributes can be added
     * @return user/list if ok
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {

        logger.info("delete user start for id " + id);
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userService.deleteUser(user);
        model.addAttribute("users", userService.findAll());
        logger.info("user/delete ended for : " + user.toString());
        return "redirect:/user/list";
    }
}


