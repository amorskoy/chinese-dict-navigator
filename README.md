# chinese-dict-navigator
Tool to help me with dictionary tabs during "by-part" kind of chinese translation 

## Install
Clone ths repo
Then fetch assets
```bash
    # Fill 'data' and 'deps' dirs
    git lfs pull
    sbt assembly
```

## Usage
### Translator
```shell
./run_segmenter.sh
```

And then paste your text.

It opens:
 * one google translate for the whole sentence
 * one yabla tab for the whole sentence
 * four BKRS browser tabs, one for each of comma-separated parts
 
### Sentence splitter
```shell
./run_splitter.sh
```
And then paste your text, interactive.

It will add newline to each sentence separator and copy output into clipboard using `xclip`