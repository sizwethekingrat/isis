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
 * @deprecated - see {@link Action#semantics()}.
 */
@Deprecated
@Inherited
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionSemantics {

    /**
     * @deprecated - see {@link org.apache.isis.applib.annotation.Action.Semantics}
     */
    @Deprecated
    public enum Of {
        /**
         * @deprecated - see {@link org.apache.isis.applib.annotation.Action.Semantics#SAFE}
         */
        @Deprecated
        SAFE, 
        /**
         * @deprecated - see {@link org.apache.isis.applib.annotation.Action.Semantics#IDEMPOTENT}
         */
        @Deprecated
        IDEMPOTENT,
        /**
         * @deprecated - see {@link org.apache.isis.applib.annotation.Action.Semantics#IDEMPOTENT}
         */
        @Deprecated
        NON_IDEMPOTENT;

        /**
         * @deprecated - see {@link org.apache.isis.applib.annotation.Action.Semantics#getFriendlyName()}
         */
        @Deprecated
        public String getFriendlyName() {
            return Action.Semantics.from(this).getFriendlyName();
            //return Enums.getFriendlyNameOf(this);
        }

        /**
         * @deprecated - see {@link org.apache.isis.applib.annotation.Action.Semantics#getCamelCaseName()}
         */
        @Deprecated
        public String getCamelCaseName() {
            return Action.Semantics.from(this).getCamelCaseName();
        }

        /**
         * {@link #SAFE} is idempotent in nature, as well as, obviously, {@link #IDEMPOTENT}.
         *
         * @deprecated - see {@link org.apache.isis.applib.annotation.Action.Semantics#isIdempotentInNature()}
         */
        @Deprecated
        public boolean isIdempotentInNature() {
            return Action.Semantics.from(this).isIdempotentInNature();
        }

        /**
         * @deprecated - see {@link org.apache.isis.applib.annotation.Action.Semantics#isSafe()}.
         */
        @Deprecated
        public boolean isSafe() {
            return Action.Semantics.from(this).isSafe();
        }
    }

    /**
     * @deprecated - use {@link Action#semantics()} instead.
     */
    @Deprecated
    Of value() default Of.NON_IDEMPOTENT;
    
}
