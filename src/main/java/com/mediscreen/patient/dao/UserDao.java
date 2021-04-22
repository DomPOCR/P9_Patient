package com.mediscreen.patient.dao;

import com.mediscreen.patient.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDao extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    /**
     *
     * @param userName
     * @return
     */
    User findByUsername(String userName);
}
