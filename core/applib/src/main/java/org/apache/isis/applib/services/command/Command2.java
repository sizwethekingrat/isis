/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.isis.applib.services.command;

import java.util.List;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.eventbus.ActionInteractionEvent;

/**
 * An extension to {@link org.apache.isis.applib.services.command.Command} that makes the
 * relationship with {@link org.apache.isis.applib.services.eventbus.ActionInteractionEvent} bi-directional.
 */
public interface Command2 extends Command {

    /**
     * The current (most recently pushed) {@link org.apache.isis.applib.services.eventbus.ActionInteractionEvent}.
     *
     * <p>
     *     Note that the {@link org.apache.isis.applib.services.eventbus.ActionInteractionEvent} itself is mutable,
     *     as its {@link org.apache.isis.applib.services.eventbus.ActionInteractionEvent#getPhase() phase} changes from
     *     {@link org.apache.isis.applib.services.eventbus.AbstractInteractionEvent.Phase#EXECUTING executing} to
     *     {@link org.apache.isis.applib.services.eventbus.AbstractInteractionEvent.Phase#EXECUTED executed}.  The
     *     event returned from this method will always be in one or other of these phases.
     * </p>
     * @return
     */
    @Programmatic
    ActionInteractionEvent<?> peekActionInteractionEvent();

    /**
     * <b>NOT API</b>: intended to be called only by the framework.
     *
     * <p>
     * Push a new {@link org.apache.isis.applib.services.eventbus.ActionInteractionEvent}
     * onto the stack of events held by the command.
     * </p>
     */
    @Programmatic
    void pushActionInteractionEvent(ActionInteractionEvent<?> event);

    /**
     * <b>NOT API</b>: intended to be called only by the framework.
     *
     * <p>
     * Push a new {@link org.apache.isis.applib.services.eventbus.ActionInteractionEvent}
     * onto the stack of events held by the command.
     * </p>
     */
    @Programmatic
    ActionInteractionEvent<?> popActionInteractionEvent();

    /**
     * Returns the {@link org.apache.isis.applib.services.eventbus.ActionInteractionEvent}s in the order that they
     * were {@link #pushActionInteractionEvent(org.apache.isis.applib.services.eventbus.ActionInteractionEvent) pushed}.
     */
    @Programmatic
    List<ActionInteractionEvent<?>> flushActionInteractionEvents();
}
