package com.timmattison.hadoop.examples.basic.sortbyintvalue;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class SortByIntValueReducer extends MapReduceBase implements
		Reducer<IntWritable, Text, Text, IntWritable> {

	@Override
	public void reduce(IntWritable key, Iterator<Text> values,
			OutputCollector<Text, IntWritable> output, Reporter reporter)
			throws IOException {

		/**
		 * In this class our key is the number of occurrences followed by a list
		 * of words with that many occurrences. We now break apart these records
		 * so we get one word and one occurrence value per record.
		 */

		// Are there values left?
		while (values.hasNext()) {
			/**
			 * Yes, output the word as the key followed by the count as the
			 * value
			 */
			output.collect(values.next(), key);
		}
	}
}
