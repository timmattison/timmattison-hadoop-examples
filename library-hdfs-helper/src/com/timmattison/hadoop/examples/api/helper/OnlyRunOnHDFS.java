package com.timmattison.hadoop.examples.api.helper;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;

/**
 * Ensures that the code that is passed into it only runs on HDFS, not the local file system
 * 
 * @author timmattison
 */
public class OnlyRunOnHDFS {
	private static final String CORE_SITE_NAME = "core-site.xml";
	private static final String CORE_SITE_LOCATION = "/etc/hadoop-0.20/conf.empty/"
			+ CORE_SITE_NAME;
	private static final String LOCAL_SEARCH_PATH = "bin/";
	private static final String LOCAL_CORE_SITE_LOCATION = LOCAL_SEARCH_PATH
			+ CORE_SITE_NAME;

	private static boolean updatedConfiguration = false;

	public void run(HDFSRunnable hdfsRunnable) throws IOException {
		try {
			// Get the configuration and connect to the file system
			Configuration configuration = new Configuration();
			FileSystem fileSystem = FileSystem.get(configuration);

			// Is this the local file system?
			if (fileSystem instanceof LocalFileSystem) {
				// Yes, we need to do use the cluster. Update the configuration.
				updatedConfiguration = true;

				/**
				 * Remove the file if it already exists. Just in case this is a
				 * symlink or something.
				 */
				removeTemporaryConfigurationFile();

				// Copy the core-site.xml file to where our JVM can see it
				copyConfigurationToTemporaryLocation();

				// Recreate the configuration object
				configuration = new Configuration();

				// Get a new file system object
				fileSystem = FileSystem.get(configuration);

				// Is this the local file system?
				if (fileSystem instanceof LocalFileSystem) {
					// Yes, give up. We cannot connect to the cluster.
					System.err.println("Failed to connect to the cluster.");
					System.exit(2);
				}
			}

			// Set the configuration and the file system
			hdfsRunnable.setConfiguration(configuration);
			hdfsRunnable.setFileSystem(fileSystem);

			// Run it
			hdfsRunnable.run();
		} catch (IOException ioe) {
			// An IOException occurred, give up
			System.err.println("IOException during operation: "
					+ ioe.toString());
			System.exit(1);
		} finally {
			// Did we update the configuration?
			if (updatedConfiguration) {
				// Yes, clean up the temporary configuration file
				removeTemporaryConfigurationFile();
			}
		}
	}

	private static void copyConfigurationToTemporaryLocation()
			throws IOException {
		Runtime.getRuntime().exec(
				new String[] { "cp", CORE_SITE_LOCATION,
						LOCAL_CORE_SITE_LOCATION });
	}

	private static void removeTemporaryConfigurationFile() throws IOException {
		Runtime.getRuntime().exec(
				new String[] { "rm", LOCAL_CORE_SITE_LOCATION });
	}
}