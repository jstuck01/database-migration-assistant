package com.ibm.cloud.db2.migration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Provides the configuration for the migration
 */
public class ConfigurationBean {

	// Source Config Variables DB
	/**
	 * The JDBC driver name to use for the source connection.
	 */
	private String sourceJdbcDriver = "";

	/**
	 * The source database host name.
	 */
	private String sourceHostName = "";

	/**
	 * The source database port
	 */
	private String sourcePort = "";

	/**
	 * Use SSL true or false
	 */
	private boolean sourceUseSSL = false;

	/**
	 * The source database name
	 */
	private String sourceDbName = "";

	/**
	 * The username for the source connection
	 */
	private String sourceDbUserName = "";

	/**
	 * The password for the source connection
	 */
	private String sourceDbPassword = "";

	// Target Config Variables DB
	/**
	 * The JDBC driver name to use for the target connection.
	 */
	private String targetJdbcDriver = "";

	/**
	 * The source database host name.
	 */
	private String targetHostName = "";

	/**
	 * The source database port
	 */
	private String targetPort = "";

	/**
	 * Use SSL true or false
	 */
	private boolean targetUseSSL = false;

	/**
	 * The source database name
	 */
	private String targetDbName = "";

	/**
	 * The username for the source connection
	 */
	private String targetDbUserName = "";

	/**
	 * The password for the source connection
	 */
	private String targetDbPassword = "";

	/**
	 * Use a time stamp format for imports TRUE or FALSE
	 */
	private boolean useTargetTimestampFormat = false;

	/**
	 * The timestamp format. Must be a valid Db2 timestamp format.
	 */
	private String targetTimestampFormat = "";
	
	/**
	 * The arguments to use as part of the external table load
	 */
	private String targetLoadArgs = "";

	// Config Variables Export
	/**
	 * The name of the file that contains the table names to be processed.
	 */
	private String tableNamesFileName = "";

	/**
	 * The export file location for reading and wrting of export files.
	 */
	private String exportFileLocation = "";

	// Config Variable Load to COS
	/**
	 * Load to COS access_key_id
	 */
	private String accessKeyId = "";

	/**
	 * Load to COS access_key_id
	 */
	private String secretAccessKey = "";

	/**
	 * The name of the bucket the client will connect to
	 */
	private String bucketName = "";

	/**
	 * The IBM Cloud Object Storage API key
	 */
	private String api_key = "";

	/**
	 * The IBM Cloud Service Instance ID
	 */
	private String service_instance_id = "";

	/**
	 * IBM Cloud service end point
	 */
	private String service_endpoint = "";

	/**
	 * The geo location of the object store
	 */
	private String geo_location = "";

	/**
	 * Agent end point
	 */
	private String iam_endpoint = "";

	/**
	 * S3Client time out value
	 */
	private int clientTimeOut = 5000;

	/**
	 * Default constructor
	 */
	public ConfigurationBean() {
		super();
		init();
	}

