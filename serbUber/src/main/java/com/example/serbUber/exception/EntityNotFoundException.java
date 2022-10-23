package com.example.serbUber.exception;

public class EntityNotFoundException extends AppException {
    public EntityNotFoundException(String id, EntityType entityType) {
        super(id, entityType);
    }
}
