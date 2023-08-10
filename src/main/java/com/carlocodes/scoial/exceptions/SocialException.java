package com.carlocodes.scoial.exceptions;

public class SocialException extends Exception {

    public SocialException(String message) {
        super(message);
    }

    public SocialException(String message, Throwable cause) {
        super(message, cause);
    }
}
