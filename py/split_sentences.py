import os
import re
import subprocess
import sys
from typing import List


class SplitSentences:
    @property
    def sentence_separators(self):
        return ["。", "!", "?"]

    def split(self, text: str) -> str:
        """Return text with added newlines after sentence separators"""

        separators = "".join(self.sentence_separators)
        regexp = re.compile(f"([{separators}])")

        return re.sub(regexp, r"\1\n", text)

if __name__ == '__main__':
    text = sys.argv[1]
    splitter = SplitSentences()
    splitted = splitter.split(text)

    print(splitted)