package com.wl.spacecraft.exception.login;

import com.wl.spacecraft.exception.project.ProjectException;

public class UserNotExistException extends UserLoginException {
    public UserNotExistException() {
    }

    public UserNotExistException(String message) {
        super(message);
    }
}
