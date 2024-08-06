# mini-term project CS 202 spell checker
Trie Search Spell Checker
The Trie Search Spell Checker is a Java application that reads a dictionary file and checks the spelling of words in a document file using a ternary search trie (TST) data structure. This project demonstrates efficient word storage and retrieval using TSTs.

Features
TST-based Dictionary: Loads words into a ternary search trie for efficient storage and lookup.
Spell Checking: Reads a document and checks each word against the dictionary stored in the TST.
Misspelled Word Reporting: Identifies and reports misspelled words, including the line number where they occur.
How It Works
Load the Dictionary: Words from dictionary.txt are loaded into a ternary search trie, converting each word to lowercase for case-insensitive matching.

Process the Document: The program reads testfile.txt line by line, converting each line to lowercase and splitting it into words using non-word characters as delimiters.

Trie Search: For each word, the program checks if it exists in the TST. If a word is not found, it is reported as misspelled with its line number.

