package com.wl.spacecraft.exception.login;

import com.wl.spacecraft.exception.project.ProjectException;

public class UserLoginException extends ProjectException {
    public UserLoginException() {
    }

    public UserLoginException(String message) {
        super(message);
    }
}
