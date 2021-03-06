package com.timmattison.hadoop.examples.counters.httplogimages;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * A basic driver, using ToolRunner, that lets us run a word count job on Hadoop
 * 
 * @author timmattison
 */
public class HttpLogImagesDriver extends Configured implements Tool {
	public static final String IMAGE_COUNTER_GROUP = "ImageCounter";
	public static final String IMAGE_COUNTER_JPG = "jpg";
	public static final String IMAGE_COUNTER_GIF = "gif";
	public static final String IMAGE_COUNTER_PNG = "png";
	public static final String IMAGE_COUNTER_OTHER = "other";

	@Override
	public int run(String[] args) throws Exception {
		// Do we have two arguments?
		if (args.length != 2) {
			// No, we need at least an input and output directory

			// Show the usage information
			System.out.printf(
					"Usage: %s [generic options] <input dir> <output dir>\n",
					getClass().getSimpleName());

			// Show ToolRunner's generic command usage information on System.out
			ToolRunner.printGenericCommandUsage(System.out);

			// Return an error code
			return -1;
		}

		/**
		 * We have the necessary arguments at this point. Start building up the
		 * configuration.
		 */

		// Create a basic JobConf object using this class as our driver
		JobConf conf = new JobConf(getConf(), HttpLogImagesDriver.class);

		// Set the job's name to the name of this driver class
		conf.setJobName(this.getClass().getName());

		// Set our input and output paths from the passed in arguments
		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		// Set our mapper class as the WordMapper class
		conf.setMapperClass(HttpLogImagesMapper.class);

		// No reducers needed, this is a map only task
		conf.setNumReduceTasks(0);

		// Our mapper will output a Text key followed by an IntWritable value
		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(IntWritable.class);

		// Our reducer will output a Text key followed by an IntWritable value
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		// Run the job
		JobClient.runJob(conf);

		// Return no error
		return 0;
	}

	/**
	 * Run our HttpLogImagesDriver class with ToolRunner
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Run our HttpLogImagesDriver class after ToolRunner processes its options
		int exitCode = ToolRunner.run(new HttpLogImagesDriver(), args);

		// Return the exit code
		System.exit(exitCode);
	}
}
