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

package org.apache.isis.core.metamodel.facets.actions.interaction;

import java.lang.reflect.Method;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.core.metamodel.adapter.mgr.AdapterManager;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.runtimecontext.RuntimeContext;
import org.apache.isis.core.metamodel.runtimecontext.ServicesInjector;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;

public class ActionInvocationFacetForPostsActionInvokedEventAnnotation
        extends ActionInvocationFacetForInteractionAbstract {

    public ActionInvocationFacetForPostsActionInvokedEventAnnotation(
            final Class<? extends ActionDomainEvent<?>> eventType,
            final Method method,
            final ObjectSpecification onType,
            final ObjectSpecification returnType,
            final ActionInteractionFacetAbstract actionInteractionFacet,
            final FacetHolder holder,
            final RuntimeContext runtimeContext,
            final AdapterManager adapterManager,
            final ServicesInjector servicesInjector) {
        super(eventType, method, onType, returnType, actionInteractionFacet, holder, runtimeContext, adapterManager, servicesInjector);
    }

    @Override
    protected ActionDomainEvent<?> verify(final ActionDomainEvent<?> event) {
        // will discard event if different type to that specified in the PostsActionInvokedEvent annotation.
        return event != null && eventType == event.getClass() ? event : null;
    }

}
