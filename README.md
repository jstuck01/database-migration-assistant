# Database Migration Assistant
The Database Migration Assistant is a simple straight forward utility to move data from one database to another.  The utility moves data by processing one table at a time as follows:

* EXPORT:  Export data to CSV files from a source database
* PUT: Transfer those files to Cloud Object Storage (COS)
* LOAD: Import data from Cloud Object Storage (COS) to existing tables within the target database

Currently, Db2 based source and targets are supported.

A complete lifecycle of the Database Migration Assistant consists of three phases: EXPORT, PUT and LOAD.  However, a single phase of the lifecycle can be executed, or any combination of phases can be executed, to accomodate instances where only one phase of the process is required.  For example, EXPORT and PUT can be run without LOAD.  Or, just LOAD can be run.

Examples of execution statements:

| Command | Runtime Behavior |
|----------------|---------------|
| migrate EXPORT PUT LOAD  | Executes all phases of the lifecyle. |
| migrate RUNALL  | Same as above.								  |
| migrate EXPORT PUT  | Executes EXPORT and PUT phases only.      |
| migrate LOAD  | Executes LOAD phase only.                       |

NOTE:  The utility will accept command line arguments in any order, but will always execute in the order of EXPORT, PUT and LOAD.  While PUT and LOAD can be run without executing EXPORT, it is assumed that EXPORT was run prior to the other phases.  The same is assumed for LOAD.          

When processing large amounts of data, the processing runtime can be reduced by running multiple instances of the utility in parallel.  Simply create a copy of the distribution, configure and run.

The utility leverages a configuration file that defines the attributes needed to execute a data migration.  For example, connection information for both the source and target databases.  Cloud object storage information such as credentials and storage buckets to use.  As well as runtime information such as where to store the exported CSV files and what table names should be processed.  See below for detailed configuration information. 

The Database Migration Assistant supports built in data types with the exception of LOB types.  For TIMESTAMP data types which require specific formatting, the utility supports specifying a format string to be used for import. 

## Getting Started
Pull the repository and execute maven to build the project.  (e,g,:  mvn package)  This will result in the distribution being created as a compressed file.  Decompress the file to access the artifacts.  Distribution packages are created as TAR and ZIP archives. 

**Building for Linux**
Pull the repository and execute a maven build.  (e.g.:  mvn package).  This will result in a compressed tar file.  Simply decompress the file (e.g.:  gunzip Db2Migrator-bin.tar.gz) and then extract the contents of the archive.  (e.g.:  tar -xvf Db2Migrator-bin.tar).  Once expanded, you will see the following files:

* config.props (Required configuration file)
* Db2Migrator.jar (Executable jar file)
* migrate.sh (bash shell script)
* migrate.bat (Windows batch file)

Ensure that the migrate.sh file has an executable attribute set.  (chmod +x migrate.sh) and the config.props file is edited with your specific runtime information.  See the configuration section below for details.

## Configuration
The configuration file is named config.props and resides in the main directory of the utility.  It consists of 4 sections.  

* COS Configuration
* Database Source Configuration
* Database Target Configuration
* Export Configuration

The following tables detail the different configuration sections. 

### COS Configuration
Config Variable | Example Value | Description | Required
----------------|---------------|-------------|----------
BUCKET_NAME  |  mybucket  |  The name of the IBM Cloud Object Storage bucket  | YES
API_KEY  | cMJD73pkjfglb3skum96SxlrrZYlnEGSn-tE3i_cr7xY  |  The value of "apikey" from the service credentials  |  YES
SERVICE_INSTANCE_ID  |  crn:v1:bluemix:public:cloud-object-storage:global:a/a676d937c:a160-4583-4d7f-8bd5-e6a93b::  |  The value of "resource_instance_id" from the service credentials  |  YES
SERVICE_ENDPOINT  |  s3-api.us-geo.objectstorage.softlayer.net  |  Default value should not need to be changed  |  YES
LOCATION  |  US  | The geo location for the cloud object storage location  |  YES
IAM_ENDPOINT  | https://iam.bluemix.net/oidc/token  |  Default value should not have to be changed  |  YES
CLIENT_TIMEOUT  |  50000  |  The S3 client timeout value.  |  YES
ACCESS_ID_KEY	|  46036c2c7d3h4737a7e0773e0da985b4	| HMAC credential for COS	| YES
SECRET_ACCESS_KEY	|  9523df0737990ddc5df0e41947bfb1fe3d7rf7b62a6ar760	|  HMAC credential for COS	| YES

