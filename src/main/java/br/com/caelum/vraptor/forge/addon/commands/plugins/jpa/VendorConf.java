package br.com.caelum.vraptor.forge.addon.commands.plugins.jpa;

public class VendorConf {

	private final String dialect;
	private final String driverClass;
	private String driverMavenDependency;

	public VendorConf(String dialect, String driverClass, String driverMavenDependency) {
		super();
		this.dialect = dialect;
		this.driverClass = driverClass;
		this.driverMavenDependency = driverMavenDependency;
	}

	public String getDialect() {
		return dialect;
	}

	public String getDriverClass() {
		return driverClass;
	}
	
	public String getDriverMavenDependency() {
		return driverMavenDependency;
	}
	
	public boolean isMavenConfigured(){
		return !driverMavenDependency.trim().isEmpty();
	}

}
