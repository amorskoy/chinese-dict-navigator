while true
do
  echo "Paste your text and press CTRL+d when you finish. To exit press CTRL+c"
  sentence=$(cat)
  python3 py/split_sentences.py "$sentence" | grep -vP "^[\n]*$" | sed "s/ //g" | xclip
  echo "Split copied to clipboard"
done