package com.timmattison.hadoop.examples.writables.wordcooccurrence;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class WordCooccurrenceReducer extends MapReduceBase implements
		Reducer<TextPairWritable, LongWritable, Text, LongWritable> {

	@Override
	public void reduce(TextPairWritable key, Iterator<LongWritable> values,
			OutputCollector<Text, LongWritable> output, Reporter reporter)
			throws IOException {

		// Initialize our word count to zero
		int wordCount = 0;

		// Do we have any more IntWritable values for this TextPairWritable key?
		while (values.hasNext()) {
			// Get the IntWritable value
			LongWritable value = values.next();

			/**
			 * NOTE: You cannot loop through the keys and just add one and
			 * ignore the IntWritable value. This is because a combiner could
			 * run before this reducer or this class might be used in a
			 * different job that doesn't behave as our WordMapper class does.
			 * From the WordMapper class we expect that our values are all going
			 * to be the number "1" but making this assumption is bad practice
			 * and will break in more advanced scenarios or when your code gets
			 * reused. Never depend on that kind of behavior. Always use the
			 * values you are given.
			 */

			// Add it to the word count
			wordCount += value.get();
		}

		// Output the Text key (the word pair) with the calculated word count
		output.collect(key.toText(), new LongWritable(wordCount));
	}
}
