package com.example.serbUber.exception;

public class EntityNotFoundException extends AppException {
    public EntityNotFoundException(String id, EntityType entityType) {
        super(id, entityType);
    }

    public EntityNotFoundException(Long id, EntityType entityType) {
        super(id.toString(), entityType);
    }

}
