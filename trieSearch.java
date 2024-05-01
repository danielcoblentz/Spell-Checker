import java.util.*;
import java.io.*;

public class trieSearch {
    private static TSTNode root;

    static class TSTNode {
        char data;
        boolean isEndOfString;
        TSTNode left, middle, right;

        public TSTNode(char data) {
            this.data = data;
            this.isEndOfString = false;
            this.left = null;
            this.middle = null;
            this.right = null;
        }
    }

    public static void main(String[] args) {
        String dictPath = "dictionary.txt";
        String filePath = "testingfile.txt";
        root = null;

        // Load the dictionary into the TST
        try (Scanner scanner = new Scanner(new File(dictPath))) {
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase(); // ensure all words are lowercase
                insert(word);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Dictionary file not found.");
            System.exit(1);
        }

        // Read and check each line of the file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                lineCount++;
                String[] words = line.split("\\W+");

                for (String word : words) {
                    if (!word.isEmpty() && !search(root, word.toLowerCase())) {
                        System.out.println("Misspelled word '" + word + "' found at line " + lineCount);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
            System.exit(1);
        }
    }

    public static void insert(String word) {
        root = insertRec(root, word, 0);
    }

    public static TSTNode insertRec(TSTNode current, String word, int index) {
        if (index < word.length()) {
            char ch = word.charAt(index);
            if (current == null) {
                current = new TSTNode(ch);
            }
            if (ch < current.data) {
                current.left = insertRec(current.left, word, index);
            } else if (ch > current.data) {
                current.right = insertRec(current.right, word, index);
            } else {
                if (index + 1 < word.length()) {
                    current.middle = insertRec(current.middle, word, index + 1);
                } else {
                    current.isEndOfString = true;
                }
            }
        }
        return current;
    }

    public static boolean search(TSTNode current, String word) {
        return searchRec(current, word, 0);
    }

    public static boolean searchRec(TSTNode current, String word, int index) {
        if (current == null) return false;
        char ch = word.charAt(index);

        if (ch < current.data) {
            return searchRec(current.left, word, index);
        } else if (ch > current.data) {
            return searchRec(current.right, word, index);
        } else {
            if (index == word.length() - 1) {
                return current.isEndOfString;
            }
            return searchRec(current.middle, word, index + 1);
        }
    }
}
