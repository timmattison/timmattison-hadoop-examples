package com.timmattison.hadoop.examples.writables.wordcooccurrence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class WordCooccurrenceMapper extends MapReduceBase implements
		Mapper<LongWritable, Text, TextPairWritable, LongWritable> {

	@Override
	public void map(LongWritable key, Text value,
			OutputCollector<TextPairWritable, LongWritable> output,
			Reporter reporter) throws IOException {
		// Get the line from the value
		String line = value.toString();

		// Create an ArrayList for the list of words
		List<String> words = new ArrayList<String>();

		boolean first = true;

		// Split the string on non-word characters (ie. spaces, commas, etc)
		for (String word : line.split("\\W+")) {
			// Is the word longer than zero characters?
			if (word.length() > 0) {
				// Yes, convert it to lowercase and add it to the list
				words.add(word.toLowerCase());
			}
		}

		// Build a list of word pairs
		for (int loop = 1; loop < words.size(); loop++) {
			output.collect(
					new TextPairWritable(words.get(loop - 1), words.get(loop)),
					new LongWritable(1));
		}
	}
}
