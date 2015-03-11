#!/bin/sh

#java -Xmx4g -cp target/nerdle-core-0.0.1-SNAPSHOT-withDependencies.jar de.textmining.nerdle.utils.CSVExporter /Users/umar/Documents/projects/data/nerdle-oie/simpsons /Users/umar/Documents/projects/data/nerdle-csv simpsons

java -Xmx4g -cp target/nerdle-core-0.0.1-SNAPSHOT-withDependencies.jar de.textmining.nerdle.utils.CSVExporter /Users/umar/Documents/projects/data/nerdle-oie/star-trek /Users/umar/Documents/projects/data/nerdle-csv star-trek

java -Xmx4g -cp target/nerdle-core-0.0.1-SNAPSHOT-withDependencies.jar de.textmining.nerdle.utils.CSVExporter /Users/umar/Documents/projects/data/nerdle-oie/wookieepedia /Users/umar/Documents/projects/data/nerdle-csv star-wars