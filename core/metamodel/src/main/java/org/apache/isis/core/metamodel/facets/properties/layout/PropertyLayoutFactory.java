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

package org.apache.isis.core.metamodel.facets.properties.layout;

import java.util.Properties;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facets.Annotations;
import org.apache.isis.core.metamodel.facets.ContributeeMemberFacetFactory;
import org.apache.isis.core.metamodel.facets.FacetFactoryAbstract;
import org.apache.isis.core.metamodel.facets.all.describedas.DescribedAsFacet;
import org.apache.isis.core.metamodel.facets.all.hide.HiddenFacet;
import org.apache.isis.core.metamodel.facets.all.named.NamedFacet;
import org.apache.isis.core.metamodel.facets.members.cssclass.CssClassFacet;
import org.apache.isis.core.metamodel.facets.objpropparam.typicallen.TypicalLengthFacet;
import org.apache.isis.core.metamodel.facets.propparam.layout.LabelAtFacet;
import org.apache.isis.core.metamodel.facets.propparam.multiline.MultiLineFacet;
import org.apache.isis.core.metamodel.facets.propparam.renderedadjusted.RenderedAdjustedFacet;

public class PropertyLayoutFactory extends FacetFactoryAbstract implements ContributeeMemberFacetFactory {

    public PropertyLayoutFactory() {
        super(FeatureType.PROPERTIES_ONLY);
    }

    @Override
    public void process(final ProcessMethodContext processMethodContext) {

        final FacetHolder holder = processMethodContext.getFacetHolder();

        Properties properties = processMethodContext.metadataProperties("propertyLayout");
        if(properties == null) {
            // alternate key
            properties = processMethodContext.metadataProperties("layout");
        }
        final PropertyLayout propertyLayout = Annotations.getAnnotation(processMethodContext.getMethod(), PropertyLayout.class);


        // cssClass
        CssClassFacet cssClassFacet = CssClassFacetOnPropertyFromLayoutProperties.create(properties, holder);
        if(cssClassFacet == null) {
            cssClassFacet = CssClassFacetForPropertyLayoutAnnotation.create(propertyLayout, holder);
        }
        FacetUtil.addFacet(cssClassFacet);


        // describedAs
        DescribedAsFacet describedAsFacet = DescribedAsFacetOnPropertyFromLayoutProperties.create(properties, holder);
        if(describedAsFacet == null) {
            describedAsFacet = DescribedAsFacetForPropertyLayoutAnnotation.create(propertyLayout, holder);
        }
        FacetUtil.addFacet(describedAsFacet);


        // hidden
        HiddenFacet hiddenFacet = HiddenFacetOnPropertyFromLayoutProperties.create(properties, holder);
        if(hiddenFacet == null) {
            hiddenFacet = HiddenFacetForPropertyLayoutAnnotation.create(propertyLayout, holder);
        }
        FacetUtil.addFacet(hiddenFacet);


        // labelAt
        LabelAtFacet labelAtFacet = LabelAtFacetOnPropertyFromLayoutProperties.create(properties, holder);
        if(labelAtFacet == null) {
            labelAtFacet = LabelAtFacetForPropertyLayoutAnnotation.create(propertyLayout, holder);
        }
        FacetUtil.addFacet(labelAtFacet);


        // multiLine
        MultiLineFacet multiLineFacet = MultiLineFacetOnPropertyFromLayoutProperties.create(properties, holder);
        if(multiLineFacet == null) {
            multiLineFacet = MultiLineFacetForPropertyLayoutAnnotation.create(propertyLayout, holder);
        }
        FacetUtil.addFacet(multiLineFacet);


        // named
        NamedFacet namedFacet = NamedFacetOnPropertyFromLayoutProperties.create(properties, holder);
        if(namedFacet == null) {
            namedFacet = NamedFacetForPropertyLayoutAnnotation.create(propertyLayout, holder);
        }
        FacetUtil.addFacet(namedFacet);


        // renderedAsDayBefore
        RenderedAdjustedFacet renderedAdjustedFacet = RenderedAdjustedFacetOnPropertyFromLayoutProperties.create(properties, holder);
        if(renderedAdjustedFacet == null) {
            renderedAdjustedFacet = RenderedAdjustedFacetForPropertyLayoutAnnotation.create(propertyLayout, holder);
        }
        FacetUtil.addFacet(renderedAdjustedFacet);


        // typicalLength
        TypicalLengthFacet typicalLengthFacet = TypicalLengthFacetOnPropertyFromLayoutProperties.create(properties, holder);
        if(typicalLengthFacet == null) {
            typicalLengthFacet = TypicalLengthFacetForPropertyLayoutAnnotation.create(propertyLayout, holder);
        }
        FacetUtil.addFacet(typicalLengthFacet);

    }

    @Override
    public void process(ProcessContributeeMemberContext processMemberContext) {
        final FacetHolder holder = processMemberContext.getFacetHolder();

        Properties properties = processMemberContext.metadataProperties("propertyLayout");
        if(properties == null) {
            // alternate key
            properties = processMemberContext.metadataProperties("layout");
        }


        // cssClass
        CssClassFacet cssClassFacet = CssClassFacetOnPropertyFromLayoutProperties.create(properties, holder);
        FacetUtil.addFacet(cssClassFacet);


        // describedAs
        DescribedAsFacet describedAsFacet = DescribedAsFacetOnPropertyFromLayoutProperties.create(properties, holder);
        FacetUtil.addFacet(describedAsFacet);


        // hidden
        HiddenFacet hiddenFacet = HiddenFacetOnPropertyFromLayoutProperties.create(properties, holder);
        FacetUtil.addFacet(hiddenFacet);


        // labelAt
        LabelAtFacet labelAtFacet = LabelAtFacetOnPropertyFromLayoutProperties.create(properties, holder);
        FacetUtil.addFacet(labelAtFacet);


        // multiLine
        MultiLineFacet multiLineFacet = MultiLineFacetOnPropertyFromLayoutProperties.create(properties, holder);
        FacetUtil.addFacet(multiLineFacet);


        // named
        NamedFacet namedFacet = NamedFacetOnPropertyFromLayoutProperties.create(properties, holder);
        FacetUtil.addFacet(namedFacet);


        // renderedAsDayBefore
        RenderedAdjustedFacet renderedAdjustedFacet = RenderedAdjustedFacetOnPropertyFromLayoutProperties.create(properties, holder);
        FacetUtil.addFacet(renderedAdjustedFacet);


        // typicalLength
        TypicalLengthFacet typicalLengthFacet = TypicalLengthFacetOnPropertyFromLayoutProperties.create(properties, holder);
        FacetUtil.addFacet(typicalLengthFacet);

    }

}
