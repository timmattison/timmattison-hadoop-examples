package com.timmattison.hadoop.examples.basic.httplogipcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class HttpLogIpCountMapper extends MapReduceBase implements
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
		 * The value is the entire web server record so we split it on spaces
		 * and extract the first value. This value should be the IP address.
		 */
		String[] tokens = value.toString().split(" ");
		String ip = tokens[0];

		/**
		 * Output the IP as a Text key and the number one as an IntWritable
		 * value
		 */
		output.collect(new Text(ip), new IntWritable(1));
	}
}
