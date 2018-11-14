
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.DataInput;
import java.io.DataOutput;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public class AverageRating {

  public static class TokenizerMapper
		extends Mapper<Object, Text, Text, PairWritable>{

    private final static IntWritable one = new IntWritable(1);
	private Text userID = new Text();
	private Text movieID = new Text();
	
	public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString(),",");
	  
	  userID.set(itr.nextToken());

	  // Ignore the first line
	  if (userID.toString().equals("userId")) {
	      itr.nextToken();
		  itr.nextToken();
		  itr.nextToken();
		  return;
	  }
	  itr.nextToken();
	  
	  DoubleWritable rating = new DoubleWritable(Double.valueOf(itr.nextToken()));
	  PairWritable pairRatingTimestamp = new PairWritable(rating, one);
	  itr.nextToken();

	  context.write(userID, pairRatingTimestamp);
    }
  }

  public static class IntSumReducer
   extends Reducer<Text,PairWritable, Text, DoubleWritable> {
	private DoubleWritable avg = new DoubleWritable();

	public void reduce(Text key, Iterable<PairWritable> values, Context context) throws IOException, InterruptedException {
      
	  double sum = 0.0;
	  int cnt = 0;
      for (PairWritable pairRatingTimestamp : values) {
        sum += pairRatingTimestamp.getFirst().get();
		cnt += pairRatingTimestamp.getSecond().get();
      }
      avg.set(sum / cnt);
      context.write(key, avg);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "AverageMapping");
    job.setJarByClass(AverageRating.class);
    job.setMapperClass(TokenizerMapper.class);

    //job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);

	job.setMapOutputKeyClass(Text.class);
	job.setMapOutputValueClass(PairWritable.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(DoubleWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
