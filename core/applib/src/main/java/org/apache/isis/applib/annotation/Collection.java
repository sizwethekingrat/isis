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
import org.apache.isis.applib.services.eventbus.CollectionInteractionEvent;

/**
 * Domain semantics for domain object collection.
 */
@Inherited
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Collection {

    /**
     * Indicates that changes to the collection that should be posted to the
     * {@link org.apache.isis.applib.services.eventbus.EventBusService event bus} using a custom (subclass of)
     * {@link org.apache.isis.applib.services.eventbus.CollectionInteractionEvent}.
     *
     * <p>For example:
     * </p>
     * <pre>
     * public class Order {
     *   public static class OrderLineItems extends CollectionInteractionEvent {}
     *
     *   &#64;CollectionInteraction(OrderLineItems.class)
     *   public SortedSet&lt;OrderLine&gt; getLineItems() { ...}
     * }
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
    Class<? extends CollectionInteractionEvent<?,?>> interaction() default CollectionInteractionEvent.Default.class;

    // //////////////////////////////////////

    /**
     * Indicates when the collection is not visible to the user.
     */
    Where hidden() default Where.NOWHERE;


    // //////////////////////////////////////

    /**
     * Indicates when the collection is not editable by the user.
     *
     * <p>
     * Note that if the owning domain object is {@link DomainObject#notEditable()}, then that will take precedence.
     * </p>
     */
    Where disabled() default Where.NOWHERE;

    /**
     * If {@link #disabled()} (in any {@link Where} context), then the reason to provide to the user as to why the
     * collection cannot be edited.
     * @return
     */
    String disabledReason();


    // //////////////////////////////////////

    /**
     * The type-of the elements held within the collection.
     * @return
     */
    Class<?> typeOf();

}