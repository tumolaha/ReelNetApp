package com.learning.reelnet.common.application.cqrs.command;

/**
 * Interface for command handlers in CQRS pattern.
 * A command handler is responsible for executing the business logic
 * associated with a specific command and returning a result.
 * 
 * @param <R> The type of result returned by the command
 * @param <C> The type of command to handle
 */
public interface CommandHandler<R, C extends Command<R>> {
    /**
     * Handles the command and returns a result.
     * This method contains the business logic for processing the command.
     * 
     * @param command The command to handle
     * @return The result of handling the command
     * @throws Exception If an error occurs during command processing
     */
    R handle(C command) throws Exception;
    
    /**
     * Get the command class this handler can process.
     * Default implementation uses generic type inference.
     * 
     * @return The class of commands this handler can process
     */
    default Class<C> getCommandClass() {
        throw new UnsupportedOperationException("Command handlers must override getCommandClass() method");
    }
} 