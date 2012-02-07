package com.timmattison.hadoop.examples.api.copymerge;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;

public class TestCopyMerge {
	private static final int innerLoopMax = 1024;
	private static final String MAIN_PATH = "./api-copymerge-example";
	private static final String INPUT_PATH = MAIN_PATH + "/input";

	public static void main(String[] args) {
		try {
			Configuration configuration = new Configuration();
			FileSystem fileSystem = FileSystem.get(configuration);

			Path inputPath = new Path(INPUT_PATH);
			Path mainPath = new Path(MAIN_PATH);
			Path outputPath = new Path(MAIN_PATH);

			// Delete the input and output paths recursively
			boolean deleted = fileSystem.delete(inputPath, true);
			deleted = fileSystem.delete(outputPath, true);

			FSDataOutputStream fsDataOutputStream;

			boolean created = fileSystem.mkdirs(mainPath,
					FsPermission.getDefault());

			// Create the input files
			for (int loop = 0; loop < 10; loop++) {
				String tempFile = loop + "-data.txt";
				Path tempFilePath = new Path(inputPath + "/" + tempFile);
				fsDataOutputStream = fileSystem.create(tempFilePath);

				for (int innerLoop = 0; innerLoop < innerLoopMax; innerLoop++) {
					fsDataOutputStream.writeChars("Data point,"
							+ (loop * innerLoopMax) + innerLoop + "\n");
				}

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
