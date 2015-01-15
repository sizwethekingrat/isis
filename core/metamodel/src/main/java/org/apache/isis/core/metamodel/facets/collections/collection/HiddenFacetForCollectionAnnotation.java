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

package org.apache.isis.core.metamodel.facets.collections.collection;

import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.When;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facets.all.hide.HiddenFacet;
import org.apache.isis.core.metamodel.facets.members.hidden.HiddenFacetAbstract;

public class HiddenFacetForCollectionAnnotation extends HiddenFacetAbstract {

    public static HiddenFacet create(
            final Collection collection,
            final FacetHolder holder) {

        if (collection == null) {
            return null;
        }

        final Where where = collection.hidden();
        if (where != null && where != Where.NOT_SPECIFIED) {
            return new HiddenFacetForCollectionAnnotation(where, holder);
        }

        return null;
    }

    private HiddenFacetForCollectionAnnotation(final Where where, final FacetHolder holder) {
        super(When.ALWAYS, where, holder);
    }

    @Override
    public String hiddenReason(final ObjectAdapter targetAdapter, final Where whereContext) {
        if(!where().includes(whereContext)) {
            return null;
        }
        return "Hidden on " + where().getFriendlyName();
    }

}
