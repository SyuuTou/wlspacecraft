package com.wl.spacecraft.exception.login;


import com.wl.spacecraft.exception.project.ProjectException;

public class TokenIlleaglException extends UserLoginException {
    public TokenIlleaglException() {
        super();
    }

    public TokenIlleaglException(String message) {
        super(message);
    }
}
