package com.wl.spacecraft.exception.login;

import com.wl.spacecraft.exception.project.ProjectException;

public class MsgCodeException extends ProjectException {
    public MsgCodeException() {
    }

    public MsgCodeException(String message) {
        super(message);
    }
}
