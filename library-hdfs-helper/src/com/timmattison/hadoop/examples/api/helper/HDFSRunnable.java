package com.timmattison.hadoop.examples.api.helper;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

public abstract class HDFSRunnable implements Runnable {
	private Configuration configuration = null;
	private FileSystem fileSystem = null;

	public HDFSRunnable(Configuration configuration, FileSystem fileSystem) {
		this.configuration = configuration;
		this.fileSystem = fileSystem;
	}

	public HDFSRunnable() {
	}

	public Configuration getConfiguration() {
		// Is the configuration NULL?
		if (configuration == null) {
			// Yes, this should never happen
			throw new IllegalStateException("Configuration cannot be NULL");
		}

		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public FileSystem getFileSystem() {
		// Is the file system NULL?
		if (fileSystem == null) {
			// Yes, this should never happen
			throw new IllegalStateException("File system cannot be NULL");
		}

		return fileSystem;
	}

	public void setFileSystem(FileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}

	/**
	 * Checks to make sure that the configuration and file system are set. They
	 * will throw IllegalStateExceptions if they are not.
	 */
	private void checkAll() {
		getConfiguration();
		getFileSystem();
	}

	@Override
	public void run() {
		// Make sure the values are all set
		checkAll();

		// They're all set, run our real method
		innerRun();
	}

	public abstract void innerRun();
}
