#!/bin/sh

java -Xmx4g -cp nerdle-core-0.0.1-SNAPSHOT-withDependencies.jar de.textmining.nerdle.evaluation.Controller $1 $2 > eval_$(date +"%Y-%m-%d-%H%M%S")