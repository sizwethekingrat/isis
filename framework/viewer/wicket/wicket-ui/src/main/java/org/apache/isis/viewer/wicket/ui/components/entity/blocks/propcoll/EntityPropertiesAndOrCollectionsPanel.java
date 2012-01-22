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

package org.apache.isis.viewer.wicket.ui.components.entity.blocks.propcoll;

import java.util.List;

import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.filter.Filters;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.metamodel.spec.feature.ObjectAssociation;
import org.apache.isis.core.metamodel.spec.feature.ObjectAssociationFilters;
import org.apache.isis.core.metamodel.spec.feature.OneToManyAssociation;
import org.apache.isis.core.metamodel.spec.feature.OneToOneAssociation;
import org.apache.isis.core.progmodel.facets.object.validate.ValidateObjectFacet;
import org.apache.isis.runtimes.dflt.runtime.memento.Memento;
import org.apache.isis.viewer.wicket.model.mementos.PropertyMemento;
import org.apache.isis.viewer.wicket.model.models.EntityCollectionModel;
import org.apache.isis.viewer.wicket.model.models.EntityModel;
import org.apache.isis.viewer.wicket.model.models.ScalarModel;
import org.apache.isis.viewer.wicket.ui.ComponentType;
import org.apache.isis.viewer.wicket.ui.components.collection.CollectionPanel;
import org.apache.isis.viewer.wicket.ui.components.widgets.formcomponent.CancelHintRequired;
import org.apache.isis.viewer.wicket.ui.panels.FormAbstract;
import org.apache.isis.viewer.wicket.ui.panels.PanelAbstract;
import org.apache.isis.viewer.wicket.ui.util.EvenOrOddCssClassAppenderFactory;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IFormVisitorParticipant;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

/**
 * {@link PanelAbstract Panel} representing the properties of an entity, as per
 * the provided {@link EntityModel}.
 */
public class EntityPropertiesAndOrCollectionsPanel extends PanelAbstract<EntityModel> {

    private static final long serialVersionUID = 1L;

    private static final String ID_ENTITY_PROPERTIES_AND_OR_COLLECTIONS = "entityPropertiesAndOrCollections";

    public enum Render {
        PROPERTIES_ONLY {
            @Override
            public Filter<ObjectAssociation> getFilters() {
                return ObjectAssociationFilters.PROPERTIES;
            }
        },
        COLLECTIONS_ONLY {
            @Override
            public Filter<ObjectAssociation> getFilters() {
                return ObjectAssociationFilters.COLLECTIONS;
            }
        },
        PROPERTIES_AND_COLLECTIONS {
            @Override
            public Filter<ObjectAssociation> getFilters() {
                return Filters.or(PROPERTIES_ONLY.getFilters(), COLLECTIONS_ONLY.getFilters());
            }
        };

        public abstract Filter<ObjectAssociation> getFilters();
    }

    private final Render render;
    private PropCollForm form;

    public EntityPropertiesAndOrCollectionsPanel(final String id, final EntityModel entityModel, final Render render) {
        super(id, entityModel);
        this.render = render;
        buildGui();
        form.toViewMode(null);
    }

    private void buildGui() {
        buildEntityPropertiesAndOrCollectionsGui();
        setOutputMarkupId(true); // so can repaint via ajax
    }

    private void buildEntityPropertiesAndOrCollectionsGui() {
        final EntityModel model = getModel();
        final ObjectAdapter adapter = model.getObject();
        if (adapter != null) {
            form = new PropCollForm(ID_ENTITY_PROPERTIES_AND_OR_COLLECTIONS, model, render, this);
            addOrReplace(form);
        } else {
            permanentlyHide(ID_ENTITY_PROPERTIES_AND_OR_COLLECTIONS);
        }
    }

    static class PropCollForm extends FormAbstract<ObjectAdapter> {

        private static final long serialVersionUID = 1L;

        private static final String ID_PROPERTIES_AND_OR_COLLECTIONS = "propertiesAndOrCollections";
        private static final String ID_PROPERTY_OR_COLLECTION = "propertyOrCollection";
        private static final String ID_EDIT_BUTTON = "edit";
        private static final String ID_OK_BUTTON = "ok";
        private static final String ID_CANCEL_BUTTON = "cancel";
        private static final String ID_FEEDBACK = "feedback";

