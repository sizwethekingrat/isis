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
package org.apache.isis.applib.services.classdiscovery;

import java.util.Set;
import org.apache.isis.applib.annotation.Programmatic;

/**
 * This utility service supports the dynamic discovery of classes from the classpath.  One service that uses this
 * is the <tt>FixtureScripts</tt> domain service.
 *
 * <p>
 * Because an implementation of this service (<tt>ClassDiscoveryServiceUsingReflections</tt> is annotated with
 * {@link org.apache.isis.applib.annotation.DomainService} and is implemented in the core applib, it is automatically
 * registered and available for use; no configuration is required.
 * </p>
 */
public interface ClassDiscoveryService2 extends ClassDiscoveryService {

    @Programmatic
    public <T> Set<Class<? extends T>> findSubTypesOfClasses(Class<T> type, String packagePrefix);

}