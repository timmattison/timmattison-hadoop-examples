package com.timmattison.hadoop.examples.counters.httplogimages;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class HttpLogImagesMapper extends MapReduceBase implements
		Mapper<LongWritable, Text, Text, IntWritable> {
	/*
	 * Input line looks like: 24.15.0.12 - - [1/Apr/2011:05:21:14 -0500]
	 * "GET /image.jpg HTTP/1.1" 200 81922 "http://timmattison.com"
	 * "Firefox/3.3.1"
	 */
	@Override
	public void map(LongWritable key, Text value,
			OutputCollector<Text, IntWritable> output, Reporter reporter)
			throws IOException {
		/**
		 * The value is the entire web server record so we split it on
		 * double-quotes and extract the first value. This value should be the
		 * actual GET/POST request info.
		 */
		String[] tokens = value.toString().split("\"");

		// Get the request information and convert it to lowercase so that
		String request = tokens[1].toLowerCase();

		/**
		 * NOTE: This is not 100% accurate since URLs that contain these strings
		 * as substrings will come up as matches
		 */

		// What kind of file is in the request?
		if (request.contains(".jpg ")) {
			// It's a JPG file, increment the JPG counter
			reporter.incrCounter(HttpLogImagesDriver.IMAGE_COUNTER_GROUP,
					HttpLogImagesDriver.IMAGE_COUNTER_JPG, 1);
		} else if (request.contains(".gif ")) {
			// It's a GIF file, increment the GIF counter
			reporter.incrCounter(HttpLogImagesDriver.IMAGE_COUNTER_GROUP,
					HttpLogImagesDriver.IMAGE_COUNTER_GIF, 1);
		} else if (request.contains(".png ")) {
			// It's a PNG file, increment the PNG counter
			reporter.incrCounter(HttpLogImagesDriver.IMAGE_COUNTER_GROUP,
					HttpLogImagesDriver.IMAGE_COUNTER_PNG, 1);
		} else {
			// It's some other kind of file, increment the other counter
			reporter.incrCounter(HttpLogImagesDriver.IMAGE_COUNTER_GROUP,
					HttpLogImagesDriver.IMAGE_COUNTER_OTHER, 1);
		}
	}
}
