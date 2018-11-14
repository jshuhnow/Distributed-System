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

public class AverageRating {

  public static class TokenizerMapper
		extends Mapper<Object, Text, Text, PairWritable>{

    private final static IntWritable one = new IntWritable(1);
	private Text userID = new Text();
	private Text movieID = new Text();
	private PairWritable<DoubleWritable, IntWritable> pairRatingTimestamp = new PairWritable<DoubleWritable, IntWritable>();

	public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
	  
	  userID.set(itr.nextToken());
	  itr.nextToken();
	  pairRatingTimestamp.setFirst(new DoubleWritable(Double.valueOf(itr.nextToken())));
	  pairRatingTimestamp.setSecond(one);
	  itr.nextToken();
      context.write(userID, pairRatingTimestamp);
    }
  }

  public static class IntSumReducer
   extends Reducer<Text,PairWritable<DoubleWritable, IntWritable>, Text, DoubleWritable> {
	private DoubleWritable avg = new DoubleWritable();

	public void reduce(Text key, Iterable<PairWritable<DoubleWritable, IntWritable>> values, Context context) throws IOException, InterruptedException {
      
	  double sum = 0.0;
	  int cnt = 0;
      for (PairWritable<DoubleWritable, IntWritable> pairRatingTimestamp : values) {
        sum += pairRatingTimestamp.getFirst().get();
		cnt += pairRatingTimestamp.getSecond().get();
      }
      avg.set(sum / cnt);
      context.write(key, avg);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Average Counting");
    job.setJarByClass(AverageRating.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(DoubleWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
