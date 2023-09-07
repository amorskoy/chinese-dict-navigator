while true
do
  read sentence
  python3 py/split_sentences.py "$sentence" | xclip
done