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


package org.apache.isis.tck.fixture.simples;

import org.apache.isis.applib.fixtures.AbstractFixture;
import org.apache.isis.tck.dom.scalars.WrapperValuedEntity;
import org.apache.isis.tck.dom.scalars.WrapperValuedEntityRepository;
import org.apache.isis.tck.dom.simples.SimpleEntity;
import org.apache.isis.tck.dom.simples.SimpleEntityRepository;



public class SimpleEntityFixture extends AbstractFixture {

    @Override
    public void install() {
        createEntity("John", true);
        createEntity("Mary", false);
        createEntity("Bill", false);
        createEntity("Sally", true);
        createEntity("Diedre", true);
    }
    
    private SimpleEntity createEntity(String name, Boolean flag) {
        return simpleEntityRepository.newPersistentEntity(name, flag);
    }


    // {{ injected: SimpleEntityRepository
    private SimpleEntityRepository simpleEntityRepository;

    public void setSimpleEntityRepository(final SimpleEntityRepository simpleEntityRepository) {
        this.simpleEntityRepository = simpleEntityRepository;
    }
    // }}


    
}