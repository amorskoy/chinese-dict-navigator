#!/usr/bin/env bash

SENTENCE=$1

#Navigate Browser
java -jar ./dist/chinese-dict-navigator.jar "$SENTENCE"

#Segment with Stanford Segmenter
./analyze.sh "$SENTENCE" 2>/dev/null