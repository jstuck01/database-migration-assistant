package com.ibm.cloud.db2.migration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 * Implementation to execute simple database migrations. Provides support for
 * data export, moving data to cloud object storage, and loading that data into
 * a remote database.
 */
public class Db2Migrator {
	private static Log logger = LogFactory.getLog(Db2Migrator.class);
	private ConfigurationBean config = null;
	private ArrayList<String> tableNames = null;

	/**
	 * Default Constructor
	 */
	public Db2Migrator() {
		super();
		this.config = new ConfigurationBean();
		this.tableNames = new ArrayList<String>();
		populateTableNames();
	}

	/**
	 * Reads table names from a file. There is only one table name per line. The
	 * file name read is defined in the configuration file.
	 */
	private void populateTableNames() {
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(this.config.getTableNamesFileName()));
			String line;
			while ((line = in.readLine()) != null) {
				this.tableNames.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * Exports table data to the file location defined within the configuration
	 * file.
	 */
	public void exportTableData() {
		logger.info("---------------------------------");
		logger.info("EXPORT PHASE STARTED");
		DB2Connection db2Connection = new DB2Connection(this.config);
		Statement statement;
		Connection connection = db2Connection.getSourceConnection();
		for (String tableName : this.tableNames) {
			logger.info("Exporting Table: " + tableName);
			String fileName = tableName + ".csv";
			String db2ExportCommand = getDb2ExportCommand(fileName, tableName);
			logger.debug("Here is the Db2 command to export for the table name " + tableName);
			logger.debug(db2ExportCommand);
			try {
				statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(db2ExportCommand);
				int rowsExported = 0;
				while (resultSet.next()) {
					rowsExported = resultSet.getInt(1);
				}
				logger.info("Export File Name: " + fileName + "  | Records Exported: " + rowsExported);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		logger.info("EXPORT PHASE FINISHED");
		logger.info("---------------------------------");
	}

	/**
	 * Moves data that has been exported to a cloud object repository.
	 */
	public void transferToCos() {
		logger.info("---------------------------------");
		logger.info("TRANSFER PHASE STARTED");
		logger.info("IBM Cloud Object Repository Endpoint: " + this.config.getService_endpoint());
		logger.info("Bucket Name: " + this.config.getBucketName());
		for (String tableName : this.tableNames) {
			String fileName = tableName + ".csv";
			logger.info("Put File: " + fileName);
			COSClient cosClient = new COSClient(this.config);
			// cosClient.backupFile(fileName, this.config.getExportFileLocation() +
			// fileName);
			cosClient.putMultiPart(fileName, this.config.getExportFileLocation() + fileName);
		}
		logger.info("TRANSFER PHASE COMPLETED");
		logger.info("---------------------------------");
	}

	/**
	 * Loads data from a cloud object repository to a remote database.
	 */
	public void loadRemoteSystem() {
		logger.info("---------------------------------");
		logger.info("LOAD PHASE STARTED");
		DB2Connection db2conn = new DB2Connection(this.config);
		Statement statement;
		//Connection connection = db2conn.getTargetConnection();
		try {
			for (String tableName : this.tableNames) {
				logger.info("Loading Table: " + tableName);
				String fileName = tableName + ".csv";
				String db2LoadCommand = getDb2LoadCommand(fileName, tableName);
				logger.debug("Here is the DB Command:");
				logger.debug(db2LoadCommand);
				Connection connection = db2conn.getTargetConnection();
				statement = connection.createStatement();
				statement.execute(db2LoadCommand);
				logger.info("Load File Name: " + fileName + "  | Records Loaded: " + statement.getUpdateCount());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("LOAD PHASE FINISHED");
		logger.info("---------------------------------");
	}

	/**
	 * Generates an export command based on the current table name.
	 * 
	 * @param fileName
	 *            the file name to write data to
	 * @param tableName
	 *            the table name to select data from.
	 * @return the Db2 export command
	 */
	private String getDb2ExportCommand(String fileName, String tableName) {
		String command = "CALL SYSPROC.ADMIN_CMD ('EXPORT to " + config.getExportFileLocation() + fileName
				+ " OF DEL MODIFIED BY NOCHARDEL COLDEL| MESSAGES ON SERVER select * from " + tableName + "')";
		return command;
	}

	/**
	 * Generates a load command based on the current table name.
	 * 
	 * @param fileName
	 *            The name of the file / object key to read from cloud object
	 *            storage
	 * @param tableName
	 *            the name of the table being processed
	 * @return the Db2 load command
	 */
	private String getDb2LoadCommand(String fileName, String tableName) {
		String command = "insert into " + tableName + " select * from external '" + fileName
				+ "' using (s3('s3.us-east.cloud-object-storage.appdomain.cloud', '" + this.config.getAccessKeyId() + "', '"
				+ this.config.getSecretAccessKey() + "', '" + this.config.getBucketName() + "' ) " + this.config.getTargetLoadArgs() + " ";
		if (this.config.isUseTargetTimestampFormat()) {
			command = command + "TIMESTAMP_FORMAT '" + this.config.getTargetTimestampFormat() + "'";
		}
		command = command + ")";
		return command;
	}
}
