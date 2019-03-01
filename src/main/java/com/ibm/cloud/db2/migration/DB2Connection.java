package com.ibm.cloud.db2.migration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB2Connection {
	ConfigurationBean config = null;

	public DB2Connection(ConfigurationBean config) {
		super();
		this.config = config;
	}

	/**
	 * Gets a Db2 formatted JDBC URL based on the configuration.
	 * 
	 * @return The Db2 JDBC URL.
	 */
	private String getJdbcUrl(String hostName, String port, String dbName, boolean useSsl) {
		String jdbcUrl = "jdbc:db2://" + hostName + ":" + port + "/" + dbName;
		if (useSsl) {
			jdbcUrl = jdbcUrl + ":sslConnection=true;";
		}
		return jdbcUrl;
	}

	/**
	 * Gets a JDBC connection to the configured database.
	 * 
	 * @return The JDBC connection.
	 */
	private Connection getConnection(String jdbcDriver, String hostName, String dbName, String port, String userName,
			String password, boolean useSsl) {
		Connection connection = null;
		try {
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(getJdbcUrl(hostName, port, dbName, useSsl), userName, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return connection;
	}

	/**
	 * Gets a JDBC connection to the configured source database.
	 * 
	 * @return the JDBC Connection
	 */
	public Connection getSourceConnection() {
		String jdbcDriver = this.config.getSourceJdbcDriver();
		String hostName = this.config.getSourceHostName();
		String dbName = this.config.getSourceDbName();
		String port = this.config.getSourcePort();
		String userName = this.config.getSourceDbUserName();
		String password = this.config.getSourceDbPassword();
		boolean useSsl = this.config.isSourceUseSSL();
		Connection connection = getConnection(jdbcDriver, hostName, dbName, port, userName, password, useSsl);
		return connection;
	}

	/**
	 * Gets a JDBC connection to the configured target database.
	 * 
	 * @return the JDBC Connection
	 */
	public Connection getTargetConnection() {
		String jdbcDriver = this.config.getTargetJdbcDriver();
		String hostName = this.config.getTargetHostName();
		String dbName = this.config.getTargetDbName();
		String port = this.config.getTargetPort();
		String userName = this.config.getTargetDbUserName();
		String password = this.config.getTargetDbPassword();
		boolean useSsl = this.config.isTargetUseSSL();
		Connection connection = getConnection(jdbcDriver, hostName, dbName, port, userName, password, useSsl);
		return connection;
	}
}
