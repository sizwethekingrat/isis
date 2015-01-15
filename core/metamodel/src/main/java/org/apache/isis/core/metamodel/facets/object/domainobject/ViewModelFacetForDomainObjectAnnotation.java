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

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.core.metamodel.adapter.mgr.AdapterManager;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facets.object.viewmodel.ViewModelFacet;
import org.apache.isis.core.metamodel.facets.object.viewmodel.ViewModelFacetDeclarativeAbstract;
import org.apache.isis.core.metamodel.runtimecontext.ServicesInjector;
import org.apache.isis.core.metamodel.spec.SpecificationLoader;

public class ViewModelFacetForDomainObjectAnnotation extends ViewModelFacetDeclarativeAbstract {

    public static ViewModelFacet create(
            final DomainObject domainObject,
            final SpecificationLoader specificationLoader,
            final AdapterManager adapterManager,
            final ServicesInjector servicesInjector,
            final FacetHolder holder) {

        final boolean viewModel = domainObject.viewModel();
        if(!viewModel) {
            return null;
        }
        return new ViewModelFacetForDomainObjectAnnotation(holder, specificationLoader, adapterManager, servicesInjector);
    }

    private ViewModelFacetForDomainObjectAnnotation(
            final FacetHolder holder,
            final SpecificationLoader specificationLoader,
            final AdapterManager adapterManager,
            final ServicesInjector servicesInjector) {
        super(holder, specificationLoader, adapterManager, servicesInjector);
    }

}
