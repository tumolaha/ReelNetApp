package com.learning.reelnet.common.model.base;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base class for DTOs in the system.
 * Provides common fields for all DTOs such as id, audit information.
 * 
 * @param <ID> Data type of the primary key
 */
@Getter
@Setter
public abstract class BaseDto<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Object ID
     */
    private ID id;
    
    /**
     * Creation timestamp
     */
    private LocalDateTime createdAt;
    
    /**
     * Created by user
     */
    private String createdBy;
    
    /**
     * Last update timestamp
     */
    private LocalDateTime updatedAt;
    
    /**
     * Last updated by user
     */
    private String updatedBy;
    
    /**
     * Flag for soft delete
     */
    private Boolean deleted = false;
} 