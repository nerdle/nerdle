#!/bin/sh

java -Xmx4g -cp target/nerdle-core-0.0.1-SNAPSHOT-withDependencies.jar de.textmining.nerdle.utils.H2Importer /Users/umar/Documents/projects/data/nerdle-oie-filtered/simpsons.json /Users/umar/Documents/projects/data/nerdle-db/simpsons full

java -Xmx4g -cp target/nerdle-core-0.0.1-SNAPSHOT-withDependencies.jar de.textmining.nerdle.utils.H2Importer /Users/umar/Documents/projects/data/nerdle-oie-filtered/star-trek.json /Users/umar/Documents/projects/data/nerdle-db/star-trek full

java -Xmx4g -cp target/nerdle-core-0.0.1-SNAPSHOT-withDependencies.jar de.textmining.nerdle.utils.H2Importer /Users/umar/Documents/projects/data/nerdle-oie-filtered/star-wars.json /Users/umar/Documents/projects/data/nerdle-db/star-wars full

java -Xmx4g -cp target/nerdle-core-0.0.1-SNAPSHOT-withDependencies.jar de.textmining.nerdle.utils.H2Importer /Users/umar/Documents/projects/data/nerdle-oie-filtered/simpsons.json /Users/umar/Documents/projects/data/nerdle-db/simpsons-eval eval

java -Xmx4g -cp target/nerdle-core-0.0.1-SNAPSHOT-withDependencies.jar de.textmining.nerdle.utils.H2Importer /Users/umar/Documents/projects/data/nerdle-oie-filtered/star-trek.json /Users/umar/Documents/projects/data/nerdle-db/star-trek-eval eval

java -Xmx4g -cp target/nerdle-core-0.0.1-SNAPSHOT-withDependencies.jar de.textmining.nerdle.utils.H2Importer /Users/umar/Documents/projects/data/nerdle-oie-filtered/star-wars.json /Users/umar/Documents/projects/data/nerdle-db/star-wars-eval eval
