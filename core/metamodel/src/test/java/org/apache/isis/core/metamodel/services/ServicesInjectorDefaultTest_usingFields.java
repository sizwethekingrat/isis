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

package org.apache.isis.core.metamodel.services;

import java.util.Arrays;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.core.metamodel.services.container.DomainObjectContainerDefault;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ServicesInjectorDefaultTest_usingFields {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    private DomainObjectContainer container;
    
    private SomeDomainService1 service1;
    private SomeDomainService2 service2;
    private SomeDomainService3 service3;
    
    private ServicesInjectorSpi injector;


    static class SomeDomainService3 { }

    static class SomeDomainService1 {
        @Inject
        private DomainObjectContainer container;
        DomainObjectContainer getContainer() {
            return container;
        }
        @Inject
        private SomeDomainService2Abstract someDomainService2;
        SomeDomainService2Abstract getSomeDomainService2() {
            return someDomainService2;
        }
    }
    static abstract class SomeDomainService2Abstract {
        @Inject
        private SomeDomainService1 someDomainService1;
        SomeDomainService1 getSomeDomainService1() {
            return someDomainService1;
        }
    }
    static class SomeDomainService2 extends SomeDomainService2Abstract {
        @Inject
        private SomeDomainService3 someDomainService3;
        SomeDomainService3 getSomeDomainService3() {
            return someDomainService3;
        }
    }

    @Before
    public void setUp() throws Exception {
        container = new DomainObjectContainerDefault();
        service1 = new SomeDomainService1();
        service3 = new SomeDomainService3();
        service2 = new SomeDomainService2();
        injector = new ServicesInjectorDefault();
    }

    @Test
    public void shouldInjectContainer() {
        injector.setServices(Arrays.asList(container, service1, service3, service2));

        injector.injectServicesInto(service1);
        injector.injectServicesInto(service2);
        injector.injectServicesInto(service3);
        
        assertThat(service1.getSomeDomainService2(), is((SomeDomainService2Abstract)service2));
        
        assertThat(service2.getSomeDomainService1(), is(service1));
        
        assertThat(service2.getSomeDomainService1(), is(service1));
        assertThat(service2.getSomeDomainService3(), is(service3));
    }

}
