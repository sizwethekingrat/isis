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

package org.apache.isis.core.metamodel.facets.collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Iterator;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.adapter.mgr.AdapterManager;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facets.collections.javautilcollection.JavaCollectionFacet;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;

public class JavaCollectionFacetTest {

    private JavaCollectionFacet facet;

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    private FacetHolder mockFacetHolder;
    @Mock
    private ObjectAdapter mockCollection;
    @Mock
    private Collection<ObjectAdapter> mockWrappedCollection;
    @Mock
    private Iterator<ObjectAdapter> mockIterator;
    @Mock
    private AdapterManager mockAdapterManager;

    @Before
    public void setUp() throws Exception {
        mockAdapterManager = context.mock(AdapterManager.class);

        facet = new JavaCollectionFacet(mockFacetHolder, mockAdapterManager);
    }

    @After
    public void tearDown() throws Exception {
        facet = null;
    }

    @Test
    public void firstElementForEmptyCollectionIsNull() {
        context.checking(new Expectations() {
            {
                one(mockCollection).getObject();
                will(returnValue(mockWrappedCollection));

                one(mockWrappedCollection).iterator();
                will(returnValue(mockIterator));

                one(mockIterator).hasNext();
                will(returnValue(false));
            }
        });
        assertThat(facet.firstElement(mockCollection), is(nullValue()));
    }

}
