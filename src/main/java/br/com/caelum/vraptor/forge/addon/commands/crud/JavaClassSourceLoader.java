package br.com.caelum.vraptor.forge.addon.commands.crud;

import java.io.FileNotFoundException;

import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.parser.java.resources.JavaResource;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

public class JavaClassSourceLoader {

	private JavaSourceFacet javaFacet;

	public JavaClassSourceLoader(Project project) {
		javaFacet = project.getFacet(JavaSourceFacet.class);

	}

	public JavaClassSource load(String fqn) throws FileNotFoundException {
		JavaResource javaResource = javaFacet.getJavaResource(fqn);
		JavaClassSource classSource = Roaster.parse(JavaClassSource.class,
				javaResource.getJavaType().toString());
		return classSource;
	}

}
