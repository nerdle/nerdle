#!/bin/sh

time java -Xmx4g -cp target/nerdle-core-0.0.1-SNAPSHOT-withDependencies.jar de.textmining.nerdle.evaluation.Controller $1 $2 $3 $4 > eval_$(date +"%Y-%m-%d-%H%M%S")