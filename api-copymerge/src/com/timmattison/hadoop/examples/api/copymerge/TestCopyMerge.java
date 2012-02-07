package com.timmattison.hadoop.examples.api.copymerge;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;

import com.timmattison.hadoop.examples.api.helper.HDFSRunnable;
import com.timmattison.hadoop.examples.api.helper.OnlyRunOnHDFS;

public class TestCopyMerge extends HDFSRunnable {
	private static final int innerLoopMax = 1024;
	private static final String MAIN_PATH = "./api-copymerge-example";
	private static final String INPUT_PATH = MAIN_PATH + "/input";

	public static void main(String[] args) throws IOException {
		// Make sure that this class only runs if we connect to HDFS
		OnlyRunOnHDFS onlyRunOnHDFS = new OnlyRunOnHDFS();
		onlyRunOnHDFS.run(new TestCopyMerge());
	}

	@Override
	public void innerRun() {
		try {
			Configuration configuration = getConfiguration();
			FileSystem fileSystem = getFileSystem();

			Path inputPath = new Path(INPUT_PATH);
			Path mainPath = new Path(MAIN_PATH);
			Path outputPath = new Path(MAIN_PATH);

			// Delete the input and output paths recursively
			fileSystem.delete(inputPath, true);
			fileSystem.delete(outputPath, true);

			// Create the main directory
			fileSystem.mkdirs(mainPath, FsPermission.getDefault());

			// Create the input files
			for (int loop = 0; loop < 10; loop++) {
				// Build the temp file's name
				String tempFile = loop + "-data.txt";
				Path tempFilePath = new Path(inputPath + "/" + tempFile);

				// Create the temp file
				FSDataOutputStream fsDataOutputStream = fileSystem
						.create(tempFilePath);

				// Write a bunch of lines to it
				for (int innerLoop = 0; innerLoop < innerLoopMax; innerLoop++) {
					fsDataOutputStream.writeChars("Data point,"
							+ (loop * innerLoopMax) + innerLoop + "\n");
				}

				// Close the temp file
				fsDataOutputStream.close();
			}

			// Delete the source directory
			boolean deleteSource = true;

			// Don't add any string after each file
			String addString = null;

			// Put the final output into final.txt
			Path finalOutputPath = new Path(MAIN_PATH + "/final.txt");

			// Use copy merge to combine all of the input files
			FileUtil.copyMerge(fileSystem, inputPath, fileSystem,
					finalOutputPath, deleteSource, configuration, addString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
