/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.isis.core.metamodel.facets;

import java.util.Set;
import org.junit.Test;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.services.eventbus.AbstractDomainEvent;
import org.apache.isis.applib.services.eventbus.CollectionAddedToEvent;
import org.apache.isis.applib.services.eventbus.CollectionDomainEvent;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InteractionHelper_newCollectionInteractionEvent_forAdd {

    public static class SomeDomainObject {
        public Set<SomeReferencedObject> getReferences() { return null; }
    }
    public static class SomeReferencedObject {}
    
    public static class SomeDomainObjectCollectionEvent extends CollectionDomainEvent<SomeDomainObject, SomeReferencedObject> {
        private static final long serialVersionUID = 1L;
        public SomeDomainObjectCollectionEvent(SomeDomainObject source, Identifier identifier, Of of, SomeReferencedObject value) {
            super(source, identifier, of, value);
        }
    }
    
    @Test
    public void defaultEventType() throws Exception {
        final SomeDomainObject sdo = new SomeDomainObject();
        final SomeReferencedObject other = new SomeReferencedObject();
        final Identifier identifier = Identifier.propertyOrCollectionIdentifier(SomeDomainObject.class, "references");

        final CollectionDomainEvent<Object, Object> ev = new InteractionHelper(null).newCollectionDomainEvent(
                CollectionDomainEvent.Default.class, null, identifier, sdo, CollectionDomainEvent.Of.ADD_TO, other);
        assertThat(ev.getSource(), is((Object)sdo));
        assertThat(ev.getIdentifier(), is(identifier));
        assertThat(ev.getOf(), is(CollectionDomainEvent.Of.ADD_TO));
        assertThat(ev.getValue(), is((Object)other));
    }

    @Test
    public void collectionAddedToDefaultEventType() throws Exception {
        final SomeDomainObject sdo = new SomeDomainObject();
        final SomeReferencedObject other = new SomeReferencedObject();
        final Identifier identifier = Identifier.propertyOrCollectionIdentifier(SomeDomainObject.class, "references");

        final CollectionDomainEvent<Object, Object> ev = new InteractionHelper(null).newCollectionDomainEvent(
                CollectionAddedToEvent.Default.class, AbstractDomainEvent.Phase.EXECUTED, identifier, sdo, CollectionDomainEvent.Of.ADD_TO, other);
        assertThat(ev.getSource(), is((Object)sdo));
        assertThat(ev.getIdentifier(), is(identifier));
        assertThat(ev.getOf(), is(CollectionDomainEvent.Of.ADD_TO));
        assertThat(ev.getValue(), is((Object)other));
    }

    @Test
    public void customEventType() throws Exception {
        final SomeDomainObject sdo = new SomeDomainObject();
        final SomeReferencedObject other = new SomeReferencedObject();
        final Identifier identifier = Identifier.propertyOrCollectionIdentifier(SomeDomainObject.class, "references");
        
        final CollectionDomainEvent<SomeDomainObject, SomeReferencedObject> ev = new InteractionHelper(null).newCollectionDomainEvent(
                SomeDomainObjectCollectionEvent.class, AbstractDomainEvent.Phase.EXECUTED, identifier, sdo, CollectionDomainEvent.Of.ADD_TO, other);
        assertThat(ev.getSource(), is(sdo));
        assertThat(ev.getIdentifier(), is(identifier));
        assertThat(ev.getOf(), is(CollectionDomainEvent.Of.ADD_TO));
        assertThat(ev.getValue(), is(other));
    }
    
}
