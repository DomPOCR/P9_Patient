package com.mediscreen.patient.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DataAlreadyExistException extends Exception {

    // Pour le log4j2
    final Logger logger = LogManager.getLogger(this.getClass().getName());

    public DataAlreadyExistException(String message) {

        super(message);
        logger.error(message);
    }
}
