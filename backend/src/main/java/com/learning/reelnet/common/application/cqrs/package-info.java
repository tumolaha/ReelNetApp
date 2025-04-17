/**
 * This package contains the base interfaces and components for implementing
 * the Command Query Responsibility Segregation (CQRS) pattern.
 * <p>
 * CQRS separates the responsibility of handling commands (write operations
 * that change state) from queries (read operations that return data).
 * <p>
 * Key components:
 * <ul>
 *   <li>Command: Interface for objects that represent an intention to change state</li>
 *   <li>CommandHandler: Interface for components that process commands</li>
 *   <li>Query: Interface for objects that represent a request for data</li>
 *   <li>QueryHandler: Interface for components that process queries</li>
 * </ul>
 */
package com.learning.reelnet.common.application.cqrs; 