package com.learning.reelnet.common.application.commands;

import java.io.Serializable;

/**
 * Base interface for all command objects in the application.
 * Commands represent an intention to change the state of the system.
 * <p>
 * In the Command pattern (part of CQRS), commands are sent to a command handler
 * that processes them and applies the necessary changes to the domain model.
 * <p>
 * Commands should:
 * - Be named with a verb in imperative form (e.g., CreateUser, UpdateProfile)
 * - Contain all the data needed to perform the operation
 * - Be immutable
 */
public interface Command extends Serializable {

    /**
     * Gets the command identifier.
     * This can be used for logging, tracing, or idempotency checks.
     *
     * @return the command identifier
     */
    String getCommandId();

    /**
     * Gets the name of the command.
     * By default, this is the simple name of the command class.
     *
     * @return the command name
     */
    default String getCommandName() {
        return this.getClass().getSimpleName();
    }
}