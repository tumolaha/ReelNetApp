package com.learning.reelnet.common.application.cqrs.command;

/**
 * Base interface for all command handlers in the application.
 * Command handlers are responsible for processing commands and applying
 * the necessary changes to the domain model.
 * <p>
 * In the Command pattern (part of CQRS), each command type typically has
 * a corresponding command handler that knows how to process it.
 *
 * @param <T> the type of command this handler can process
 * @param <R> the type of result produced by handling the command
 */
public interface CommandHandler<T extends Command, R> {

    /**
     * Handles the given command and produces a result.
     *
     * @param command the command to handle
     * @return the result of handling the command
     * @throws Exception if an error occurs while handling the command
     */
    R handle(T command) throws Exception;
} 