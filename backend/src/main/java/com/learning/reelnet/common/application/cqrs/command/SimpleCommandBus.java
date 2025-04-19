package com.learning.reelnet.common.application.cqrs.command;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.learning.reelnet.common.infrastructure.events.EventPublisher;
import com.learning.reelnet.common.infrastructure.events.DomainEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * Simple implementation of CommandBus that uses Spring's ApplicationContext
 * to find command handlers and supports both synchronous and asynchronous
 * command dispatching.
 */
@Slf4j
@Component
public class SimpleCommandBus implements CommandBus {

    private final ApplicationContext applicationContext;
    private final EventPublisher eventPublisher;
    private final Map<Class<?>, CommandHandler<?, ?>> handlers = new ConcurrentHashMap<>();

    public SimpleCommandBus(ApplicationContext applicationContext, EventPublisher eventPublisher) {
        this.applicationContext = applicationContext;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public <R, C extends Command<R>> R dispatch(C command) throws Exception {
        log.debug("Dispatching command: {}", command.getCommandName());
        
        CommandHandler<R, C> handler = findHandler(command);
        try {
            R result = handler.handle(command);
            
            // If the result is an Event or collection of Events, publish them
            if (result instanceof DomainEvent) {
                eventPublisher.publish((DomainEvent) result);
            } else if (result instanceof Iterable) {
                Iterable<?> iterable = (Iterable<?>) result;
                for (Object item : iterable) {
                    if (item instanceof DomainEvent) {
                        eventPublisher.publish((DomainEvent) item);
                    }
                }
            }
            
            return result;
        } catch (Exception e) {
            log.error("Error handling command {}: {}", command.getCommandName(), e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    @Async
    public <R, C extends Command<R>> CompletableFuture<R> dispatchAsync(C command) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return dispatch(command);
            } catch (Exception e) {
                throw new RuntimeException("Error dispatching command asynchronously", e);
            }
        });
    }
    
    @Override
    public <R, C extends Command<R>> void register(CommandHandler<R, C> handler) {
        Class<C> commandClass;
        try {
            commandClass = handler.getCommandClass();
        } catch (UnsupportedOperationException e) {
            log.warn("Command handler {} did not implement getCommandClass(), skipping registration", 
                    handler.getClass().getSimpleName());
            return;
        }
        
        log.debug("Registering handler {} for command {}", 
                handler.getClass().getSimpleName(), 
                commandClass.getSimpleName());
                
        handlers.put(commandClass, handler);
    }
    
    @SuppressWarnings("unchecked")
    private <R, C extends Command<R>> CommandHandler<R, C> findHandler(C command) {
        Class<?> commandType = command.getClass();
        
        // First check if we have a registered handler
        CommandHandler<R, C> handler = (CommandHandler<R, C>) handlers.get(commandType);
        
        // If not, try to find it in the application context
        if (handler == null) {
            String handlerBeanName = commandType.getSimpleName() + "Handler";
            try {
                handler = (CommandHandler<R, C>) applicationContext.getBean(handlerBeanName);
                // Cache the handler for future use
                handlers.put(commandType, handler);
            } catch (Exception e) {
                throw new IllegalStateException(
                    "No handler registered for command " + command.getCommandName(), e);
            }
        }
        
        return handler;
    }
} 