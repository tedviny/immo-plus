package com.ted.immo.exception;

public class PropertyNotFoundException extends RuntimeException {

    public PropertyNotFoundException(Long propertyId) {
        super("Bien immobilier non trouvé " + propertyId);
    }
}
