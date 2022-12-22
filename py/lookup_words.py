import json
import sys
from abc import abstractmethod
from typing import Dict


class DictionaryDB:
    @abstractmethod
    def resolve(self, sample: str) -> str:
        pass

    @property
    def max_lookup_len(self) -> int:
        return 4


class FileDictionary(DictionaryDB):
    def __init__(self) -> None:
        super().__init__()
        self.dictionary: Dict[str, str] = self.load_dictionary()

    def load_dictionary(self) -> Dict[str, str]:
        return json.load(open("py/dictionary.json", 'rt'))

    def resolve(self, sample: str) -> str:
        pos = 0
        sample_len = len(sample)
        out = []

        while pos < sample_len:
            for i in reversed(range(self.max_lookup_len)):
                num_search = i + 1
                search_str = sample[pos:pos+num_search]
                res = self.dictionary[search_str] if search_str in self.dictionary else None
                if res:
                    out.append(" " + res + " ")
                    pos = pos + num_search
                    break

            # if not found = preserve original symbol
            out.append(sample[pos])
            pos = pos + 1

        return "".join(out)


if __name__ == '__main__':
    input = sys.argv[1]
    dictionary = FileDictionary()
    res = dictionary.resolve(input.replace(" ", ""))

    print(res)