### DB2 Source Configuration
Config Variable | Example Value | Description | Required
----------------|---------------|-------------|----------
SOURCE_JDBC_DRIVER	|  com.ibm.db2.jcc.DB2Driver	| JDBC Driver Name | YES
SOURCE_DATABASE_HOST_NAME	| db2-myinst-repo.cloud.ibm.com	| Database hostname or IP	| YES
SOURCE_DATABASE_PORT	|  50000	| The port for connecting to the database	| YES
SOURCE_DATABASE_NAME	| BLUDB	| The name of the source database	| YES
SOURCE_DATABASE_USERNAME	|  userName	| The user name to use when connecting to the source	| YES
SOURCE_DATABASE_PASSWORD	|  password	| The password to use when connecting to the source	| YES
SOURCE_USE_SSL_CONNECTION	| false / true	| Use SSL for the connection		| YES

### DB2 Target Configuration
Config Variable | Example Value | Description | Required
----------------|---------------|-------------|----------
TARGET_JDBC_DRIVER	|  com.ibm.db2.jcc.DB2Driver	| JDBC Driver Name | YES
TARGET_DATABASE_HOST_NAME	| dashdb-entry-yp-dal09-10.services.dal.bluemix.net| Database hostname or IP	| YES
TARGET_DATABASE_PORT	|  50000	| The port for connecting to the database	| YES
TARGET_DATABASE_NAME	| BLUDB	| The name of the target database	| YES
TARGET_DATABASE_USERNAME	|  userName	| The user name to use when connecting to the target	| YES
TARGET_DATABASE_PASSWORD	|  password	| The password to use when connecting to the target	| YES
TARGET_USE_SSL_CONNECTION	| false / true	| Use SSL for the connection		| YES
TARGET_USE_TIMESTAMP_FORMAT	| true / false	| Should the timestamp format option be used for load	| YES
TARGET_TIMESTAMP_FORMAT	|  YYYY-MM-DD-HH24.MI.SS.FF6	| A valid timestamp format string for Db2. | Only when setting use timestamp formatting to true.
TARGET_LOAD_ARGS  |  DELIMITER ',' SKIPROWS 1  | Options used with [Db2 create external table](https://www.ibm.com/support/knowledgecenter/en/SS6NHC/com.ibm.swg.im.dashdb.sql.ref.doc/doc/r_create_ext_table.html "DB2 CREATE EXTERNAL TABLE Documentation") statements.  See Descriptions --> option section of the Db2 documentation. | NO 

### Export Configuration
Config Variable | Example Value | Description | Required
----------------|---------------|-------------|----------
TABLE_NAMES_FILE	|  tableNames.txt	| the filename that contains the list of table names to be processed.  One table name per line.  |  YES
EXPORT_FILE_LOCATION	|  C:\\holding\\db2files\\ or /home/db2inst/export/	| Full path to location where export files will reside.  Full read / write access required for phases EXPORT and PUT.	| YES
 

## Encryption Information ##
IBM Cloud Object Storage encrypts all data in motion and at rest.  All data within Db2 is encrypted at the file system level.


## Related Topics ##
<a href="https://www.ibm.com/cloud/lift">IBM Lift - https://www.ibm.com/cloud/lift</a>  
 


