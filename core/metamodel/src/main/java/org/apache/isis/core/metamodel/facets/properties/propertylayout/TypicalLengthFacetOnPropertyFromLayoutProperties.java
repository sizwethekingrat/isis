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

package org.apache.isis.core.metamodel.facets.properties.propertylayout;

import java.util.Properties;
import com.google.common.base.Strings;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facets.objpropparam.typicallen.TypicalLengthFacet;
import org.apache.isis.core.metamodel.facets.objpropparam.typicallen.TypicalLengthFacetAbstract;

public class TypicalLengthFacetOnPropertyFromLayoutProperties extends TypicalLengthFacetAbstract {

    private final int typicalLength;

    public static TypicalLengthFacet create(Properties properties, FacetHolder holder) {
        final int typicalLength = typicalLength(properties);
        return typicalLength != -1? new TypicalLengthFacetOnPropertyFromLayoutProperties(typicalLength, holder): null;
    }

    private TypicalLengthFacetOnPropertyFromLayoutProperties(int typicalLength, FacetHolder holder) {
        super(holder, Derivation.NOT_DERIVED);
        this.typicalLength = typicalLength;
    }

    private static int typicalLength(Properties properties) {
        if(properties == null) {
            return -1;
        }
        String typicalLength = Strings.emptyToNull(properties.getProperty("typicalLength"));
        if(typicalLength == null) {
            return -1;
        }
        return Integer.parseInt(typicalLength);
    }

    @Override
    public int value() {
        return typicalLength;
    }
}
