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

package org.apache.isis.progmodel.wrapper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.events.PropertyModifyEvent;
import org.apache.isis.applib.events.PropertyUsabilityEvent;
import org.apache.isis.applib.events.PropertyVisibilityEvent;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.services.wrapper.DisabledException;
import org.apache.isis.core.commons.authentication.AuthenticationSessionProvider;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.adapter.ObjectPersistor;
import org.apache.isis.core.metamodel.adapter.mgr.AdapterManager;
import org.apache.isis.core.metamodel.consent.Allow;
import org.apache.isis.core.metamodel.consent.Consent;
import org.apache.isis.core.metamodel.consent.InteractionResult;
import org.apache.isis.core.metamodel.consent.Veto;
import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facets.members.disabled.DisabledFacet;
import org.apache.isis.core.metamodel.facets.members.disabled.DisabledFacetAbstractAlwaysEverywhere;
import org.apache.isis.core.metamodel.facets.properties.accessor.PropertyAccessorFacetViaAccessor;
import org.apache.isis.core.metamodel.facets.properties.update.modify.PropertySetterFacetViaSetterMethod;
import org.apache.isis.core.metamodel.spec.SpecificationLoader;
import org.apache.isis.core.metamodel.spec.feature.OneToOneAssociation;
import org.apache.isis.core.metamodel.specloader.specimpl.dflt.ObjectSpecificationDefault;
import org.apache.isis.core.runtime.authentication.standard.SimpleSession;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;
import org.apache.isis.core.wrapper.WrapperFactoryAbstract;
import org.apache.isis.progmodel.wrapper.dom.employees.Employee;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Contract test.
 */
public abstract class WrapperFactoryAbstractTest_wrappedObject_transient {

    @Rule
    public final JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    private AdapterManager mockAdapterManager;
    @Mock
    private AuthenticationSessionProvider mockAuthenticationSessionProvider;
    @Mock
    private ObjectPersistor mockObjectPersistor;
    @Mock
    private SpecificationLoader mockSpecificationLookup;

    private Employee employeeDO;
    @Mock
    private ObjectAdapter mockEmployeeAdapter;
    @Mock
    private ObjectSpecificationDefault mockEmployeeSpec;
    @Mock
    private OneToOneAssociation mockPasswordMember;
    @Mock
    private Identifier mockPasswordIdentifier;

    @Mock
    protected ObjectAdapter mockPasswordAdapter;
    
    private final String passwordValue = "12345678";

    private final SimpleSession session = new SimpleSession("tester", Collections.<String>emptyList());

    private List<Facet> facets;
    private Method getPasswordMethod;
    private Method setPasswordMethod;

    private WrapperFactoryAbstract wrapperFactory;
    private Employee employeeWO;


    @Before
    public void setUp() throws Exception {

        employeeDO = new Employee();
        employeeDO.setName("Smith");
        
        getPasswordMethod = Employee.class.getMethod("getPassword");
        setPasswordMethod = Employee.class.getMethod("setPassword", String.class);

        wrapperFactory = createWrapperFactory();
        wrapperFactory.setAdapterManager(mockAdapterManager);
        wrapperFactory.setAuthenticationSessionProvider(mockAuthenticationSessionProvider);
        wrapperFactory.setObjectPersistor(mockObjectPersistor);
        wrapperFactory.setSpecificationLookup(mockSpecificationLookup);
        
        context.checking(new Expectations() {
            {
                allowing(mockAdapterManager).getAdapterFor(employeeDO);
                will(returnValue(mockEmployeeAdapter));

                allowing(mockAdapterManager).adapterFor(passwordValue);
                will(returnValue(mockPasswordAdapter));

                allowing(mockEmployeeAdapter).getSpecification();
                will(returnValue(mockEmployeeSpec));

                allowing(mockEmployeeAdapter).getObject();
                will(returnValue(employeeDO));

                allowing(mockPasswordAdapter).getObject();
                will(returnValue(passwordValue));

                allowing(mockPasswordMember).getIdentifier();
                will(returnValue(mockPasswordIdentifier));

                allowing(mockSpecificationLookup).loadSpecification(Employee.class);
                will(returnValue(mockEmployeeSpec));
                
                allowing(mockEmployeeSpec).getMember(with(setPasswordMethod));
                will(returnValue(mockPasswordMember));

                allowing(mockEmployeeSpec).getMember(with(getPasswordMethod));
                will(returnValue(mockPasswordMember));

                allowing(mockPasswordMember).getName();
                will(returnValue("password"));

                allowing(mockAuthenticationSessionProvider).getAuthenticationSession();
                will(returnValue(session));
                
                allowing(mockPasswordMember).isOneToOneAssociation();
                will(returnValue(true));

                allowing(mockPasswordMember).isOneToManyAssociation();
                will(returnValue(false));
            }
        });

        employeeWO = wrapperFactory.wrap(employeeDO);
    }

