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

package org.apache.isis.core.metamodel.facets.collections.interaction;

import java.util.Collection;
import org.apache.isis.applib.services.eventbus.AbstractDomainEvent;
import org.apache.isis.applib.services.eventbus.CollectionDomainEvent;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facets.InteractionHelper;
import org.apache.isis.core.metamodel.facets.SingleValueFacetAbstract;
import org.apache.isis.core.metamodel.facets.collections.modify.CollectionRemoveFromFacet;
import org.apache.isis.core.metamodel.facets.propcoll.accessor.PropertyOrCollectionAccessorFacet;
import org.apache.isis.core.metamodel.runtimecontext.ServicesInjector;


public abstract class CollectionRemoveFromFacetForInteractionAbstract
    extends SingleValueFacetAbstract<Class<? extends CollectionDomainEvent<?,?>>>
    implements CollectionRemoveFromFacet {

    public static Class<? extends Facet> type() {
	    return CollectionRemoveFromFacet.class;
    }

    private final PropertyOrCollectionAccessorFacet getterFacet;
    private final CollectionRemoveFromFacet collectionRemoveFromFacet;
    private final CollectionInteractionFacetAbstract collectionDomainEventFacet;

    private final InteractionHelper interactionHelper;

    public CollectionRemoveFromFacetForInteractionAbstract(
            final Class<? extends CollectionDomainEvent<?, ?>> eventType,
            final PropertyOrCollectionAccessorFacet getterFacet,
            final CollectionRemoveFromFacet collectionRemoveFromFacet,
            final CollectionInteractionFacetAbstract collectionDomainEventFacet,
            final ServicesInjector servicesInjector,
            final FacetHolder holder) {
        super(type(), eventType, holder);
        this.getterFacet = getterFacet;
        this.collectionRemoveFromFacet = collectionRemoveFromFacet;
        this.collectionDomainEventFacet = collectionDomainEventFacet;
        this.interactionHelper = new InteractionHelper(servicesInjector);
    }

    @Override
    public void remove(ObjectAdapter targetAdapter,
                       ObjectAdapter referencedObjectAdapter) {
        if (this.collectionRemoveFromFacet == null) {
            return;
        }
        if(!interactionHelper.hasEventBusService()) {
            collectionRemoveFromFacet.remove(targetAdapter,
                    referencedObjectAdapter);
            return;
        }


        try {

            final Object referencedObject = ObjectAdapter.Util.unwrap(referencedObjectAdapter);

            // get hold of underlying collection
            final Object collection = getterFacet.getProperty(targetAdapter);

            // don't post event if the collections does not contain object
            if (!((Collection<?>) collection).contains(referencedObject)) {
                return;
            }

            // contains the element, so
            // execute the remove wrapped between the executing and executed events ...

            // pick up existing event (saved in thread local during the validation phase)
            final CollectionDomainEvent<?, ?> existingEvent = collectionDomainEventFacet.currentInteraction.get();

            // ... post the executing event
            final CollectionDomainEvent<?, ?> event = interactionHelper.postEventForCollection(
                    value(), existingEvent, AbstractDomainEvent.Phase.EXECUTING,
                    getIdentified(), targetAdapter, CollectionDomainEvent.Of.REMOVE_FROM, referencedObject);

            // ... perform remove
            collectionRemoveFromFacet.remove(targetAdapter, referencedObjectAdapter);

            // ... and post the executed event
            interactionHelper.postEventForCollection(
                    value(), verify(event), AbstractDomainEvent.Phase.EXECUTED,
                    getIdentified(), targetAdapter, CollectionDomainEvent.Of.REMOVE_FROM, referencedObject);

        } finally {
            // clean up
            collectionDomainEventFacet.currentInteraction.set(null);
        }
    }

    /**
     * Optional hook to allow the facet implementation for the deprecated {@link org.apache.isis.applib.annotation.PostsCollectionRemovedFromEvent} annotation
     * to discard the event if of a different type.
     */
    protected CollectionDomainEvent<?, ?> verify(CollectionDomainEvent<?, ?> event) {
        return event;
    }

}
