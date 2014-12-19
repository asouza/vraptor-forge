package br.com.caelum.vraptor.forge.addon.commands.plugins.jpa;

import javax.inject.Inject;

import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.javaee.cdi.CDIFacet_1_1;
import org.jboss.forge.addon.javaee.jpa.DatabaseType;
import org.jboss.forge.addon.javaee.jpa.JPAFacet_2_1;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.shrinkwrap.descriptor.api.persistence21.PersistenceDescriptor;
import org.jboss.shrinkwrap.descriptor.api.persistence21.PersistenceUnit;
import org.jboss.shrinkwrap.descriptor.api.persistence21.Properties;

import br.com.caelum.vraptor.forge.addon.commands.VRaptorSetupCommand;
import br.com.caelum.vraptor.forge.addon.javaee.JavaeeMavenDependency;

@FacetConstraint({ CDIFacet_1_1.class, JPAFacet_2_1.class })
public class VRaptorJPACommand extends AbstractProjectCommand {

	@Inject
	@WithAttributes(label = "Support handle transactions manually", required = false, defaultValue = "false")
	private UIInput<Boolean> supportsTransactional;
	@Inject
	@WithAttributes(shortName = 't', label = "Database Type", required = true)
	private UISelectOne<DatabaseType> dbType;
	@Inject
	private JavaeeMavenDependency javaeeDependency;
	@Inject
	private DependencyInstaller dependencyInstaller;
	@Inject
	private FacetFactory facetFactory;
	@Inject
	private JPADatabaseRelation jpaDatabaseRelation;

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(VRaptorSetupCommand.class)
				.name("VRaptor: jpa-plugin")
				.category(Categories.create("VRaptor"));
	}

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
		facetFactory.install(getSelectedProject(builder.getUIContext()),
				JPAFacet_2_1.class);	
		builder.add(dbType);
		builder.add(supportsTransactional);
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		configureDependencies(context);
		if (supportsTransactional.getValue()) {
			installDecorator(context);
		}
		maybeGeneratePersistenceFile(context);

		return Results
				.success("Command 'VRaptor-jpa: Setup' successfully executed!");
	}

	private void maybeGeneratePersistenceFile(UIExecutionContext context) {
		Project project = getSelectedProject(context);
		JPAFacet_2_1 jpaFacet = project.getFacet(JPAFacet_2_1.class);
		if(!jpaFacet.isInstalled()){
			jpaFacet.install();
			
			PersistenceDescriptor config = jpaFacet.getConfig();
			PersistenceUnit<PersistenceDescriptor> persistenceUnit = config.getOrCreatePersistenceUnit();
			persistenceUnit.name("default");
			
			Properties<PersistenceUnit<PersistenceDescriptor>> props = persistenceUnit.getOrCreateProperties();
			props.createProperty().name("javax.persistence.jdbc.url").value("");
			props.createProperty().name("javax.persistence.jdbc.user").value("");
			props.createProperty().name("javax.persistence.jdbc.password").value("");
			VendorConf vendorConf = jpaDatabaseRelation.getVendorConf(dbType.getValue());
			props.createProperty().name("javax.persistence.jdbc.driver").value(vendorConf.getDriverClass());
			props.createProperty().name("hibernate.dialect").value(vendorConf.getDialect());
			props.createProperty().name("hibernate.show_sql").value("true");
			props.createProperty().name("hibernate.format_sql").value("true");		
			props.createProperty().name("hibernate.hbm2ddl.auto").value("update");
			jpaFacet.saveConfig(config);
		}
		
		
	}

	private void installDecorator(UIExecutionContext context) {
		Project project = getSelectedProject(context);
		CDIFacet_1_1 cdiFacet = project.getFacet(CDIFacet_1_1.class);
		BeansDescriptor config = cdiFacet.getConfig();
		config.getOrCreateDecorators()
				.clazz("br.com.caelum.vraptor.jpa.TransactionDecorator");
		cdiFacet.saveConfig(config);
	}

	private void configureDependencies(UIExecutionContext context) {
		Dependency vraptorJPADependency = DependencyBuilder
				.create("br.com.caelum.vraptor:vraptor-jpa:4.0.2");
		Project project = getSelectedProject(context);
		dependencyInstaller.install(project, vraptorJPADependency);
		DependencyBuilder jpaDependency = DependencyBuilder
				.create("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.0.Final");
		javaeeDependency.applyProvided(jpaDependency, false);
		dependencyInstaller.install(project, jpaDependency);
	}

	@Override
	protected boolean isProjectRequired() {
		return true;
	}

	@Inject
	private ProjectFactory projectFactory;

	@Override
	protected ProjectFactory getProjectFactory() {
		return projectFactory;
	}

}
