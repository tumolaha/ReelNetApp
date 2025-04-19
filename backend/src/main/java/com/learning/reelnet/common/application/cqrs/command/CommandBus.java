package com.learning.reelnet.common.application.cqrs.command;

import java.util.concurrent.CompletableFuture;

/**
 * Interface for command bus in CQRS pattern.
 * The command bus is responsible for routing commands to their appropriate handlers
 * and can provide cross-cutting concerns like validation, logging, and error handling.
 */
public interface CommandBus {
    /**
     * Dispatches a command to its appropriate handler.
     * This is a synchronous operation that will block until the command is handled.
     * 
     * @param <R> The type of result returned by the command
     * @param <C> The type of command to dispatch
     * @param command The command to dispatch
     * @return The result of handling the command
     * @throws Exception If an error occurs during command handling
     */
    <R, C extends Command<R>> R dispatch(C command) throws Exception;
    
    /**
     * Dispatches a command asynchronously.
     * This is an asynchronous operation that will return immediately.
     * 
     * @param <R> The type of result returned by the command
     * @param <C> The type of command to dispatch
     * @param command The command to dispatch
     * @return A CompletableFuture that will be completed with the result or an exception
     */
    <R, C extends Command<R>> CompletableFuture<R> dispatchAsync(C command);
    
    /**
     * Registers a command handler with this command bus.
     * 
     * @param <R> The type of result returned by the command
     * @param <C> The type of command to handle
     * @param handler The command handler to register
     */
    <R, C extends Command<R>> void register(CommandHandler<R, C> handler);
} 