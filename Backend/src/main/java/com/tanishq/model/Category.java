package com.tanishq.model;

public enum Category {
    RINGS,
    EARRINGS,
    NECKLACE,
    BANGLES,
    NOSE_PINS,
    BRACELETS;
    
    public static boolean isValid(String value) {
        for (Category c : values()) {
            if (c.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}