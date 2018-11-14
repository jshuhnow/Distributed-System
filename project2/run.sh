hdfs dfs -mkdir /input
hdfs dfs -copyFromLocal data/ratings.csv /input
hadoop jar bin/averageRating.jar com.jaeseok.AverageRating /input/ratings.csv /output/ratings.csv
