package br.com.caelum.vraptor.forge.addon;

import javax.inject.Inject;

import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
import org.jboss.forge.addon.projects.facets.DependencyFacet;

public class PluginInstaller {

	@Inject
	private DependencyInstaller dependencyInstaller;

	public void forceInstall(Project selectedProject,
			DependencyBuilder mavenDependency) {

		DependencyFacet dependencyFacet = selectedProject
				.getFacet(DependencyFacet.class);
		
		dependencyInstaller.installManaged(selectedProject, mavenDependency);					

		if (dependencyInstaller.isInstalled(selectedProject, mavenDependency)) {
			dependencyFacet.removeDependency(mavenDependency);
			dependencyFacet.addDirectDependency(mavenDependency);
		} else {
			dependencyInstaller.install(selectedProject, mavenDependency);
		}
	}

}
