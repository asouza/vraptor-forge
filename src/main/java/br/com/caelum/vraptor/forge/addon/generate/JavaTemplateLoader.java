package br.com.caelum.vraptor.forge.addon.generate;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.templates.Template;
import org.jboss.forge.addon.templates.TemplateFactory;
import org.jboss.forge.addon.templates.freemarker.FreemarkerTemplate;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

@ApplicationScoped
public class JavaTemplateLoader {
	
	@Inject
	private TemplateFactory templateFactory;
	@Inject
	private ResourceFactory resourceFactory;

	public JavaClassSource load(Project project,String templatePath){
		return Roaster.parse(JavaClassSource.class, getClass().getResourceAsStream(templatePath));
	}
	
	public JavaClassSource processTemplate(String path,Map<?, ?> params) throws IOException{
		  Resource<URL> templateResource = resourceFactory.create(getClass().getResource(path));
	      Template processor = templateFactory.create(templateResource, FreemarkerTemplate.class);
	      String output = processor.process(params);
	      JavaClassSource resource = Roaster.parse(JavaClassSource.class, output);
	      return resource;
	}
	
}
