package com.ted.immo.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long userId){
        super("User non trouvé avec cet identifiant" + userId);
    }
}
