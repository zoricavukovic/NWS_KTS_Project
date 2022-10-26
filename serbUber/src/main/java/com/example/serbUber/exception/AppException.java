package com.example.serbUber.exception;

public class AppException extends Exception {

    private final String message;

    public AppException(String id, EntityType entityType) {
        super();
        this.message = createExceptionMessage(id, entityType);
    }

    public AppException(String message) {
        super();
        this.message = message;
    }

    private String createExceptionMessage(String id, EntityType entityType) {
        if (entityType.equals(EntityType.USER)){

            return "User with id: " + id + " is not found";
        }

        return "Data not found.";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