	/**
	 * Initialize variables with values from properties file.
	 */
	private void init() {
		Properties props = new Properties();
		try {
			InputStream inputStream = new FileInputStream("config.props");
			props.load(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Source Db2
		this.sourceJdbcDriver = props.getProperty("SOURCE_JDBC_DRIVER");
		this.sourceHostName = props.getProperty("SOURCE_DATABASE_HOST_NAME");
		this.sourcePort = props.getProperty("SOURCE_DATABASE_PORT");
		this.sourceUseSSL = Boolean.valueOf(props.getProperty("SOURCE_USE_SSL_CONNECTION"));
		this.sourceDbName = props.getProperty("SOURCE_DATABASE_NAME");
		this.sourceDbUserName = props.getProperty("SOURCE_DATABASE_USERNAME");
		this.sourceDbPassword = props.getProperty("SOURCE_DATABASE_PASSWORD");

		// Target Db2
		this.targetJdbcDriver = props.getProperty("TARGET_JDBC_DRIVER");
		this.targetHostName = props.getProperty("TARGET_DATABASE_HOST_NAME");
		this.targetPort = props.getProperty("TARGET_DATABASE_PORT");
		this.targetUseSSL = Boolean.valueOf(props.getProperty("TARGET_USE_SSL_CONNECTION"));
		this.targetDbName = props.getProperty("TARGET_DATABASE_NAME");
		this.targetDbUserName = props.getProperty("TARGET_DATABASE_USERNAME");
		this.targetDbPassword = props.getProperty("TARGET_DATABASE_PASSWORD");
		this.useTargetTimestampFormat = Boolean.valueOf(props.getProperty("TARGET_USE_TIMESTAMP_FORMAT"));
		this.targetTimestampFormat = props.getProperty("TARGET_TIMESTAMP_FORMAT");
		this.targetLoadArgs = props.getProperty("TARGET_LOAD_ARGS");
		

		// Export
		this.tableNamesFileName = props.getProperty("TABLE_NAMES_FILE");
		this.exportFileLocation = props.getProperty("EXPORT_FILE_LOCATION");

		// COS
		this.clientTimeOut = Integer.parseInt(props.getProperty("CLIENT_TIMEOUT"));
		this.bucketName = props.getProperty("BUCKET_NAME");
		this.api_key = props.getProperty("API_KEY");
		this.service_instance_id = props.getProperty("SERVICE_INSTANCE_ID");
		this.service_endpoint = props.getProperty("SERVICE_ENDPOINT");
		this.geo_location = props.getProperty("LOCATION");
		this.iam_endpoint = props.getProperty("IAM_ENDPOINT");
		this.clientTimeOut = Integer.parseInt(props.getProperty("CLIENT_TIMEOUT"));
		this.accessKeyId = props.getProperty("ACCESS_KEY_ID");
		this.secretAccessKey = props.getProperty("SECRET_ACCESS_KEY");
	}

	/**
	 * Get the fully qualified class name of the configured JDBC driver.
	 * 
	 * @return JDBC class name.
	 */
	public String getSourceJdbcDriver() {
		return sourceJdbcDriver;
	}

	/**
	 * Sets the JDBC driver class name.
	 * 
	 * @param jdbcDriver
	 *            is the fully qualified class name of the JDBC driver.
	 */
	public void setSourceJdbcDriver(String jdbcDriver) {
		this.sourceJdbcDriver = jdbcDriver;
	}

	/**
	 * Get the host name for the database to be used in the connection.
	 * 
	 * @return Database host name.
	 */
	public String getSourceHostName() {
		return sourceHostName;
	}

	/**
	 * Sets the host name of the database to connect to.
	 * 
	 * @param hostName
	 *            of the database to connect to.
	 */
	public void setSourceHostName(String hostName) {
		this.sourceHostName = hostName;
	}

	/**
	 * Get the port number for the configured database.
	 * 
	 * @return the port number.
	 */
	public String getSourcePort() {
		return sourcePort;
	}

	/**
	 * Set the port for the configured database.
	 * 
	 * @param port
	 *            The port number of the database.
	 */
	public void setSourcePort(String port) {
		this.sourcePort = port;
	}

	/**
	 * Gets the name of the configured database.
	 * 
	 * @return The database name.
	 */
	public String getSourceDbName() {
		return sourceDbName;
	}

	/**
	 * Sets the configured database name.
	 * 
	 * @param dbName
	 *            The name of the database.
	 */
	public void setSourceDbName(String dbName) {
		this.sourceDbName = dbName;
	}

	/**
	 * Gets the configured database user name.
	 * 
	 * @return The database user name.
	 */
	public String getSourceDbUserName() {
		return sourceDbUserName;
	}

	/**
	 * Sets the database use name.
	 * 
	 * @param dbUserName
	 *            The database user name.
	 */
	public void setSourceDbUserName(String dbUserName) {
		this.sourceDbUserName = dbUserName;
	}

	/**
	 * Gets the configured database user password.
	 * 
	 * @return The database user password.
	 */
	public String getSourceDbPassword() {
		return sourceDbPassword;
	}

	/**
	 * Sets the database use password.
	 * 
	 * @param dbPassword
	 *            The database user password.
	 */
	public void setSourceDbPassword(String dbPassword) {
		this.sourceDbPassword = dbPassword;
	}

	/**
	 * Indicates if SSL / TLS based connections should be used.
	 * 
	 * @return True if SSL / TLS will be used. False if no SSL / TLS is required.
	 */
	public boolean isSourceUseSSL() {
		return sourceUseSSL;
	}

	/**
	 * Set the configuration to use SSL / TLS.
	 * 
	 * @param useSSL
	 *            True to use SSL / TLS. False to use clear text.
	 */
	public void setSourceUseSSL(boolean sourceUseSSL) {
		this.sourceUseSSL = sourceUseSSL;
	}

	// Target DB

	public String getTargetJdbcDriver() {
		return targetJdbcDriver;
	}

	public void setTargetJdbcDriver(String targetJdbcDriver) {
		this.targetJdbcDriver = targetJdbcDriver;
	}

	public String getTargetHostName() {
		return targetHostName;
	}

	public void setTargetHostName(String targetHostName) {
		this.targetHostName = targetHostName;
	}

	public String getTargetPort() {
		return targetPort;
	}

	public void setTargetPort(String targetPort) {
		this.targetPort = targetPort;
	}

	public boolean isTargetUseSSL() {
		return targetUseSSL;
	}

	public void setTargetUseSSL(boolean targetUseSSL) {
		this.targetUseSSL = targetUseSSL;
	}

	public String getTargetDbName() {
		return targetDbName;
	}

	public void setTargetDbName(String targetDbName) {
		this.targetDbName = targetDbName;
	}

	public String getTargetDbUserName() {
		return targetDbUserName;
	}

	public void setTargetDbUserName(String targetDbUserName) {
		this.targetDbUserName = targetDbUserName;
	}

	public String getTargetDbPassword() {
		return targetDbPassword;
	}

	public void setTargetDbPassword(String targetDbPassword) {
		this.targetDbPassword = targetDbPassword;
	}

	public boolean isUseTargetTimestampFormat() {
		return useTargetTimestampFormat;
	}

	public void setUseTargetTimestampFormat(boolean useTargetTimestampFormat) {
		this.useTargetTimestampFormat = useTargetTimestampFormat;
	}

	public String getTargetTimestampFormat() {
		return targetTimestampFormat;
	}

	public void setTargetTimestampFormat(String targetTimestampFormat) {
		this.targetTimestampFormat = targetTimestampFormat;
	}

	public String getTargetLoadArgs() {
		return targetLoadArgs;
	}

	public void setTargetLoadArgs(String targetLoadArgs) {
		this.targetLoadArgs = targetLoadArgs;
	}

	// EXPORT
	public String getTableNamesFileName() {
		return tableNamesFileName;
	}

	public void setTableNamesFileName(String tableNamesFileName) {
		this.tableNamesFileName = tableNamesFileName;
	}

	public String getExportFileLocation() {
		if(exportFileLocation.endsWith(File.separator)) {
			exportFileLocation = exportFileLocation + File.separator;
		}
		return exportFileLocation;
	}

	public void setExportFileLocation(String exportFileLocation) {
		this.exportFileLocation = exportFileLocation;
	}

	// COS
	public int getClientTimeOut() {
		return clientTimeOut;
	}

	public void setClientTimeOut(int clientTimeOut) {
		this.clientTimeOut = clientTimeOut;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getApi_key() {
		return api_key;
	}

	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}

	public String getService_instance_id() {
		return service_instance_id;
	}

	public void setService_instance_id(String service_instance_id) {
		this.service_instance_id = service_instance_id;
	}

	public String getService_endpoint() {
		return service_endpoint;
	}

	public void setService_endpoint(String service_endpoint) {
		this.service_endpoint = service_endpoint;
	}

	public String getGeo_location() {
		return geo_location;
	}

	public void setGeo_location(String geo_location) {
		this.geo_location = geo_location;
	}

	public String getIam_endpoint() {
		return iam_endpoint;
	}

	public void setIam_endpoint(String iam_endpoint) {
		this.iam_endpoint = iam_endpoint;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getSecretAccessKey() {
		return secretAccessKey;
	}

	public void setSecretAccessKey(String secretAccessKey) {
		this.secretAccessKey = secretAccessKey;
	}
}
