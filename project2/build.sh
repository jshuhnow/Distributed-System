mkdir bin 2>/dev/null
hadoop com.sun.tools.javac.Main src/*.java -d bin/
jar cf bin/averageRating.jar bin/com/jaeseok/*.class



