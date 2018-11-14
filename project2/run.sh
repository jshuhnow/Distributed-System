hdfs dfs -mkdir /input
hdfs dfs -copyFromLocal data/ratings.csv /input
cd bin
hadoop jar averageRating.jar AverageRating /input/ratings.csv /output/ratings.csv
cd ..
