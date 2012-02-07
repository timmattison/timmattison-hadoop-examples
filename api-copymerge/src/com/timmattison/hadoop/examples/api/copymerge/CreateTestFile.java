package com.timmattison.hadoop.examples.api.copymerge;

import java.io.IOException;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.timmattison.hadoop.examples.api.helper.HDFSRunnable;
import com.timmattison.hadoop.examples.api.helper.OnlyRunOnHDFS;

public class CreateTestFile extends HDFSRunnable {

	public static void main(String[] args) throws IOException {
		// Make sure that this class only runs if we connect to HDFS
		OnlyRunOnHDFS onlyRunOnHDFS = new OnlyRunOnHDFS();
		onlyRunOnHDFS.run(new CreateTestFile());
	}

	@Override
	public void innerRun() {
		try {
			// Create a path object for our "test.txt" file
			Path path = new Path("test.txt");

			// Create the file and write "Testing" into it
			FileSystem hdfs = getFileSystem();
			FSDataOutputStream outputStream = hdfs.create(path);
			outputStream.writeChars("Testing");
			outputStream.close();

			// Delete the file but don't do a recursive delete
			hdfs.delete(path, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
