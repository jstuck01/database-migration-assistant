package com.ibm.cloud.db2.migration;

import java.text.SimpleDateFormat;
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
 * Class to provide entry point into application.
 */
public final class Main {
	private static Log logger = LogFactory.getLog(Main.class);

	/**
	 * Default Constructor
	 */
	private Main() {
		super();
	}

	/**
	 * Entry point for Database Migration Assistant
	 * 
	 * @param args
	 *            The arguments for the runtime. Up to 3 arguments are supported,
	 *            and can be used in any combination without affecting processing
	 *            workflow. Valid arguments are: RUNALL - Execute export, move to
	 *            COS and remote database load. EXPORT - Executes the export phase
	 *            only. PUT - Executes the movement of data to COS phase only. LOAD
	 *            - Executes the load phase only.
	 */
	public static void main(String[] args) {
		long totalStartTime = 0;
		long totalEndTime = 0;
		long totalElapsedTime = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		totalStartTime = System.currentTimeMillis();

		System.out.println("#################################");
		System.out.println("#################################");
		System.out.println("#################################");
		System.out.println("Migration Assistant Started.  See the log file for process details.");
		System.out.println("PROCESS START TIME: " + sdf.format(totalStartTime) + System.lineSeparator());
		logger.info("#################################");
		logger.info("#################################");
		logger.info("#################################");
		logger.info("Migration Assistant Started!");
		logger.info("PROCESS START TIME: " + sdf.format(totalStartTime) + System.lineSeparator());
		Db2Migrator migrator = new Db2Migrator();
		if (args.length < 1) {
			invalidUsage();
			System.exit(0);
		}
		ArrayList<String> cmdArgs = new ArrayList<String>();
		for (String arg : args) {
			cmdArgs.add(arg.toLowerCase());
		}
		if (cmdArgs.contains("runall") && cmdArgs.size() == 1) {
			runAll(migrator);
		} else if (cmdArgs.contains("runall") && cmdArgs.size() > 1) {
			invalidUsage();
			System.exit(0);
		}
		if (cmdArgs.contains("export")) {
			export(migrator);
		}
		if (cmdArgs.contains("put")) {
			transferToCos(migrator);
		}
		if (cmdArgs.contains("load")) {
			load(migrator);
		}
		totalEndTime = System.currentTimeMillis();
		totalElapsedTime = totalEndTime - totalStartTime;
		logger.info("PROCESS END TIME: " + sdf.format(totalEndTime) + System.lineSeparator());
		logger.info("TOTAL PROCESS TIME: " + elapsedToString(totalElapsedTime));
		logger.info("Migration Assistant has Completed!");
		logger.info("#################################");
		logger.info("#################################");
		logger.info("#################################");
		System.out.println("PROCESS END TIME: " + sdf.format(totalEndTime) + System.lineSeparator());
		System.out.println("TOTAL PROCESS TIME: " + elapsedToString(totalElapsedTime));
		System.out.println("Migration Assistant has Completed!");
		System.out.println("#################################");
		System.out.println("#################################");
		System.out.println("#################################");
		System.out.println();
	}

	/**
	 * Provides messaging on usage of application.
	 */
	private static void invalidUsage() {
		logger.info("Invalid usage!  Command line arguments must include <OPERATION> to be run.  Exiting!");
		System.out.println("USAGE:  migrate <OPERATION>");
		System.out.println("Valid Operations: RUNALL, EXPORT, PUT, LOAD");
		System.out.println(
				"NOTE:  RUNALL can be used only by itself.  EXPORT, PUT and LOAD can be used in any combination.");
		System.out.println("#################################");
		System.out.println("#################################");
		System.out.println("#################################");
		System.out.println();
	}

	/**
	 * Aggregates execution of all workflow phases.
	 * 
	 * @param migrator
	 *            is the instance of the migration to be executed.
	 */
	private static void runAll(Db2Migrator migrator) {
		logger.info("Running All Migration Phases");
		export(migrator);
		transferToCos(migrator);
		load(migrator);
	}

	/**
	 * Executes the export phase of the workflow.
	 * 
	 * @param migrator
	 *            is the instance of the migration to be executed.
	 */
	private static void export(Db2Migrator migrator) {
		System.out.println("Exporting......");
		logger.info("*********************************");
		logger.info("*********************************");
		logger.info("*****  PROCESSING EXPORT");
		logger.info("*****");
		migrator.exportTableData();
		logger.info("*****");
		logger.info("*****  EXPORT COMPLETE!");
		logger.info("*********************************");
		logger.info("*********************************");
	}

	/**
	 * Executes the data transfer phase of the workflow,
	 * 
	 * @param migrator
	 *            is the instance of the migration to be executed.
	 */
	private static void transferToCos(Db2Migrator migrator) {
		System.out.println("Moving to COS.....");
		logger.info("*********************************");
		logger.info("*********************************");
		logger.info("*****  INITIATING COS TRANSFER");
		logger.info("*****");
		migrator.transferToCos();
		logger.info("*****");
		logger.info("*****  COS TRANSFER COMPLETE!");
		logger.info("*********************************");
		logger.info("*********************************");
	}

	/**
	 * Executes the load phase of the workflow.
	 * 
	 * @param migrator
	 *            is the instance of the migration to be executed.
	 */
	private static void load(Db2Migrator migrator) {
		System.out.println("Loading......");
		logger.info("*********************************");
		logger.info("*********************************");
		logger.info("*****  INITIATING REMOTE LOAD");
		logger.info("*****");
		migrator.loadRemoteSystem();
		logger.info("*****");
		logger.info("*****  REMOTE LOAD COMPLETE!");
		logger.info("*********************************");
		logger.info("*********************************");
	}

	/**
	 * Formats elapsed system time to human readable format.
	 * 
	 * @param elapsedTimeMillis
	 *            - Elapsed time to be formatted.
	 * @return
	 */
	private static String elapsedToString(long elapsedTimeMillis) {
		long seconds = (elapsedTimeMillis) / 1000; // round
		long minutes = seconds / 60;
		long hours = minutes / 60;
		return String.format("%1$02d:%2$02d:%3$02d", hours, minutes % 60, seconds % 60);
	}
}
