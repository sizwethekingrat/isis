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

package org.apache.isis.core.metamodel.facets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.metamodel.spec.feature.Contributed;
import org.apache.isis.core.metamodel.spec.feature.ObjectAction;
import org.apache.isis.core.metamodel.specloader.validator.MetaModelValidatorComposite;
import org.apache.isis.core.metamodel.specloader.validator.MetaModelValidatorVisiting;
import org.apache.isis.core.metamodel.specloader.validator.ValidationFailures;

public abstract class MethodPrefixBasedFacetFactoryAbstract extends FacetFactoryAbstract implements MethodPrefixBasedFacetFactory {

    private final List<String> prefixes;

    protected static final Object[] NO_PARAMETERS = new Object[0];
    protected static final Class<?>[] NO_PARAMETERS_TYPES = new Class<?>[0];

    private final OrphanValidation orphanValidation;

    protected enum OrphanValidation {
        VALIDATE,
        DONT_VALIDATE
    }
    
    public MethodPrefixBasedFacetFactoryAbstract(final List<FeatureType> featureTypes, final OrphanValidation orphanValidation, final String... prefixes) {
        super(featureTypes);
        this.orphanValidation = orphanValidation;
        this.prefixes = Collections.unmodifiableList(Arrays.asList(prefixes));
    }

    @Override
    public List<String> getPrefixes() {
        return prefixes;
    }

    @Override
    public void refineMetaModelValidator(final MetaModelValidatorComposite metaModelValidator, final IsisConfiguration configuration) {
        if(orphanValidation == OrphanValidation.DONT_VALIDATE) {
            return;
        }
        metaModelValidator.add(new MetaModelValidatorVisiting(new MetaModelValidatorVisiting.Visitor() {

            @Override
            public boolean visit(final ObjectSpecification objectSpec, final ValidationFailures validationFailures) {
                final List<ObjectAction> objectActions = objectSpec.getObjectActions(Contributed.EXCLUDED);
                for (final ObjectAction objectAction : objectActions) {
                    for (final String prefix : prefixes) {
                        final String actionId = objectAction.getId();
                        if (actionId.startsWith(prefix) && prefix.length() < actionId.length()) {
                            validationFailures.add("Method '%s#%s' has prefix %s, is probably a supporting method for a property, collection or action.  If the method is intended to be an action, then rename and use @ActionLayout(named=\"...\") or ignore completely using @Programmatic", objectSpec.getIdentifier().getClassName(), actionId, prefix);
                        }
                    }
                }
                return true;
            }
        }));
    }
}
