package br.com.caelum.vraptor.forge.addon.commands.plugins.jpa;

public class VendorConf {

	private final String dialect;
	private final String driverClass;

	public VendorConf(String dialect, String driverClass) {
		super();
		this.dialect = dialect;
		this.driverClass = driverClass;
	}

	public String getDialect() {
		return dialect;
	}

	public String getDriverClass() {
		return driverClass;
	}

}
