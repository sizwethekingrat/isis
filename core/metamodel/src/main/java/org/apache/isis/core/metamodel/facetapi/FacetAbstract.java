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

package org.apache.isis.core.metamodel.facetapi;

import org.apache.isis.core.commons.ensure.Ensure;
import org.apache.isis.core.commons.matchers.IsisMatchers;

import static org.apache.isis.core.commons.ensure.Ensure.ensureThatArg;
import static org.hamcrest.CoreMatchers.*;

public abstract class FacetAbstract implements Facet {

    public enum Derivation {
        DERIVED,
        NOT_DERIVED
    }
    
    private Facet underlyingFacet;

    private final Class<? extends Facet> facetType;
    private final boolean derived;
    private FacetHolder holder;

    /**
     * Populated in {@link #setFacetHolder(FacetHolder)} if the provided holder
     * implements {@link IdentifiedHolder}.
     * 
     * <p>
     * Otherwise is <tt>null</tt>.
     */
    private IdentifiedHolder identifiedHolder;

    @SuppressWarnings("unchecked")
    public FacetAbstract(
            final Class<? extends Facet> facetType,
            final FacetHolder holder,
            final Derivation derivation) {
        this.facetType = ensureThatArg(facetType, is(not(nullValue(Class.class))));
        setFacetHolder(ensureThatArg(holder, is(not(nullValue(FacetHolder.class)))));
        this.derived = (derivation == Derivation.DERIVED);
    }

    @Override
    public final Class<? extends Facet> facetType() {
        return facetType;
    }

    @Override
    public FacetHolder getFacetHolder() {
        return holder;
    }

    @Override
    public boolean isDerived() {
        return derived;
    }

    /**
     * Convenience method that returns {@link #getFacetHolder()} downcast to
     * {@link IdentifiedHolder} if the implementation does indeed inherit from
     * {@link IdentifiedHolder}, otherwise <tt>null</tt>.
     */
    public IdentifiedHolder getIdentified() {
        return identifiedHolder;
    }

    @Override
    public Facet getUnderlyingFacet() {
        return underlyingFacet;
    }

    @Override
    public void setUnderlyingFacet(final Facet underlyingFacet) {
        if(underlyingFacet != null) {
            Ensure.ensureThatArg(underlyingFacet.facetType(), IsisMatchers.classEqualTo(facetType));
        }
        this.underlyingFacet = underlyingFacet;
    }

    /**
     * Assume implementation is <i>not</i> a no-op.
     * 
     * <p>
     * No-op implementations should override and return <tt>true</tt>.
     */
    @Override
    public boolean isNoop() {
        return false;
    }

    /**
     * Default implementation of this method that returns <tt>true</tt>, ie
     * should replace (none {@link #isNoop() no-op} implementations.
     * 
     * <p>
     * Implementations that don't wish to replace none no-op implementations
     * should override and return <tt>false</tt>.
     */
    @Override
    public boolean alwaysReplace() {
        return true;
    }

    @Override
    public void setFacetHolder(final FacetHolder facetHolder) {
        this.holder = facetHolder;
        this.identifiedHolder = holder instanceof IdentifiedHolder ? (IdentifiedHolder) holder : null;
    }

    protected String toStringValues() {
        return "";
    }

    @Override
    public String toString() {
        String details = "";
        if (Validating.class.isAssignableFrom(getClass())) {
            details += "Validating";
        }
        if (Disabling.class.isAssignableFrom(getClass())) {
            details += (details.length() > 0 ? ";" : "") + "Disabling";
        }
        if (Hiding.class.isAssignableFrom(getClass())) {
            details += (details.length() > 0 ? ";" : "") + "Hiding";
        }
        if (!"".equals(details)) {
            details = "interaction=" + details + ",";
        }

        final String className = getClass().getName();
        final String stringValues = toStringValues();
        if (getClass() != facetType()) {
            final String facetType = facetType().getName();
            details += "type=" + facetType.substring(facetType.lastIndexOf('.') + 1);
        }
        if (!"".equals(stringValues)) {
            details += ",";
        }
        return className.substring(className.lastIndexOf('.') + 1) + "[" + details + stringValues + "]";
    }

    /**
     * Marker interface used within {@link #toString()}.
     */
    public static interface Hiding {
    }

    /**
     * Marker interface used within {@link #toString()}.
     */
    public static interface Disabling {
    }

    /**
     * Marker interface used within {@link #toString()}.
     */
    public static interface Validating {
    }

}
