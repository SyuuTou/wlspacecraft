package com.wl.spacecraft.exception.account;

import com.wl.spacecraft.exception.project.ProjectException;

public class AccountException extends ProjectException {
    public AccountException() {
    }

    public AccountException(String message) {
        super(message);
    }
}
