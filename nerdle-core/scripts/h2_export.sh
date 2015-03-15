#!/bin/sh

java -Xmx4g -cp target/nerdle-core-0.0.1-SNAPSHOT-withDependencies.jar de.textmining.nerdle.utils.H2Exporter /Users/umar/Documents/projects/data/nerdle-oie/simpsons /Users/umar/Documents/projects/data/nerdle-db/simpsons

java -Xmx4g -cp target/nerdle-core-0.0.1-SNAPSHOT-withDependencies.jar de.textmining.nerdle.utils.H2Exporter /Users/umar/Documents/projects/data/nerdle-oie/star-trek /Users/umar/Documents/projects/data/nerdle-db/star-trek

java -Xmx4g -cp target/nerdle-core-0.0.1-SNAPSHOT-withDependencies.jar de.textmining.nerdle.utils.H2Exporter /Users/umar/Documents/projects/data/nerdle-oie/wookieepedia /Users/umar/Documents/projects/data/nerdle-db/star-wars
