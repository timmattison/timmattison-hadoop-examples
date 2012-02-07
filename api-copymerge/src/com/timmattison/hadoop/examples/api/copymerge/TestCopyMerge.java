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

	public static void main(String[] args) {
		try {
			Configuration configuration = new Configuration();
			FileSystem fileSystem = FileSystem.get(configuration);

			Path inputPath = new Path(MAIN_PATH + "/input");
			Path mainPath = new Path(MAIN_PATH);
			Path outputPath = new Path(MAIN_PATH + "/output");

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

			boolean deleteSource = false;
			String addString = "\n";

			Path finalOutputPath = new Path("final.txt");
			FileUtil.copyMerge(fileSystem, inputPath, fileSystem,
					finalOutputPath, deleteSource, configuration, addString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
