import java.util.*;
import java.io.*;

class TSTNode {
    char data;
    boolean isEndOfString;
    TSTNode left, middle, right;

    TSTNode(char data) {
        this.data = data;
        this.isEndOfString = false;
        this.left = this.middle = this.right = null;
    }
}

class TernarySearchTrie {
    private TSTNode root;
    private List<String> allWords = new ArrayList<>();

    void insert(String word) {
        root = insert(root, word.toCharArray(), 0);
        if (!allWords.contains(word)) {
            allWords.add(word);
        }
    }

    private TSTNode insert(TSTNode node, char[] word, int index) {
        if (node == null) {
            node = new TSTNode(word[index]);
        }

        if (word[index] < node.data) {
            node.left = insert(node.left, word, index);
        } else if (word[index] > node.data) {
            node.right = insert(node.right, word, index);
        } else {
            if (index + 1 < word.length) {
                node.middle = insert(node.middle, word, index + 1);
            } else {
                node.isEndOfString = true;
            }
        }
        return node;
    }

    boolean search(String word) {
        return search(root, word.toCharArray(), 0);
    }

    private boolean search(TSTNode node, char[] word, int index) {
        if (node == null) return false;

        if (word[index] < node.data) {
            return search(node.left, word, index);
        } else if (word[index] > node.data) {
            return search(node.right, word, index);
        } else {
            if (index == word.length - 1) {
                return node.isEndOfString;
            }
            return search(node.middle, word, index + 1);
        }
    }

    // Get suggestions for a misspelled word
    List<String> getSuggestions(String word) {
        Map<Integer, List<String>> distances = new TreeMap<>();
        for (String w : allWords) {
            int dist = levenshteinDistance(word, w);
            distances.putIfAbsent(dist, new ArrayList<>());
            distances.get(dist).add(w);
        }
        return distances.get(Collections.min(distances.keySet()));
    }

    // Levenshtein distance calculation
    private int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(
                        dp[i - 1][j - 1] + costOfSubstitution(a.charAt(i - 1), b.charAt(j - 1)),
                        dp[i - 1][j] + 1,
                        dp[i][j - 1] + 1
                    );
                }
            }
        }
        return dp[a.length()][b.length()];
    }

    private int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    private int min(int... numbers) {
        return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }
}

public class SpellCheckerWithTST {

    public static void main(String[] args) {
        String dictPath = "dictionary.txt";
        String filePath = "testfile.txt";
        TernarySearchTrie tst = new TernarySearchTrie();

        try (Scanner scanner = new Scanner(new File(dictPath))) {
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase();
                tst.insert(word);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Dictionary file not found.");
            System.exit(1);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                lineCount++;
                String[] words = line.split("\\W+");

                for (String word : words) {
                    if (!word.isEmpty() && !isNumeric(word) && !tst.search(word.toLowerCase())) {
                        System.out.println("Misspelled word '" + word + "' found at line " + lineCount);
                        List<String> suggestions = tst.getSuggestions(word.toLowerCase());
                        System.out.println("Did you mean: " + suggestions);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
            System.exit(1);
        }
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
