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

package org.apache.isis.core.metamodel.facets.object.domainobject;

import com.google.common.base.Strings;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.When;
import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facets.object.immutable.ImmutableFacet;
import org.apache.isis.core.metamodel.facets.object.immutable.ImmutableFacetAbstract;

public class ImmutableFacetForDomainObjectAnnotation extends ImmutableFacetAbstract {

    private final String reason;

    public static ImmutableFacet create(
            final DomainObject domainObject,
            final IsisConfiguration configuration,
            final FacetHolder holder) {

        final Editing editing       = domainObject != null? domainObject.editing() : Editing.AS_CONFIGURED;
        final String disabledReason = domainObject != null? domainObject.editingDisabledReason(): "Disabled";

        switch (editing) {
            case AS_CONFIGURED:

                if(holder.containsDoOpFacet(ImmutableFacet.class)) {
                    // do not replace
                    return null;
                }

                final EditingObjectsConfiguration setting = EditingObjectsConfiguration.parse(configuration);
                return setting == EditingObjectsConfiguration.FALSE
                        ? new ImmutableFacetForDomainObjectAnnotation(disabledReason, holder)
                        : null;
            case DISABLED:
                return new ImmutableFacetForDomainObjectAnnotation(disabledReason, holder);
            case ENABLED:
                return null;
        }
        return null;
    }

    public ImmutableFacetForDomainObjectAnnotation(final String reason, final FacetHolder holder) {
        super(When.ALWAYS, holder);
        this.reason = reason;
    }

    @Override
    public String disabledReason(final ObjectAdapter targetAdapter) {
        return !Strings.isNullOrEmpty(reason)
                ? reason
                : super.disabledReason(targetAdapter);
    }

    @Override
    public void copyOnto(final FacetHolder holder) {
        final Facet facet = new ImmutableFacetForDomainObjectAnnotation(reason, holder);
        FacetUtil.addFacet(facet);
    }

}
