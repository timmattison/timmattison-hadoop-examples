package com.timmattison.hadoop.examples.basic.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class WordMapper extends MapReduceBase implements
		Mapper<LongWritable, Text, Text, IntWritable> {

	@Override
	public void map(LongWritable key, Text value,
			OutputCollector<Text, IntWritable> output, Reporter reporter)
			throws IOException {
		/**
		 * In this class we ignore the key. The key value here is a byte offset
		 * into the input file. We don't need that information so we don't need
		 * to touch it.
		 */

		// Read in one full line of text from the value
		String s = value.toString();

		// Split the string on non-word characters (ie. spaces, commas, etc)
		for (String word : s.split("\\W+")) {
			// Is this word longer than zero characters?
			if (word.length() > 0) {
				/**
				 * Yes, output the Text key (the word) and the number 1 since
				 * this is a single instance of it
				 */
				output.collect(new Text(word), new IntWritable(1));
			}
		}
	}
}
