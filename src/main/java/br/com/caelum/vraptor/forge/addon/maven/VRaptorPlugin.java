package br.com.caelum.vraptor.forge.addon.maven;

import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.maven.plugins.ConfigurationBuilder;
import org.jboss.forge.addon.maven.plugins.MavenPluginBuilder;

public enum VRaptorPlugin {

	TOMCAT("org.apache.tomcat.maven:tomcat7-maven-plugin:2.1") {
		@Override
		ConfigurationBuilder extraConfiguration() {
			return null;
		}
	},
	MAVEN_COMPILER("org.apache.maven.plugins:maven-compiler-plugin:3.1") {
		@Override
		ConfigurationBuilder extraConfiguration() {
			ConfigurationBuilder builder = ConfigurationBuilder.create();
			builder.createConfigurationElement("source").setText("1.7");
			builder.createConfigurationElement("target").setText("1.7");
			return builder;
		}
	};

	private String pluginConf;

	VRaptorPlugin(String pluginConf) {
		this.pluginConf = pluginConf;
	}

	abstract ConfigurationBuilder extraConfiguration();
	
	public MavenPluginBuilder build(){
		MavenPluginBuilder plugin = MavenPluginBuilder.create();
		plugin.setCoordinate(CoordinateBuilder.create(pluginConf));
		plugin.setConfiguration(extraConfiguration());
		return plugin;
	}
}
