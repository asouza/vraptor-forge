package br.com.caelum.vraptor.forge.addon.specific;

import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;

public enum VRaptorDep {

	VRAPTOR("br.com.caelum", "vraptor", "4.1.2", false), 
	SERVLET("javax.servlet", "jstl", "1.2", false),
	INJECT("javax.inject","javax.inject","1",true),
	HIBERNATE_VALIDATOR("org.hibernate","hibernate-validator-cdi","5.1.1",true),
	EL("javax.el","el-api","2.2",true),
	WELD("org.jboss.weld","weld-core-impl","2.1.2.Final",true),
	WELD_SERVLET("org.jboss.weld.servlet","weld-servlet-core","2.1.2.Final",true);
	
	
	

	private String groupId;
	private String artifactId;
	private String version;
	private boolean provided;

	VRaptorDep(String groupId, String artifactId, String version,
			boolean provided) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.provided = provided;
	}
	
	public boolean isProvided() {
		return provided;
	}

	public DependencyBuilder build() {
		DependencyBuilder dep = DependencyBuilder.create(groupId)
				.setArtifactId(artifactId).setVersion(version);
		return dep;
	}

}
