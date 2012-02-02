package com.timmattison.hadoop.examples.basic.invertedindex;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class InvertedIndexReducer extends MapReduceBase implements
		Reducer<Text, Text, Text, Text> {

	// Words are separated by commas
	private static final String SEP = ",";

	@Override
	public void reduce(Text key, Iterator<Text> values,
			OutputCollector<Text, Text> output, Reporter reporter)
			throws IOException {
		// Get the word
		String word = key.toString();

		// Create a StringBuilder to build up our output value
		StringBuilder stringBuilder = new StringBuilder();

		/**
		 * Initialize our separator to a blank string so we don't use it on the
		 * first run
		 */
		String separator = "";

		// Are there more values?
		while (values.hasNext()) {
			// Append a separator
			stringBuilder.append(separator);

			// Append the next value
			stringBuilder.append(values.next().toString());

			// Make sure the separator is set
			separator = SEP;
		}

		/**
		 * Output the word as the key and the list of locations it was found as
		 * the value
		 */
		output.collect(new Text(word), new Text(stringBuilder.toString()));
	}
}
