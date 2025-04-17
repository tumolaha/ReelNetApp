package com.learning.reelnet.common.model.enums;

/**
 * Enum defining gender options used in the system.
 */
public enum Gender {
    /**
     * Male gender
     */
    MALE("male", "Nam"),
    
    /**
     * Female gender
     */
    FEMALE("female", "Nữ"),
    
    /**
     * Other/Not specified gender
     */
    OTHER("other", "Khác");
    
    /**
     * The code of the gender
     */
    private final String code;
    
    /**
     * The description of the gender
     */
    private final String description;
    
    Gender(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * Get the code of the gender
     * 
     * @return The gender code
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Get the description of the gender
     * 
     * @return The gender description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Find a gender by its code
     * 
     * @param code The gender code
     * @return The corresponding gender, null if not found
     */
    public static Gender fromCode(String code) {
        if (code == null) {
            return null;
        }
        
        for (Gender gender : Gender.values()) {
            if (gender.getCode().equalsIgnoreCase(code)) {
                return gender;
            }
        }
        
        return null;
    }
    
    /**
     * Check if the current gender is male
     * 
     * @return true if the gender is male
     */
    public boolean isMale() {
        return this == MALE;
    }
    
    /**
     * Check if the current gender is female
     * 
     * @return true if the gender is female
     */
    public boolean isFemale() {
        return this == FEMALE;
    }
} 