# Spell-Checker


## Project Description
This program reads a text file, checks words against a dictionary using either [Binary Search](https://www.geeksforgeeks.org/binary-search/) or a [Trie-based implementation](https://www.geeksforgeeks.org/introduction-to-trie-data-structure-and-algorithm-tutorials/?ref=header_outind), and uses [Levenshtein Distance](https://en.wikipedia.org/wiki/Levenshtein_distance)
 to suggest corrections for misspelled words. Designed as a command-line application, it is flexible, allowing dynamic dictionary loading and input files.

## 📂 Dataset Information

- **Dictionary**: A text file (`dictionary.txt`) containing valid words, one per line.
- **Input Text**: The program processes a text file (`testfile.txt`) line by line to identify and correct misspellings.


## 🔧 Implementations Used

### Binary Search
- **Purpose**: Quickly determines whether a word exists in the dictionary by dividing the search space in half at each step.
- **Efficiency**: Best suited for small to medium-sized dictionaries.
```java
private static boolean binarySearch(ArrayList<String> list, String key) {
    int low = 0, high = list.size() - 1;

    while (low <= high) {
        int mid = low + (high - low) / 2;
        int cmp = list.get(mid).compareTo(key);

        if (cmp < 0) {
            low = mid + 1;
        } else if (cmp > 0) {
            high = mid - 1;
        } else {
            return true; // Found the word
        }
    }
    return false; // Word not found
}
```

### Trie-Based Search
- **Purpose**: Uses a **Ternary Search Trie (TST)** to store dictionary words, allowing efficient lookups.
- **Efficiency**: Optimized for large dictionaries, where shared prefixes reduce memory usage and improve lookup times.
```java
  private class Node {
    char c;                   // Character at this node
    Node left, mid, right;    // Links to child nodes
    boolean end;              // Flag for end of a word
}

private Node add(Node x, String s, int d) {
    char c = s.charAt(d);
    if (x == null) x = new Node(c);

    if (c < x.c) {
        x.left = add(x.left, s, d); // Add to the left
    } else if (c > x.c) {
        x.right = add(x.right, s, d); // Add to the right
    } else if (d < s.length() - 1) {
        x.mid = add(x.mid,
```

### Levenshtein Distance
- **Purpose**: Suggests the closest matching words for misspellings by calculating the minimum number of edits required to transform one word into another.
```java
private static int editDistance(String word1, String word2) {
    int len1 = word1.length(), len2 = word2.length();
    int[][] dp = new int[len1 + 1][len2 + 1];

    for (int i = 0; i <= len1; i++) dp[i][0] = i;
    for (int j = 0; j <= len2; j++) dp[0][j] = j;

    for (int i = 1; i <= len1; i++) {
        for (int j = 1; j <= len2; j++) {
            if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                dp[i][j] = dp[i - 1][j - 1];
            } else {
                dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
            }
        }
    }
    return dp[len1][len2];
}


```

## Getting started

### Installation

Required files:
- **`dictionary.txt`**: The word list to compare against.
- **`testfile.txt`**: The text file to check for misspellings.

Steps to run the program:
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/spellchecker.git
   
   ```
2. Compile and run the binary search implementation:
   `SpellChecker.java`
3. Alternatively, run the trie-based implementation:
   `trieSearch.java`

## 📈 Example use
Input(dictionary.txt)
```
this
is
a
sample
text
with
some
misspelled
words
```

Input(testfile.txt)
```
Thiss is a sampl text with som mispelled words.
```

Output
```
Misspelled word: 'Thiss' at line 1
Suggestions: This, Thus
Misspelled word: 'sampl' at line 1
Suggestions: sample
Misspelled word: 'som' at line 1
Suggestions: some
Misspelled word: 'mispelled' at line 1
Suggestions: misspelled
```
