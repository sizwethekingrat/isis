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

package org.apache.isis.core.metamodel.facets.properties.property;

import java.lang.reflect.Method;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facets.propparam.mandatory.MandatoryFacet;
import org.apache.isis.core.metamodel.facets.propparam.mandatory.MandatoryFacetAbstract;

public abstract class MandatoryFacetForPropertyAnnotation extends MandatoryFacetAbstract {

    public MandatoryFacetForPropertyAnnotation(final FacetHolder holder, final Semantics semantics) {
        super(holder, semantics);
    }

    static MandatoryFacet create(final Property property, Method method, final FacetHolder holder) {
        final Class<?> returnType = method.getReturnType();
        if (returnType.isPrimitive()) {
            return new MandatoryFacetForPropertyAnnotation.Primitive(holder);
        }
        final Optionality optionality = property.optional();
        switch (optionality) {
            case DEFAULT:
                // do nothing here; instead will rely on MandatoryFromJdoColumnAnnotationFacetFactory to perform
                // the remaining processing
                return null;
            case FALSE:
                return new MandatoryFacetForPropertyAnnotation.Required(holder);
            case TRUE:
                return new MandatoryFacetForPropertyAnnotation.Optional(holder);
        }
        return null;
    }

    public static class Primitive extends MandatoryFacetForPropertyAnnotation {
        public Primitive(final FacetHolder holder) {
            super(holder, Semantics.REQUIRED);
        }
    }

    public static class Required extends MandatoryFacetForPropertyAnnotation {
        public Required(final FacetHolder holder) {
            super(holder, Semantics.REQUIRED);
        }
    }

    public static class Optional extends MandatoryFacetForPropertyAnnotation {
        public Optional(final FacetHolder holder) {
            super(holder, Semantics.OPTIONAL);
        }
    }

}
