package com.timmattison.hadoop.examples.basic.invertedindex;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class InvertedIndexMapper extends MapReduceBase implements
		Mapper<Text, Text, Text, Text> {

	@Override
	public void map(Text key, Text value, OutputCollector<Text, Text> output,
			Reporter reporter) throws IOException {
		// Get the line number and the words on this line

		/**
		 * NOTE: The input data must be a file containing text data where each
		 * line starts with a line number, followed by whitespace, and then a
		 * line of text
		 * 
		 */

		// Get the line number as a string and convert it to an integer
		int lineNumber = Integer.parseInt(key.toString());

		// Get the rest of the line
		String words = value.toString();

		// Get the filename
		FileSplit fileSplit = (FileSplit) reporter.getInputSplit();
		Path path = fileSplit.getPath();
		String fileName = path.getName();

		// Split the string on non-word characters (ie. spaces, commas, etc)
		for (String word : words.split("\\W+")) {
			// Is the word longer than zero characters?
			if (word.length() > 0) {
				/**
				 * Yes, output the word itself as the key and the filename, an @
				 * symbol, and the line number as the value
				 */
				output.collect(new Text(word.toLowerCase()), new Text(fileName
						+ "@" + lineNumber));
			}
		}
	}
}
