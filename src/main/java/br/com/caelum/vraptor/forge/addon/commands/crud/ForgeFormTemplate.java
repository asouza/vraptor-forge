package br.com.caelum.vraptor.forge.addon.commands.crud;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.templates.Template;
import org.jboss.forge.addon.templates.TemplateFactory;
import org.jboss.forge.addon.templates.freemarker.FreemarkerTemplate;

import br.com.caelum.formgenerator.templates.FormTemplate;
import freemarker.template.TemplateException;

@ApplicationScoped
public class ForgeFormTemplate implements FormTemplate {

	@Inject
	private TemplateFactory templateFactory;
	@Inject
	private ResourceFactory resourceFactory;
	
	private String projectPath;
	
	public void setBasePath(Project project) {
		projectPath = project.getRoot().getFullyQualifiedName();			
	}

	@Override
	public String process(String templatePath, Map<?, ?> params)
			throws IOException, TemplateException {
		File file = new File(projectPath+"/src/main/resources"+templatePath);
		Resource templateResource = resourceFactory.create(file);
		
		if(!templateResource.exists()){
			templateResource = resourceFactory.create(getClass().getResource(templatePath));
					
		}
		Template processor = templateFactory.create(templateResource,
				FreemarkerTemplate.class);
		String output = processor.process(params);
		return output;
	}

}
