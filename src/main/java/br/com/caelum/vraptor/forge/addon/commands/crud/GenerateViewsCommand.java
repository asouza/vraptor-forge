package br.com.caelum.vraptor.forge.addon.commands.crud;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.facets.WebResourcesFacet;
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
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import br.com.caelum.formgenerator.DefaultInputInfo;
import br.com.caelum.formgenerator.InputInfo;
import br.com.caelum.formgenerator.templates.FormTemplate;
import br.com.caelum.vraptor.forge.addon.commands.VRaptorControllerCommand;
import freemarker.template.TemplateException;

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
		forgeFormTemplate
				.setBasePath(getSelectedProject(builder.getUIContext()));
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		Project project = getSelectedProject(context);

		JavaClassSource classSource = new JavaClassSourceLoader(project)
				.load(modelClass.getValue().toString());

		List<String> inputs = generateRequiredInputs(classSource);

		generateModelPages(project, classSource, inputs);

		return Results
				.success("Command 'VRaptor: generate-views' successfully executed for "
						+ classSource.getName() + "!");
	}

	private void generateModelPages(Project project,
			final JavaClassSource classSource, List<String> inputs)
			throws IOException, TemplateException {
		String[] modelTemplates = { "form", "updateForm", "list" };

		final ViewCreator viewCreator = new ViewCreator(formTemplate,
				project.getFacet(WebResourcesFacet.class));

		final Map<Object, Object> params = new HashMap<>();
		params.put("inputs", inputs);
		for (String modelTemplate : modelTemplates) {
			BestFolderResolver.resolve(classSource, "/forminputs/model",
					modelTemplate + ".ftl",
					new TemplateResolverFunction<String>() {

						@Override
						public void apply(String input) throws IOException,
								TemplateException {
							viewCreator.create(input, classSource, params);
						}
					});
		}
	}

	private List<String> generateRequiredInputs(JavaClassSource classSource)
			throws ClassNotFoundException, IOException, TemplateException {
		final List<String> inputs = new ArrayList<>();
		for (FieldSource<JavaClassSource> fieldSource : classSource.getFields()) {
			Class<?> type = Class.forName(fieldSource.getType()
					.getQualifiedName());
			final HashMap<Object, Object> params = new HashMap<Object, Object>();
			InputInfo inputInfo = new DefaultInputInfo(type,
					fieldSource.getName());
			params.put("inputInfo", inputInfo);

			BestFolderResolver.resolve(classSource, "/forminputs",
					StringUtils.uncapitalize(type.getSimpleName() + ".ftl"),
					new TemplateResolverFunction<String>() {

						@Override
						public void apply(String ftlPath) throws IOException,
								TemplateException {
							String input = formTemplate
									.process(ftlPath, params);
							inputs.add(input);
						}
					});
		}
		return inputs;
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
