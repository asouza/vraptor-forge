package br.com.caelum.vraptor.forge.addon.generate;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

@ApplicationScoped
public class JavaTemplateLoader {

	public JavaClassSource load(Project project,String templatePath){
		return Roaster.parse(JavaClassSource.class, getClass().getResourceAsStream(templatePath));
	}
}
