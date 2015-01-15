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

import org.apache.isis.applib.services.eventbus.CollectionDomainEvent;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facets.collections.modify.CollectionRemoveFromFacet;
import org.apache.isis.core.metamodel.facets.propcoll.accessor.PropertyOrCollectionAccessorFacet;
import org.apache.isis.core.metamodel.runtimecontext.ServicesInjector;

public class CollectionRemoveFromFacetForCollectionInteractionAnnotation extends
        CollectionRemoveFromFacetForInteractionAbstract {

    public CollectionRemoveFromFacetForCollectionInteractionAnnotation(
            final Class<? extends CollectionDomainEvent<?, ?>> eventType,
            final PropertyOrCollectionAccessorFacet getterFacet,
            final CollectionRemoveFromFacet collectionRemoveFromFacet,
            final CollectionInteractionFacetAbstract collectionInteractionFacet,
            final ServicesInjector servicesInjector,
            final FacetHolder holder) {
       super(eventType, getterFacet, collectionRemoveFromFacet, collectionInteractionFacet, servicesInjector, holder);
    }

}
