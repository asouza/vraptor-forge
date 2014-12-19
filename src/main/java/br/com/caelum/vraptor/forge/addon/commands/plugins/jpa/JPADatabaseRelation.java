package br.com.caelum.vraptor.forge.addon.commands.plugins.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.forge.addon.javaee.jpa.DatabaseType;

@ApplicationScoped
public class JPADatabaseRelation {

	private static Map<DatabaseType, VendorConf> DIALECTS = new HashMap<>();

	   static
	   {
	      DIALECTS.put(DatabaseType.DERBY, new VendorConf("org.hibernate.dialect.DerbyDialect",""));
	      DIALECTS.put(DatabaseType.DB2, new VendorConf("org.hibernate.dialect.DB2Dialect", ""));
	      DIALECTS.put(DatabaseType.POSTGRES, new VendorConf("org.hibernate.dialect.PostgreSQLDialect","org.postgresql.Driver"));
	      DIALECTS.put(DatabaseType.MYSQL, new VendorConf("org.hibernate.dialect.MySQL5InnoDBDialect","com.mysql.jdbc.Driver"));
	   }
	 
	  public VendorConf getVendorConf(DatabaseType databaseType){
		  return DIALECTS.get(databaseType);
	  }
}
