package br.com.caelum.vraptor.forge.addon.commands.crud;

import java.util.HashMap;

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

import br.com.caelum.vraptor.forge.addon.commands.VRaptorControllerCommand;
import br.com.caelum.vraptor.forge.addon.generate.JavaTemplateLoader;

public class VRaptorCrudCommand extends AbstractProjectCommand{
	
	@Inject
	@WithAttributes(label = "base package for artifacts", required = true)
	private UIInput<String> basePackage;
	
	@Inject
	@WithAttributes(label = "model name in package models", required = true)
	private UIInput<String> modelName;
	@Inject
	private JavaTemplateLoader javaTemplateLoader;
	
	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(VRaptorControllerCommand.class)
				.name("VRaptor: crud-generator")
				.category(Categories.create("VRaptor"));
	}

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
		builder.add(basePackage);
		builder.add(modelName);
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		HashMap<Object, Object> params = new HashMap<>();
		params.put("basePackage", basePackage.getValue().toString());
		params.put("modelName", modelName.getValue().toString());
		
		JavaClassSource controllerSource = javaTemplateLoader.processTemplate("/CrudController.jv", params);
		JavaClassSource daoSource = javaTemplateLoader.processTemplate("/CrudDao.jv", params);
		
		Project project = getSelectedProject(context);
		
		JavaSourceFacet javaSourceFacet = project.getFacet(JavaSourceFacet.class);
		javaSourceFacet.saveJavaSource(controllerSource);
		javaSourceFacet.saveJavaSource(daoSource);
		
		return Results
				.success("Command 'VRaptor: crud-generator' successfully executed for "+modelName+"!");
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
