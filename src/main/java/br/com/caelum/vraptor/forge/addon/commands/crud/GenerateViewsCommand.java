package br.com.caelum.vraptor.forge.addon.commands.crud;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.parser.java.resources.JavaResource;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.facets.WebResourcesFacet;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFacet;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import br.com.caelum.formgenerator.DefaultInputInfo;
import br.com.caelum.formgenerator.Generator;
import br.com.caelum.formgenerator.templates.FormTemplate;
import br.com.caelum.vraptor.forge.addon.commands.VRaptorControllerCommand;

public class GenerateViewsCommand extends AbstractProjectCommand {

	@Inject
	private ProjectFactory projectFactory;

	@Inject
	@WithAttributes(label = "complete name of the model class", required = true)
	private UIInput<String> modelClass;

	@Inject
	private ForgeFormTemplate forgeFormTemplate;
	
	@Inject
	private FormTemplate formTemplate;

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(VRaptorControllerCommand.class)
				.name("VRaptor: generate-views")
				.category(Categories.create("VRaptor"));
	}

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
		builder.add(modelClass);
		forgeFormTemplate.setBasePath(getSelectedProject(builder.getUIContext()));
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		Project project = getSelectedProject(context);
		JavaSourceFacet facet = project.getFacet(
				JavaSourceFacet.class);
		JavaResource javaResource = facet.getJavaResource(modelClass.getValue()
				.toString());
		
		JavaClassSource classSource = Roaster.parse(JavaClassSource.class,
				javaResource.getJavaType().toString());

		List<FieldSource<JavaClassSource>> fields = classSource.getFields();
		Generator generator = new Generator(forgeFormTemplate);
		List<String> inputs = new ArrayList<>();
		
		for (FieldSource<JavaClassSource> fieldSource : fields) {
			Class<?> type = Class.forName(fieldSource.getType().getQualifiedName());
			String input = generator.generate(new DefaultInputInfo(type,
					fieldSource.getName()), new HashMap<Object, Object>(),
					"/forminputs");
			inputs.add(input);
			
		}
		
		String[] modelFormsTemplate = {"form","updateForm"};
		String[] modelListTemplate = {"list"};
		
		Map<Object,Object> params = new HashMap<>();
		params.put("inputs", inputs);
		String uncapitalizeClassName = StringUtils.uncapitalize(classSource.getName());
		
		WebResourcesFacet webFacet = project.getFacet(WebResourcesFacet.class);
		
		for (String modelForm : modelFormsTemplate) {	
			String jsp = formTemplate.process("/forminputs/"+modelForm+".ftl", params);
			FileResource<?> webResource = webFacet.getWebResource("WEB-INF/jsp/"+uncapitalizeClassName+"/"+modelForm+".jsp");
			webResource.setContents(jsp).createNewFile();			
		}
		
		return null;
	}

	@Override
	protected boolean isProjectRequired() {
		return true;
	}

	@Override
	protected ProjectFactory getProjectFactory() {
		return projectFactory;
	}

}
