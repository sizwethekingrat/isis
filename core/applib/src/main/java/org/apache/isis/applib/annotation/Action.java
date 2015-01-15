/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.isis.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.isis.applib.services.eventbus.ActionInteractionEvent;

/**
 * Domain semantics for domain object collection.
 */
@Inherited
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {


    /**
     * Indicates that an invocation of the action should be posted to the
     * {@link org.apache.isis.applib.services.eventbus.EventBusService event bus} using a custom (subclass of)
     * {@link org.apache.isis.applib.services.eventbus.ActionInteractionEvent}.
     *
     * <p>For example:
     * </p>
     *
     * <pre>
     * public static class StartDateChangedEvent extends ActionInvokedEvent {}
     *
     * &#64;PostsActionInvokedEvent(StartDateChangedEvent.class)
     * public void changeStartDate(final Date startDate) { ...}
     * </pre>
     *
     * <p>
     * Only domain services should be registered as subscribers; only domain services are guaranteed to be instantiated and
     * resident in memory.  The typical implementation of a domain service subscriber is to identify the impacted entities,
     * load them using a repository, and then to delegate to the event to them.
     * </p>
     *
     * <p>
     * This subclass must provide a no-arg constructor; the fields are set reflectively.
     * </p>
     */
    Class<? extends ActionInteractionEvent<?>> interaction() default ActionInteractionEvent.Default.class;


    // //////////////////////////////////////

    /**
     * Indicates when the action is not visible to the user.
     *
     * <p>
     * For a repository action, is equivalent to {@link NotContributed} and also
     * {@link NotInServiceMenu}.
     * </p>
     *
     * <p>
     * If a repository action is contributed as a property, then the {@link Hidden#where() where}
     * attribute can be used to optionally hide the contributed property in a table (using
     * {@link Where#ALL_TABLES}, {@link Where#STANDALONE_TABLES}, {@link Where#PARENTED_TABLES}
     * as appropriate).
     * </p>
     */
    Where hidden() default Where.NOWHERE;


    // //////////////////////////////////////


    /**
     * The action semantics, either {@link SemanticsOf#SAFE safe} (query-only),
     * {@link SemanticsOf#IDEMPOTENT idempotent} or
     * {@link SemanticsOf#NON_IDEMPOTENT non-idempotent}.
     */
    SemanticsOf semantics() default SemanticsOf.NON_IDEMPOTENT;


    // //////////////////////////////////////


    /**
     * Whether an action can be invoked on a single object and/or on many objects in a collection.
     *
     * <p>
     * Actions to be invoked on collection (currently) have a number of constraints:
     * <ul>
     * <li>It must take no arguments
     * <li>It cannot be hidden (any annotations or supporting methods to that effect will be
     *     ignored).
     * <li>It cannot be disabled (any annotations or supporting methods to that effect will be
     *     ignored).
     * </ul>
     *
     * <p>
     * Has no meaning if annotated on an action of a domain service.
     * </p>
     */
    InvokeOn invokeOn() default InvokeOn.OBJECT_ONLY;

    // //////////////////////////////////////


    /**
     * Whether the action invocation should be reified into a {@link org.apache.isis.applib.services.command.Command} object.
     */
    CommandReification command() default CommandReification.AS_CONFIGURED;

    /**
     * How the {@link org.apache.isis.applib.services.command.Command Command} object provided by the
     * {@link org.apache.isis.applib.services.command.CommandContext CommandContext} domain service should be persisted.
     */
    CommandPersistence commandPersistence() default CommandPersistence.PERSISTED;

    /**
     * How the command/action should be executed.
     *
     * <p>
     * If the corresponding {@link org.apache.isis.applib.services.command.Command Command} object is persisted,
     * then its {@link org.apache.isis.applib.services.command.Command#getExecuteIn() invocationType} property
     * will be set to this value.
     * </p>
     */
    CommandExecuteIn commandExecuteIn() default CommandExecuteIn.FOREGROUND;


    // //////////////////////////////////////


    /**
     * Whether changes to the object should be published.
     *
     * <p>
     * Requires that an implementation of the {@link org.apache.isis.applib.services.publish.PublishingService} is
     * registered with the framework.
     * </p>
     */
    Publishing publishing() default Publishing.AS_CONFIGURED;


    // TODO: factor out PayloadFactory.Default so similar to interaction
    Class<? extends PublishingPayloadFactoryForAction> publishingPayloadFactory() default PublishingPayloadFactoryForAction.class;


    // //////////////////////////////////////

    /**
     * The type-of the elements returned by the action.
     * @return
     */
    Class<?> typeOf() default Object.class;

}