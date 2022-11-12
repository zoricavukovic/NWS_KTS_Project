package com.example.serbUber.exception;

public enum EntityType {
    USER ,
    LOCATION,
    VEHICLE_TYPE_INFO,
    VEHICLE,
    VERIFY,
    PHOTO,
    DRIVING,
    CHAT_ROOM,
    ROLE;

    public static String getEntityErrorMessage(String id, EntityType entityType){
        switch (entityType){
            case USER -> {

                return "User: " + id + " is not found";
            }
            case LOCATION -> {

                return "Location: " + id + " is not found";
            }
            case VEHICLE_TYPE_INFO -> {

                return "Vehicle type info: " + id + " is not found";
            }
            case VEHICLE -> {
                return "Vehicle: " + id + " is not found";
            }
            case VERIFY -> {

                return "Verify: " + id + " is not found";
            }
            case PHOTO -> {

                return "Photo: " + id + " is not found";
            }
            case DRIVING -> {

                return "Driving: " + id + " is not found";
            }
            case ROLE -> {

                return "Role: " + id + " is not found";
            }
            case CHAT_ROOM -> {

                return "ChatRoom for user : " + id + " is not found";
            }
            default -> {

                return "Entity is not found";
            }
        }

    }
}
