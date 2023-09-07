while true
do
  echo "Paste your text and press CTRL+d when you finish"
  sentence=$(cat)
  python3 py/split_sentences.py "$sentence" | xclip
  echo "Split copied to clipboard"
done