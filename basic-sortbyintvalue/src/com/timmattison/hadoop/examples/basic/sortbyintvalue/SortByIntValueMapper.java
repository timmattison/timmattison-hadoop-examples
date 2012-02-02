package com.timmattison.hadoop.examples.basic.sortbyintvalue;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class SortByIntValueMapper extends MapReduceBase implements
		Mapper<Text, Text, IntWritable, Text> {

	@Override
	public void map(Text key, Text value,
			OutputCollector<IntWritable, Text> output, Reporter reporter)
			throws IOException {
		/**
		 * In this class the key is a word and the value is the count. We want
		 * to swap them so they will be grouped and sorted by their counts
		 * before the reduce phase.
		 */

		// Get the word
		String words = key.toString();

		// Get the count
		int count = Integer.parseInt(value.toString());

		// Output the count as the key followed by the word as the value
		output.collect(new IntWritable(count), new Text(words));
	}
}
