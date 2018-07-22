package com.wl.spacecraft.exception.common;

import com.wl.spacecraft.exception.project.ProjectException;

public class DataFormatException extends ProjectException {
    public DataFormatException() {
    }

    public DataFormatException(String message) {
        super(message);
    }
}