    /**
     * Mandatory hook.
     */
    protected abstract WrapperFactoryAbstract createWrapperFactory();

    @Test(expected = DisabledException.class)
    public void shouldNotBeAbleToModifyProperty() {

        // given
        final DisabledFacet disabledFacet = new DisabledFacetAbstractAlwaysEverywhere(mockPasswordMember){};
        facets = Arrays.asList(disabledFacet, new PropertySetterFacetViaSetterMethod(setPasswordMethod, mockPasswordMember));

        final Consent visibilityConsent = new Allow(new InteractionResult(new PropertyVisibilityEvent(employeeDO, null)));

        final InteractionResult usabilityInteractionResult = new InteractionResult(new PropertyUsabilityEvent(employeeDO, null));
        usabilityInteractionResult.advise("disabled", disabledFacet);
        final Consent usabilityConsent = new Veto(usabilityInteractionResult);

        context.checking(new Expectations() {
            {
                allowing(mockPasswordMember).getFacets(with(any(Filter.class)));
                will(returnValue(facets));
                
                allowing(mockPasswordMember).isVisible(session, mockEmployeeAdapter, Where.ANYWHERE);
                will(returnValue(visibilityConsent));
                
                allowing(mockPasswordMember).isUsable(session, mockEmployeeAdapter, Where.ANYWHERE);
                will(returnValue(usabilityConsent));
            }
        });
        
        // when
        employeeWO.setPassword(passwordValue);
        
        // then should throw exception
    }

    @Ignore("TODO - reinstate or replace with integration tests")
    @Test
    public void canModifyProperty() {
        // given

        final Consent visibilityConsent = new Allow(new InteractionResult(new PropertyVisibilityEvent(employeeDO, mockPasswordIdentifier)));
        final Consent usabilityConsent = new Allow(new InteractionResult(new PropertyUsabilityEvent(employeeDO, mockPasswordIdentifier)));
        final Consent validityConsent = new Allow(new InteractionResult(new PropertyModifyEvent(employeeDO, mockPasswordIdentifier, passwordValue)));

        context.checking(new Expectations() {
            {
                allowing(mockPasswordMember).isVisible(session, mockEmployeeAdapter, Where.ANYWHERE);
                will(returnValue(visibilityConsent));
                
                allowing(mockPasswordMember).isUsable(session, mockEmployeeAdapter, Where.ANYWHERE);
                will(returnValue(usabilityConsent));
                
                allowing(mockPasswordMember).isAssociationValid(mockEmployeeAdapter, mockPasswordAdapter);
                will(returnValue(validityConsent));
            }
        });

        facets = Arrays.asList((Facet)new PropertySetterFacetViaSetterMethod(setPasswordMethod, mockPasswordMember));
        context.checking(new Expectations() {
            {
                allowing(mockPasswordMember).getFacets(with(any(Filter.class)));
                will(returnValue(facets));

                oneOf(mockPasswordMember).set(mockEmployeeAdapter, mockPasswordAdapter);
            }
        });

        // when
        employeeWO.setPassword(passwordValue);


        // and given
        facets = Arrays.asList((Facet)new PropertyAccessorFacetViaAccessor(getPasswordMethod, mockPasswordMember));
        context.checking(new Expectations() {
            {
                allowing(mockPasswordMember).getFacets(with(any(Filter.class)));
                will(returnValue(facets));
                
                oneOf(mockPasswordMember).get(mockEmployeeAdapter);
                will(returnValue(mockPasswordAdapter));
            }
        });

        // then be allowed
        assertThat(employeeWO.getPassword(), is(passwordValue));
    }
}