        private final Render render;

        private final Component owningPanel;
        private Button editButton;
        private Button okButton;
        private Button cancelButton;
        private FeedbackPanel feedback;

        public PropCollForm(final String id, final EntityModel entityModel, final Render render, final Component owningPanel) {
            super(id, entityModel);
            this.owningPanel = owningPanel; // for repainting
            this.render = render;

            buildGui();
        }

        private void buildGui() {
            addPropertiesAndOrCollections();
            addButtons();
            addFeedbackGui();

            addValidator();
        }

        private void addPropertiesAndOrCollections() {
            final EntityModel entityModel = (EntityModel) getModel();
            final ObjectAdapter adapter = entityModel.getObject();
            final ObjectSpecification noSpec = adapter.getSpecification();

            final List<ObjectAssociation> associations = visibleAssociations(adapter, noSpec);

            final RepeatingView rv = new RepeatingView(ID_PROPERTIES_AND_OR_COLLECTIONS);
            final EvenOrOddCssClassAppenderFactory eo = new EvenOrOddCssClassAppenderFactory();
            add(rv);

            @SuppressWarnings("unused")
            Component component;
            for (final ObjectAssociation association : associations) {
                final WebMarkupContainer container = new WebMarkupContainer(rv.newChildId());
                rv.add(container);
                container.add(eo.nextClass());
                if (association instanceof OneToOneAssociation) {
                    final OneToOneAssociation otoa = (OneToOneAssociation) association;
                    final PropertyMemento pm = new PropertyMemento(otoa);

                    final ScalarModel scalarModel = entityModel.getPropertyModel(pm);
                    component = getComponentFactoryRegistry().addOrReplaceComponent(container, ID_PROPERTY_OR_COLLECTION, ComponentType.SCALAR_NAME_AND_VALUE, scalarModel);
                } else {
                    final OneToManyAssociation otma = (OneToManyAssociation) association;

                    final EntityCollectionModel entityCollectionModel = EntityCollectionModel.createParented(entityModel, otma);
                    final CollectionPanel collectionPanel = new CollectionPanel(ID_PROPERTY_OR_COLLECTION, entityCollectionModel);
                    container.addOrReplace(collectionPanel);

                    component = getComponentFactoryRegistry().addOrReplaceComponent(container, ID_PROPERTY_OR_COLLECTION, ComponentType.COLLECTION_NAME_AND_CONTENTS, entityCollectionModel);
                }
            }

            // massive hack: an empty property line to get CSS correct...!
            final WebMarkupContainer container = new WebMarkupContainer(rv.newChildId());
            rv.add(container);
            container.add(new Label(ID_PROPERTY_OR_COLLECTION, Model.of(" ")));
            container.add(eo.nextClass());
        }

        @SuppressWarnings("unchecked")
        private List<ObjectAssociation> visibleAssociations(final ObjectAdapter adapter, final ObjectSpecification noSpec) {
            return noSpec.getAssociations(visibleAssociationFilter(adapter));
        }

        private Filter<ObjectAssociation> visibleAssociationFilter(final ObjectAdapter adapter) {
            return Filters.and(render.getFilters(), ObjectAssociationFilters.dynamicallyVisible(getAuthenticationSession(), adapter));
        }

        private void addButtons() {
            editButton = new AjaxButton(ID_EDIT_BUTTON, Model.of("Edit")) {
                private static final long serialVersionUID = 1L;

                @Override
                public void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
                    toEditMode(target);
                }

                @Override
                protected void onError(final AjaxRequestTarget target, final Form<?> form) {
                    toEditMode(target);
                }
            };
            add(editButton);

