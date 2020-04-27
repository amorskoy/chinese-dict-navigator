TEXT="$1"
TEXT_FILE=`mktemp`
SEG_FILE=`mktemp`
SEG2_FILE=`mktemp`
OUT_FILE=`mktemp`

BASE="/opt/Chineese"
PARSER="$BASE/stanford-corenlp-full-2020-04-20/stanford-corenlp-4.0.0.jar"
MODELS="$BASE/stanford-corenlp-4.0.0-models-chinese.jar"
SEGMENTER="$BASE/stanford-segmenter-2018-10-16"

MOD_BASE="edu/stanford/nlp/models/lexparser"
#MOD="chineseFactored.ser.gz"
MOD="xinhuaFactoredSegmenting.ser.gz"

echo $TEXT > $TEXT_FILE
$SEGMENTER/segment.sh ctb $TEXT_FILE UTF-8 0 > $SEG_FILE
$SEGMENTER/segment.sh pku $TEXT_FILE UTF-8 0 > $SEG2_FILE



#java \
#-mx500m \
#-cp $PARSER:$MODELS \
#edu.stanford.nlp.parser.lexparser.LexicalizedParser \
#-encoding utf-8 \
#-outputFormat typedDependencies \
#$MOD_BASE/$MOD \
#$TEXT_FILE > $OUT_FILE

echo "SEGMENTED CTB:"
cat $SEG_FILE

echo "SEGMENTED PKU:"
cat $SEG2_FILE

#echo "ANALYZED:"
#cat $OUT_FILE

rm $SEG_FILE
rm $SEG2_FILE
rm $TEXT_FILE
rm $OUT_FILE