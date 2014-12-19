package br.com.caelum.vraptor.forge.addon.javaee;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;

@ApplicationScoped
public class JavaeeMavenDependency {

	public DependencyBuilder applyProvided(DependencyBuilder dependency,
			boolean javaeeEnv) {
		if (javaeeEnv) {
			dependency.setScopeType("provided");
		}
		return dependency;
	}
}
