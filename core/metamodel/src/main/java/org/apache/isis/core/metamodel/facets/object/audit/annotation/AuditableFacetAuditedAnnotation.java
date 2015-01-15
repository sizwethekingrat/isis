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
package org.apache.isis.core.metamodel.facets.object.audit.annotation;


import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facets.object.audit.AuditableFacet;
import org.apache.isis.core.metamodel.facets.object.audit.AuditableFacetImpl;


public class AuditableFacetAuditedAnnotation extends AuditableFacetImpl {

    public AuditableFacetAuditedAnnotation(final FacetHolder facetHolder, final Enablement enablement) {
        super(facetHolder, enablement);
    }

    public static AuditableFacet create(final Audited annotation, final FacetHolder holder) {
        if (annotation == null) {
            return null;
        }
        return new AuditableFacetAuditedAnnotation(
                holder,
                Enablement.ifDisabled(annotation.disabled()));
    }
}
