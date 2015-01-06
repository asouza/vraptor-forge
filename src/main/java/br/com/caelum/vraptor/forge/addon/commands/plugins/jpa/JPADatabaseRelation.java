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
	      DIALECTS.put(DatabaseType.DERBY, new VendorConf("org.hibernate.dialect.DerbyDialect","","org.apache.derby:derbyclient:10.11.1.1"));
	      DIALECTS.put(DatabaseType.POSTGRES, new VendorConf("org.hibernate.dialect.PostgreSQLDialect","org.postgresql.Driver","org.postgresql:postgresql:9.3-1102-jdbc41"));
	      DIALECTS.put(DatabaseType.MYSQL, new VendorConf("org.hibernate.dialect.MySQL5InnoDBDialect","com.mysql.jdbc.Driver","mysql:mysql-connector-java:5.1.34"));
	      DIALECTS.put(DatabaseType.MYSQL5_INNODB, new VendorConf("org.hibernate.dialect.MySQL5InnoDBDialect","com.mysql.jdbc.Driver","mysql:mysql-connector-java:5.1.34"));
	      DIALECTS.put(DatabaseType.MYSQL_INNODB, new VendorConf("org.hibernate.dialect.MySQL5InnoDBDialect","com.mysql.jdbc.Driver","mysql:mysql-connector-java:5.1.34"));
	   }
	 
	  public VendorConf getVendorConf(DatabaseType databaseType){
		  return DIALECTS.get(databaseType);
	  }
}
