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
package org.apache.isis.core.metamodel.facets.actions.action;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.Command.ExecuteIn;
import org.apache.isis.applib.annotation.Command.Persistence;
import org.apache.isis.applib.annotation.CommandExecuteIn;
import org.apache.isis.applib.annotation.CommandPersistence;
import org.apache.isis.applib.annotation.CommandReification;
import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facets.actions.command.CommandFacet;
import org.apache.isis.core.metamodel.facets.actions.command.CommandFacetAbstract;
import org.apache.isis.core.metamodel.facets.actions.command.configuration.ActionConfiguration;
import org.apache.isis.core.metamodel.facets.actions.semantics.ActionSemanticsFacet;

public class CommandFacetForActionAnnotation extends CommandFacetAbstract {

    static CommandFacet create(
            final Action action,
            final IsisConfiguration configuration,
            final FacetHolder holder) {

        if (action == null) {
            return null;
        }

        final CommandReification command = action.command();
        if(command == null) {
            return null;
        }

        final CommandPersistence commandPersistence = action.commandPersistence();
        final Persistence persistence = CommandPersistence.from(commandPersistence);

        final CommandExecuteIn commandExecuteIn = action.commandExecuteIn();
        final ExecuteIn executeIn = CommandExecuteIn.from(commandExecuteIn);

        switch (command) {
            case AS_CONFIGURED:
                final ActionSemanticsFacet actionSemanticsFacet = holder.getFacet(ActionSemanticsFacet.class);
                if(actionSemanticsFacet == null) {
                    throw new IllegalStateException("Require ActionSemanticsFacet in order to process");
                }
                if(holder.containsDoOpFacet(CommandFacet.class)) {
                    // do not replace
                    return null;
                }
                final ActionConfiguration setting = ActionConfiguration.parse(configuration);
                switch (setting) {
                    case NONE:
                        return null;
                    case IGNORE_SAFE:
                        if(actionSemanticsFacet.value() == ActionSemantics.Of.SAFE) {
                            return  null;
                        }
                        // else fall through
                    default:
                        return new CommandFacetForActionAnnotation(persistence, executeIn, Enablement.ENABLED, holder);
                }
            case DISABLED:
                return null;
            case ENABLED:
                return new CommandFacetForActionAnnotation(persistence, executeIn, Enablement.ENABLED, holder);
        }

        return null;
    }


    private CommandFacetForActionAnnotation(
            final Persistence persistence,
            final ExecuteIn executeIn,
            final Enablement enablement,
            final FacetHolder holder) {
        super(persistence, executeIn, enablement, holder);
    }


}
