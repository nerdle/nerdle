#!/bin/sh

java -Xmx4g -cp $1 de.textmining.nerdle.evaluation.Controller $2 $3 $4 $5 > eval_$(date +"%Y-%m-%d-%H%M%S")
