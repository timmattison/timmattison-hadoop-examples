package com.timmattison.hadoop.examples.api.helper;

import java.io.IOException;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HDFSHelperTest extends HDFSRunnable {
	public static final String OUTPUT_PATH = "library-hdfs-helper-example";
	public static final String OUTPUT_FILENAME = "test.txt";
	public static final String FULL_OUTPUT_PATH = OUTPUT_PATH + "/"
			+ OUTPUT_FILENAME;

	public static final String OUTPUT_CONTENTS = "Testing, testing, 1, 2, 3\n\n";

	public static void main(String[] args) throws IOException {
		// Make sure that this class only runs if we connect to HDFS
		OnlyRunOnHDFS onlyRunOnHDFS = new OnlyRunOnHDFS();
		onlyRunOnHDFS.run(new HDFSHelperTest());
	}

	@Override
	public void innerRun() {
		try {
			// Create a path object for our output file
			Path path = new Path(FULL_OUTPUT_PATH);

			// Create the file and write some text into it
			FileSystem hdfs = getFileSystem();
			FSDataOutputStream outputStream = hdfs.create(path);
			outputStream.writeChars(OUTPUT_CONTENTS);
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
