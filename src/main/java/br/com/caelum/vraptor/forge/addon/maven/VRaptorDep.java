package br.com.caelum.vraptor.forge.addon.maven;

import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;

public enum VRaptorDep {

	VRAPTOR("br.com.caelum", "vraptor", "4.1.2", false), 
	JSTL("javax.servlet", "jstl", "1.2", true),
	INJECT("javax.inject","javax.inject","1",true),
	HIBERNATE_VALIDATOR("org.hibernate","hibernate-validator-cdi","5.1.1.Final",true),
	EL("javax.el","el-api","2.2",true,true),
	WELD("org.jboss.weld","weld-core-impl","2.1.2.Final",true),
	WELD_SERVLET("org.jboss.weld.servlet","weld-servlet-core","2.1.2.Final",true),
	VALIDATION_API("javax.validation","validation-api","1.1.0.Final",true),
	CDI_API("javax.enterprise","cdi-api","1.1",true);
	
	
	

	private String groupId;
	private String artifactId;
	private String version;
	private boolean provided;
	private boolean alwaysProvided;

	VRaptorDep(String groupId, String artifactId, String version,
			boolean provided) {
		this(groupId,artifactId,version,provided,false);
	}
	VRaptorDep(String groupId, String artifactId, String version,
			boolean provided,boolean alwaysProvided) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.provided = provided;
		this.alwaysProvided = alwaysProvided;
	}
	
	public DependencyBuilder build() {
		DependencyBuilder dep = DependencyBuilder.create(groupId)
				.setArtifactId(artifactId).setVersion(version);
		return dep;
	}
	public boolean isProvided(Boolean isJavaeeEnv) {
		return (isJavaeeEnv && provided) || alwaysProvided;
	}

}
