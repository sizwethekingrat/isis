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

package org.apache.isis.core.metamodel.facets.param.layout;

import java.lang.annotation.Annotation;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facets.Annotations;
import org.apache.isis.core.metamodel.facets.FacetFactoryAbstract;
import org.apache.isis.core.metamodel.facets.FacetedMethodParameter;

public class ParameterLayoutFacetFactory extends FacetFactoryAbstract {

    public ParameterLayoutFacetFactory() {
        super(FeatureType.PARAMETERS_ONLY);
    }

    @Override
    public void processParams(final ProcessParameterContext processParameterContext) {
        final Class<?>[] parameterTypes = processParameterContext.getMethod().getParameterTypes();
        if (processParameterContext.getParamNum() >= parameterTypes.length) {
            // ignore
            return;
        }
        
        final Annotation[] parameterAnnotations = Annotations.getParameterAnnotations(processParameterContext.getMethod())[processParameterContext.getParamNum()];
        for (final Annotation parameterAnnotation : parameterAnnotations) {
            if (parameterAnnotation instanceof ParameterLayout) {
                final ParameterLayout parameterLayout = (ParameterLayout) parameterAnnotation;
                addFacets(processParameterContext, parameterLayout);
                return;
            }
        }
    }

    protected void addFacets(ProcessParameterContext processParameterContext, ParameterLayout parameterLayout) {
        final FacetedMethodParameter facetHolder = processParameterContext.getFacetHolder();

        FacetUtil.addFacet(CssClassFacetForParameterLayoutAnnotation.create(parameterLayout, facetHolder));
        FacetUtil.addFacet(DescribedAsFacetForParameterLayoutAnnotation.create(parameterLayout, facetHolder));
        FacetUtil.addFacet(LabelAtFacetForParameterLayoutAnnotation.create(parameterLayout, facetHolder));
        FacetUtil.addFacet(MultiLineFacetForParameterLayoutAnnotation.create(parameterLayout, facetHolder));
        FacetUtil.addFacet(NamedFacetForParameterLayoutAnnotation.create(parameterLayout, facetHolder));
        FacetUtil.addFacet(RenderedAdjustedFacetForParameterLayoutAnnotation.create(parameterLayout, facetHolder));
        FacetUtil.addFacet(TypicalLengthFacetForParameterLayoutAnnotation.create(parameterLayout, facetHolder));

    }

}
