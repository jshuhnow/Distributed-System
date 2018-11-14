hdfs dfs -mkdir /input
hdfs dfs -copyFromLocal data/ratings.csv /input
hdfs dfs -rm -r -f /output

hadoop jar averageRating.jar com.jaeseok.AverageRating /input/ratings.csv /output/