            okButton = new Button(ID_OK_BUTTON, Model.of("OK")) {
                private static final long serialVersionUID = 1L;

                @Override
                public void onSubmit() {
                    if (!getForm().hasError()) {
                        final ObjectAdapter object = getEntityModel().getObject();
                        final Memento snapshotToRollbackToIfInvalid = new Memento(object);
                        // to perform object-level validation, we must apply the
                        // changes first
                        // contrast this with ActionPanel (for validating action
                        // arguments) where
                        // we do the validation prior to the execution of the
                        // action
                        getEntityModel().apply();
                        final String invalidReasonIfAny = getEntityModel().getReasonInvalidIfAny();
                        if (invalidReasonIfAny != null) {
                            getForm().error(invalidReasonIfAny);
                            snapshotToRollbackToIfInvalid.recreateObject();
                            return;
                        } else {
                            toViewMode(null);
                        }
                    } else {
                        // stay in edit mode
                    }
                }
            };
            add(okButton);

            cancelButton = new AjaxButton(ID_CANCEL_BUTTON, Model.of("Cancel")) {
                private static final long serialVersionUID = 1L;
                {
                    setDefaultFormProcessing(false);
                }

                @Override
                protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
                    Session.get().getFeedbackMessages().clear();
                    getForm().clearInput();
                    getForm().visitFormComponentsPostOrder(new IVisitor() {

                        @Override
                        public Object formComponent(final IFormVisitorParticipant formComponent) {
                            if (formComponent instanceof CancelHintRequired) {
                                final CancelHintRequired cancelHintRequired = (CancelHintRequired) formComponent;
                                cancelHintRequired.onCancel();
                            }
                            return null;
                        }
                    });
                    getEntityModel().resetPropertyModels();
                    toViewMode(target);
                }

                @Override
                protected void onError(final AjaxRequestTarget target, final Form<?> form) {
                    toViewMode(target);
                }
            };
            add(cancelButton);

            editButton.setOutputMarkupPlaceholderTag(true);
            cancelButton.setOutputMarkupPlaceholderTag(true);
        }

        private void requestRepaintPanel(final AjaxRequestTarget target) {
            if (target != null) {
                target.addComponent(owningPanel);
                // TODO: is it necessary to add these too?
                target.addComponent(editButton);
                target.addComponent(okButton);
                target.addComponent(cancelButton);
                target.addComponent(feedback);
            }
        }

        private void addValidator() {
            add(new AbstractFormValidator() {

                private static final long serialVersionUID = 1L;

                @Override
                public FormComponent<?>[] getDependentFormComponents() {
                    return new FormComponent<?>[0];
                }

                @Override
                public void validate(final Form<?> form) {
                    final EntityModel entityModel = (EntityModel) getModel();
                    final ObjectAdapter adapter = entityModel.getObject();
                    final ValidateObjectFacet facet = adapter.getSpecification().getFacet(ValidateObjectFacet.class);
                    if (facet == null) {
                        return;
                    }
                    final String invalidReasonIfAny = facet.invalidReason(adapter);
                    if (invalidReasonIfAny != null) {
                        Session.get().getFeedbackMessages().add(new FeedbackMessage(form, invalidReasonIfAny, FeedbackMessage.ERROR));
                    }
                }
            });
        }

        private EntityModel getEntityModel() {
            return (EntityModel) getModel();
        }

        void toViewMode(final AjaxRequestTarget target) {
            getEntityModel().toViewMode();
            editButton.setVisible(true);
            okButton.setVisible(false);
            cancelButton.setVisible(false);
            requestRepaintPanel(target);
        }

        private void toEditMode(final AjaxRequestTarget target) {
            getEntityModel().toEditMode();
            editButton.setVisible(false);
            okButton.setVisible(true);
            cancelButton.setVisible(true);
            requestRepaintPanel(target);
        }

        @Override
        protected void onValidate() {
            Session.get().getFeedbackMessages().clear(new IFeedbackMessageFilter() {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean accept(final FeedbackMessage message) {
                    return message.getReporter() == owningPanel;
                }
            });
            super.onValidate();
        }

        private void addFeedbackGui() {
            final FeedbackPanel feedback = addOrReplaceFeedback();

            final ObjectAdapter adapter = getEntityModel().getObject();
            if (adapter == null) {
                feedback.error("cannot locate object:" + getEntityModel().getObjectAdapterMemento().toString());
            }
        }

        private FeedbackPanel addOrReplaceFeedback() {
            feedback = new ComponentFeedbackPanel(ID_FEEDBACK, this);
            feedback.setOutputMarkupPlaceholderTag(true);
            addOrReplace(feedback);
            return feedback;
        }
    }

}