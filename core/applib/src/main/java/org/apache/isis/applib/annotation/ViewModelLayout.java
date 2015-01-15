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

package org.apache.isis.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Layout hints for view models.
 *
 * <p>
 *     This is intended for use with UI/application-layer view models that are specified as such through either
 *     the {@link org.apache.isis.applib.annotation.ViewModel} annotation or by implementing the
 *     {@link org.apache.isis.applib.ViewModel} interface.
 * </p>
 *
 * <p>
 *      Note that the attributes are the same as {@link org.apache.isis.applib.annotation.DomainObjectLayout}; it
 *      captures the same information.  This annotation is provided for symmetry; most view models will be annotated
 *      with just <tt>@ViewModel</tt> and <tt>@ViewModelLayout</tt>.
 * </p>
 */
@Inherited
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewModelLayout {


    /**
     * Indicates the css class that a domain class (type) should have.
     */
    String cssClass() default "";


    // //////////////////////////////////////

    /**
     * Description of this class, eg to be rendered in a tooltip.
     */
    String describedAs() default "";


    // //////////////////////////////////////

    /**
     * Name of this class (overriding the name derived from its name in code).
     */
    String named() default "";


    // //////////////////////////////////////

    /**
     * The page size for instances of this class when rendered within
     * a table.
     *
     * <p>
     * If annotated on a collection, then the page size refers to
     * parented collections (eg <tt>Order#lineItems</tt>).
     *
     * <p>
     * If annotated on a type, then the page size refers to standalone
     * collections (eg as returned from a repository query).
     */
    public int paged() default -1;


    // //////////////////////////////////////

    /**
     * The plural name of the class.
     */
    String plural() default "";

    // //////////////////////////////////////

    /**
     * Whether (and how) this domain object can be bookmarked in the UI.
     */
    BookmarkPolicy bookmarking() default BookmarkPolicy.NEVER;

}