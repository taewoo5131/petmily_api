package com.petmily.api.Exception.custom;

public class TokenException extends RuntimeException {
    public TokenException() {
        super();
    }

    public TokenException(String message) {
        super(message);
    }
}
