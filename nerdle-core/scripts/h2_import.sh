#!/bin/sh

java -Xmx6g -cp $1 de.textmining.nerdle.utils.H2Importer $2 $3 $4
