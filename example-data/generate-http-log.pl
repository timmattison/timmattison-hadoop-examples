#!/usr/bin/perl -w

my $output_filename = "../example-data/http.log";

if(-f $output_filename) {
  print "$output_filename already exists, nothing to do\n";
  exit;
}

my $iterations = 10000000;

# Example output record looks like this:
# 24.15.0.12 - - [1/Apr/2011:05:21:14 -0500] "GET /image.jpg HTTP/1.1" 200 81922 "http://timmattison.com" "Firefox/3.3.1"

# We don't care about most of the fields so I leave them canned for now, we only care about generating random GET requests
#   to exercise our JPG, GIF, PNG, and "other" counting code in Hadoop, random IPs to test our IP counting code, and
#   random request sizes to keep things interesting

open OUTPUT_FILE, ">$output_filename";

for(my $loop = 0; $loop < $iterations; $loop++) {
  my $ip = (int(rand(254)) + 1) . "." . (int(rand(254)) + 1) . "." . (int(rand(254)) + 1) . "." . (int(rand(254)) + 1);

  my $random_file = int(rand(4));

  my $file = "boring_file";

  if($random_file == 0) {
    $file = "image.gif";
  }
  elsif($random_file == 1) {
    $file = "image.png";
  }
  elsif($random_file == 2) {
    $file = "image.jpg";
  }

  my $random_size = int(rand(999999));

  print OUTPUT_FILE "$ip - - [1/Apr/2011:05:21:14 -0500] \"GET /$file HTTP/1.1\" 200 $random_size \"http://timmattison.com\" \"Firefox/3.3.1\"\n";
}

close OUTPUT_FILE;
