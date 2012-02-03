package com.timmattison.hadoop.examples.writables.wordcooccurrence;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class WordCooccurrenceDriver extends Configured implements Tool {

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
		JobConf conf = new JobConf(getConf(), WordCooccurrenceDriver.class);

		// Set the job's name to the name of this driver class
		conf.setJobName(this.getClass().getName());

		// Set our mapper class as the WordCooccurrenceMapper class
		conf.setMapperClass(WordCooccurrenceMapper.class);

		// Set our reducer class as the WordCooccurrenceReducer class
		conf.setReducerClass(WordCooccurrenceReducer.class);

		// Set our input and output paths from the passed in arguments
		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		/**
		 * Our mapper will output a TextPairWritable key and a LongWritable
		 * value
		 */
		conf.setMapOutputKeyClass(TextPairWritable.class);
		conf.setMapOutputValueClass(LongWritable.class);

		// Our reducer will output a Text key and a LongWritable value
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(LongWritable.class);

		// Run the job
		JobClient.runJob(conf);

		// Return no error
		return 0;
	}

	public static void main(String[] args) throws Exception {
		// Run our WordCooccurrenceDriver class after ToolRunner processes its
		// options
		int exitCode = ToolRunner.run(new WordCooccurrenceDriver(), args);

		// Return the exit code
		System.exit(exitCode);
	}
}
