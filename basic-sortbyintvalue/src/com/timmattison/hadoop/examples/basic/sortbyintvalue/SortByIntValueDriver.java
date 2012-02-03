package com.timmattison.hadoop.examples.basic.sortbyintvalue;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.KeyValueTextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * A basic driver, using ToolRunner, that lets us run a word count job on Hadoop
 * 
 * @author timmattison
 */
public class SortByIntValueDriver extends Configured implements Tool {

	public static final String REVERSE_OPTION = "REVERSE";
	public static final boolean REVERSE_OPTION_DEFAULT = false;

	@Override
	public int run(String[] args) throws Exception {
		// Do we have two arguments?
		if (args.length < 2) {
			// No, we need at least an input and output directory

			// Show the usage information
			System.out
					.printf("Usage: %s [generic options] <input dir> <output dir> [reverse]\n - set reverse to \"1\" to sort in reverse order",
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
		JobConf conf = new JobConf(getConf(), SortByIntValueDriver.class);

		// Set the reverse option to the default value
		conf.setBoolean(REVERSE_OPTION, REVERSE_OPTION_DEFAULT);

		// Did they specify the reverse order argument?
		if (args.length == 3) {
			// Yes, was it a one?
			if (args[2].equals("1")) {
				// Yes, it was a one. They want reverse order.
				conf.setBoolean(REVERSE_OPTION, true);
			} else {
				// No, set it to false
				conf.setBoolean(REVERSE_OPTION, false);
			}
		}

		// Set the job's name to the name of this driver class
		conf.setJobName(this.getClass().getName());

		// Set our input and output paths from the passed in arguments
		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		/**
		 * Read our file as a one Key-Value pair per line separated by a
		 * separator (whitespace)
		 */
		conf.setInputFormat(KeyValueTextInputFormat.class);

		// Set our reducer class as the SortByIntValueMapper class
		conf.setMapperClass(SortByIntValueMapper.class);

		// Our mapper will output an IntWritable key followed by a Text value
		conf.setMapOutputKeyClass(IntWritable.class);
		conf.setMapOutputValueClass(Text.class);

		// Set our reducer class as the SortByIntValueReducer class
		conf.setReducerClass(SortByIntValueReducer.class);

		// Our reducer will output a Text key followed by an IntWritable value
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		// Run the job
		JobClient.runJob(conf);

		// Return no error
		return 0;
	}

	/**
	 * Run our WordCountDriver class with ToolRunner
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Run our WordCountDriver class after ToolRunner processes its options
		int exitCode = ToolRunner.run(new SortByIntValueDriver(), args);

		// Return the exit code
		System.exit(exitCode);
	}
}
