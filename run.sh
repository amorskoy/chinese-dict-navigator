#!/usr/bin/env bash

SENTENCE=$1

#Navigate Browser
java -cp dist/chinese-dict-navigator.jar org.bitheaven.NavigateDicts "$SENTENCE"

#Segment with Stanford Segmenter
./analyze.sh "$SENTENCE" 2>/dev/null