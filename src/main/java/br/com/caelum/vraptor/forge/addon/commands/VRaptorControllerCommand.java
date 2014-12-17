package br.com.caelum.vraptor.forge.addon.commands;

import javax.inject.Inject;

import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
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
import org.jboss.forge.roaster.model.source.JavaClassSource;

import br.com.caelum.vraptor.forge.addon.generate.JavaTemplateLoader;


public class VRaptorControllerCommand extends AbstractProjectCommand {


	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(VRaptorControllerCommand.class)
				.name("VRaptor: Controller")
				.category(Categories.create("VRaptor"));
	}
	
	@Inject
	@WithAttributes(label = "Controller package", required = false, defaultValue="controllers")
	private UIInput<String> packageName;
	@Inject
	@WithAttributes(label = "Controller class", required = true)
	private UIInput<String> className;
	@Inject
	private JavaTemplateLoader javaTemplateLoader;

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
		builder.add(packageName);
		builder.add(className);
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		Project project = getSelectedProject(context);
		JavaClassSource source = javaTemplateLoader.load(project,"/Controller.jv");
		
		JavaSourceFacet javaSourceFacet = project.getFacet(JavaSourceFacet.class);
		source.setName(className.getValue().toString());
		source.setPackage(packageName.getValue().toString());
		
		javaSourceFacet.saveJavaSource(source);
		return Results
				.success("Command 'VRaptor: Controller' successfully executed!");
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