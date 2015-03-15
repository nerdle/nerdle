#!/bin/sh

java -Xmx4g -cp target/nerdle-core-0.0.1-SNAPSHOT-withDependencies.jar de.textmining.nerdle.utils.H2Importer /Users/umar/Documents/projects/data/nerdle-csv/simpsons /Users/umar/Documents/projects/data/nerdle-db/simpsons

java -Xmx4g -cp target/nerdle-core-0.0.1-SNAPSHOT-withDependencies.jar de.textmining.nerdle.utils.H2Importer /Users/umar/Documents/projects/data/nerdle-csv/star-trek /Users/umar/Documents/projects/data/nerdle-db/star-trek

java -Xmx4g -cp target/nerdle-core-0.0.1-SNAPSHOT-withDependencies.jar de.textmining.nerdle.utils.H2Importer /Users/umar/Documents/projects/data/nerdle-csv/star-wars /Users/umar/Documents/projects/data/nerdle-db/star-wars
