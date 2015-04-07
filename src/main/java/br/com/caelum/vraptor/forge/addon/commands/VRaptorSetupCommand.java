package br.com.caelum.vraptor.forge.addon.commands;

import javax.inject.Inject;

import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.javaee.cdi.CDIFacet_1_1;
import org.jboss.forge.addon.javaee.servlet.ServletFacet;
import org.jboss.forge.addon.javaee.servlet.ServletFacet_3_0;
import org.jboss.forge.addon.javaee.validation.ValidationFacet;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.shrinkwrap.descriptor.api.validationConfiguration11.ExecutableValidationType;
import org.jboss.shrinkwrap.descriptor.api.validationConfiguration11.ValidationConfigurationDescriptor;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;

import br.com.caelum.vraptor.forge.addon.PluginInstaller;
import br.com.caelum.vraptor.forge.addon.maven.VRaptorDep;
import br.com.caelum.vraptor.forge.addon.maven.VRaptorPlugin;

public class VRaptorSetupCommand extends AbstractProjectCommand {

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(VRaptorSetupCommand.class)
				.name("VRaptor: Setup").category(Categories.create("VRaptor"));
	}


	@Inject
	private FacetFactory facetFactory;
	@Inject
	@WithAttributes(label = "JAVAEE environment?", required = false)
	private UIInput<Boolean> javaeeEnv;
	@Inject
	private PluginInstaller pluginInstaller;

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
		builder.add(javaeeEnv);
		Project selectedProject = getSelectedProject(builder.getUIContext());
		facetFactory.install(selectedProject, ServletFacet_3_0.class);
		facetFactory.install(selectedProject, CDIFacet_1_1.class);
		facetFactory.install(selectedProject, ValidationFacet.class);
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		installCDI(context);
		installValidation(context);
		installWebxml(context);
		configureDependencies(context);
		addMavenPlugins(context);

		return Results
				.success("Command 'VRaptor: Setup' successfully executed!");
	}

	private void installWebxml(UIExecutionContext context) {
		if (!javaeeEnv.getValue()) {
			Project project = getSelectedProject(context);
			ServletFacet<WebAppDescriptor> facet = project
					.getFacet(ServletFacet_3_0.class);
			facet.install();

			WebAppDescriptor webxml = facet.getConfig();
			webxml.createListener().listenerClass(
					"org.jboss.weld.environment.servlet.Listener");
			facet.saveConfig(webxml);
		}
	}

	private void installValidation(UIExecutionContext context) {
		Project project = getSelectedProject(context);
		ValidationFacet facet = project.getFacet(ValidationFacet.class);
		facet.install();
		ValidationConfigurationDescriptor config = facet.getConfig().version(
				"1.1");
		ExecutableValidationType<ValidationConfigurationDescriptor> executableValidation = config
				.getOrCreateExecutableValidation();
		executableValidation.enabled(false);
		facet.saveConfig(config);
	}

	private void installCDI(UIExecutionContext context) {
		Project project = getSelectedProject(context);
		CDIFacet_1_1 cdiFacet = project.getFacet(CDIFacet_1_1.class);
		cdiFacet.install();
	}

	private void addMavenPlugins(UIExecutionContext context) {
		Project project = getSelectedProject(context);
		MavenPluginFacet pluginFacet = project.getFacet(MavenPluginFacet.class);
		for (VRaptorPlugin plugin : VRaptorPlugin.values()) {
			pluginFacet.addPlugin(plugin.build());
		}

	}

	private void configureDependencies(UIExecutionContext context) {
		Boolean isJavaeeEnv = javaeeEnv.getValue();
		Project selectedProject = getSelectedProject(context);
		for (VRaptorDep vraptorDep : VRaptorDep.values()) {
			DependencyBuilder mavenDependency = vraptorDep.build();
			if (vraptorDep.isProvided(isJavaeeEnv)) {
				mavenDependency.setScopeType("provided");
			}

			pluginInstaller.forceInstall(selectedProject,mavenDependency);		}
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