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
package org.apache.isis.core.metamodel.facets.actions.layout;


import java.util.Properties;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facets.Annotations;
import org.apache.isis.core.metamodel.facets.ContributeeMemberFacetFactory;
import org.apache.isis.core.metamodel.facets.FacetFactoryAbstract;
import org.apache.isis.core.metamodel.facets.actions.position.ActionPositionFacet;
import org.apache.isis.core.metamodel.facets.actions.position.ActionPositionFacetFallback;
import org.apache.isis.core.metamodel.facets.actions.prototype.PrototypeFacet;
import org.apache.isis.core.metamodel.facets.all.describedas.DescribedAsFacet;
import org.apache.isis.core.metamodel.facets.all.hide.HiddenFacet;
import org.apache.isis.core.metamodel.facets.all.named.NamedFacet;
import org.apache.isis.core.metamodel.facets.members.cssclass.CssClassFacet;
import org.apache.isis.core.metamodel.facets.members.cssclassfa.CssClassFaFacet;


public class ActionLayoutFacetFactory extends FacetFactoryAbstract implements ContributeeMemberFacetFactory {

    public ActionLayoutFacetFactory() {
        super(FeatureType.ACTIONS_ONLY);
    }

    @Override
    public void process(final ProcessMethodContext processMethodContext) {

        final FacetHolder holder = processMethodContext.getFacetHolder();

        Properties properties = processMethodContext.metadataProperties("actionLayout");
        if(properties == null) {
            // alternate key
            properties = processMethodContext.metadataProperties("layout");
        }
        final ActionLayout actionLayout = Annotations.getAnnotation(processMethodContext.getMethod(), ActionLayout.class);


        // cssClass
        CssClassFacet cssClassFacet = CssClassFacetOnActionFromLayoutProperties.create(properties, holder);
        if(cssClassFacet == null) {
            cssClassFacet = CssClassFacetForActionLayoutAnnotation.create(actionLayout, holder);
        }
        FacetUtil.addFacet(cssClassFacet);


        // cssClassFa
        CssClassFaFacet cssClassFaFacet = CssClassFaFacetOnActionFromLayoutProperties.create(properties, holder);
        if(cssClassFaFacet == null) {
            cssClassFaFacet = CssClassFaFacetForActionLayoutAnnotation.create(actionLayout, holder);
        }
        FacetUtil.addFacet(cssClassFaFacet);


        // describedAs
        DescribedAsFacet describedAsFacet = DescribedAsFacetOnActionFromLayoutProperties.create(properties, holder);
        if(describedAsFacet == null) {
            describedAsFacet = DescribedAsFacetForActionLayoutAnnotation.create(actionLayout, holder);
        }
        FacetUtil.addFacet(describedAsFacet);


        // hidden
        HiddenFacet hiddenFacet = HiddenFacetOnActionFromLayoutProperties.create(properties, holder);
        if(hiddenFacet == null) {
            hiddenFacet = HiddenFacetForActionLayoutAnnotation.create(actionLayout, holder);
        }
        FacetUtil.addFacet(hiddenFacet);


        // named
        NamedFacet namedFacet = NamedFacetOnActionFromLayoutProperties.create(properties, holder);
        if(namedFacet == null) {
            namedFacet = NamedFacetForActionLayoutAnnotation.create(actionLayout, holder);
        }
        FacetUtil.addFacet(namedFacet);


        // position
        ActionPositionFacet actionPositionFacet = ActionPositionFacetOnActionFromLayoutProperties.create(properties, holder);
        if(actionPositionFacet == null) {
            actionPositionFacet = ActionPositionFacetForActionLayoutAnnotation.create(actionLayout, holder);
        }
        if(actionPositionFacet == null) {
            actionPositionFacet = new ActionPositionFacetFallback(holder);
        }
        FacetUtil.addFacet(actionPositionFacet);


        // prototype
        PrototypeFacet prototypeFacet = PrototypeFacetOnActionFromLayoutProperties.create(properties, holder);
        if(prototypeFacet == null) {
            prototypeFacet = PrototypeFacetForActionLayoutAnnotation.create(actionLayout, holder);
        }
        FacetUtil.addFacet(prototypeFacet);
    }

    @Override
    public void process(ProcessContributeeMemberContext processMemberContext) {
        final FacetHolder holder = processMemberContext.getFacetHolder();

        Properties properties = processMemberContext.metadataProperties("actionLayout");
        if (properties == null) {
            // alternate key
            properties = processMemberContext.metadataProperties("layout");
        }


        // cssClass
        CssClassFacet cssClassFacet = CssClassFacetOnActionFromLayoutProperties.create(properties, holder);
        FacetUtil.addFacet(cssClassFacet);


        // cssClassFa
        CssClassFaFacet cssClassFaFacet = CssClassFaFacetOnActionFromLayoutProperties.create(properties, holder);
        FacetUtil.addFacet(cssClassFaFacet);


        // describedAs
        DescribedAsFacet describedAsFacet = DescribedAsFacetOnActionFromLayoutProperties.create(properties, holder);
        FacetUtil.addFacet(describedAsFacet);


        // hidden
        HiddenFacet hiddenFacet = HiddenFacetOnActionFromLayoutProperties.create(properties, holder);
        FacetUtil.addFacet(hiddenFacet);


        // named
        NamedFacet namedFacet = NamedFacetOnActionFromLayoutProperties.create(properties, holder);
        FacetUtil.addFacet(namedFacet);


        // position
        ActionPositionFacet actionPositionFacet = ActionPositionFacetOnActionFromLayoutProperties.create(properties, holder);
        if(actionPositionFacet == null) {
            actionPositionFacet = new ActionPositionFacetFallback(holder);
        }
        FacetUtil.addFacet(actionPositionFacet);


        // prototype
        PrototypeFacet prototypeFacet = PrototypeFacetOnActionFromLayoutProperties.create(properties, holder);
        FacetUtil.addFacet(prototypeFacet);

    }

}
