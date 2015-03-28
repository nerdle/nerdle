#!/bin/sh

java -Xmx4g -cp $1 de.textmining.nerdle.utils.CSVExporter $2 $3
