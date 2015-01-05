package br.com.caelum.vraptor.forge.addon.commands;

import javax.inject.Inject;

import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.javaee.cdi.CDIFacet_1_1;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
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

import br.com.caelum.vraptor.forge.addon.maven.VRaptorDep;
import br.com.caelum.vraptor.forge.addon.maven.VRaptorPlugin;

public class VRaptorSetupCommand extends AbstractProjectCommand {

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(VRaptorSetupCommand.class)
				.name("VRaptor: Setup").category(Categories.create("VRaptor"));
	}

	@Inject
	private DependencyInstaller dependencyInstaller;
	@Inject
	@WithAttributes(label = "JAVAEE environment?", required = false)
	private UIInput<Boolean> javaeeEnv;

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
		builder.add(javaeeEnv);
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		configureDependencies(context);
		addMavenPlugins(context);
		installCDI(context);
		
		return Results
				.success("Command 'VRaptor: Setup' successfully executed!");
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
		for (VRaptorDep dep : VRaptorDep.values()) {
			DependencyBuilder dependency = dep.build();
			if (isJavaeeEnv && dep.isProvided()) {
				dependency.setScopeType("provided");
			}
			dependencyInstaller
					.install(getSelectedProject(context), dependency);
		}
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