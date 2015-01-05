package br.com.caelum.vraptor.forge.addon.commands.plugins.mailer;

import java.io.IOException;
import java.util.Properties;

import javax.inject.Inject;

import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
import org.jboss.forge.addon.projects.facets.ResourcesFacet;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

import br.com.caelum.vraptor.forge.addon.commands.VRaptorSetupCommand;

public class VRaptorMailerCommand extends AbstractProjectCommand {
	
	@Inject
	private DependencyInstaller dependencyInstaller;

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {

	}
	
	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(VRaptorSetupCommand.class)
				.name("VRaptor: mailer")
				.category(Categories.create("VRaptor"));
	}	

 	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		writeEntriesInProperties(context);
		configureDependencies(context);
		return Results
				.success("Command 'VRaptor-mailer: Setup' successfully executed!");
	}

	private void configureDependencies(UIExecutionContext context) {
		Dependency vraptorJPADependency = DependencyBuilder
				.create("br.com.caelum.vraptor:vraptor-simplemail:4.1.0-RC2");
		Project project = getSelectedProject(context);
		dependencyInstaller.install(project, vraptorJPADependency);		
	}

	private void writeEntriesInProperties(UIExecutionContext context) throws IOException {
		ResourcesFacet resources = getSelectedProject(context).getFacet(
				ResourcesFacet.class);
		FileResource<?> resource = (FileResource<?>) resources
				.getResourceDirectory().getChild("development.properties");
		if(!resource.exists()){
			resource.createNewFile();			
		}
		Properties properties = new Properties();
		properties.load(resource.getResourceInputStream());
		properties.put("vraptor.simplemail.main.server","localhost");
		properties.put("vraptor.simplemail.main.port","25");
		properties.put("vraptor.simplemail.main.tls","false");
		properties.put("vraptor.simplemail.main.from","no-reply@myapp.com");
		properties.store(resource.getResourceOutputStream(),"simplemail");
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
