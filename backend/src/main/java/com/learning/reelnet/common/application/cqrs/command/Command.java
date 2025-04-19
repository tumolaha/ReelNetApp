package com.learning.reelnet.common.application.cqrs.command;

import java.util.UUID;

/**
 * Marker interface for commands in CQRS pattern.
 * Commands represent an intention to change the system state.
 * 
 * @param <R> The type of result returned by the command
 */
public interface Command<R> {
    /**
     * Get the unique identifier for this command.
     * Default implementation generates a new UUID for each command.
     * 
     * @return The command ID
     */
    default UUID getCommandId() {
        return UUID.randomUUID();
    }
    
    /**
     * Get the name of this command.
     * Default implementation uses the simple class name.
     * 
     * @return The command name
     */
    default String getCommandName() {
        return this.getClass().getSimpleName();
    }
} 