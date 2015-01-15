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

package org.apache.isis.core.metamodel.facets.param.parameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facets.Annotations;
import org.apache.isis.core.metamodel.facets.FacetFactoryAbstract;
import org.apache.isis.core.metamodel.facets.FacetedMethodParameter;

public class ParameterAnnotationFacetFactory extends FacetFactoryAbstract {

    public ParameterAnnotationFacetFactory() {
        super(FeatureType.PARAMETERS_ONLY);
    }

    @Override
    public void processParams(final ProcessParameterContext processParameterContext) {

        final Method method = processParameterContext.getMethod();
        final int paramNum = processParameterContext.getParamNum();
        final FacetedMethodParameter holder = processParameterContext.getFacetHolder();

        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (paramNum >= parameterTypes.length) {
            return; // ignore
        }

        final Annotation[] parameterAnnotations = Annotations.getParameterAnnotations(method)[paramNum];
        for (final Annotation parameterAnnotation : parameterAnnotations) {
            if (parameterAnnotation instanceof Parameter) {
                final Parameter parameter = (Parameter) parameterAnnotation;

                FacetUtil.addFacet(
                        MaxLengthFacetForParameterAnnotation.create(parameter, holder));

                FacetUtil.addFacet(
                        MustSatisfySpecificationFacetForParameterAnnotation.create(parameter, holder));

                FacetUtil.addFacet(
                        MandatoryFacetForParameterAnnotation.create(parameter, method, holder));

                if (Annotations.isString(parameterTypes[paramNum])) {
                    FacetUtil.addFacet(
                            RegExFacetForParameterAnnotation.create(parameter, holder));
                }

                return;
            }
        }
    }

}
