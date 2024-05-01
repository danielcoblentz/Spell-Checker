import java.util.*;
import java.io.*;

public class TrieSearch{
    private static TSTNode root;


     
    static class TSTNode {
        char data; // The character this node represents
        boolean isEndOfString; // Flag to check if this node marks the end of a word
        TSTNode left, middle, right; // Pointers to left, middle, and right child nodes

       
        public TSTNode(char data) {
            this.data = data;
            this.isEndOfString = false;
            this.left = null;
            this.middle = null;
            this.right = null;
        }
    }

    public static void main(String[] args) {
        String dictPath = "dictionary.txt"; // Path to the dictionary file
        String filePath = "testfile.txt"; // Path to the file to be spell-checked
        root = null;

        // Load words from the dictionary into the TST
        try (Scanner scanner = new Scanner(new File(dictPath))) {
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase(); // Convert words to lowercase for uniformity
                insert(word);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Dictionary file not found.");
            System.exit(1);
        }

        // Check the spelling in the file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                lineCount++;
                String[] words = line.split("\\W+"); // Split the line into words using non-word delimiters

                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
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

    // Method to insert a word into the TST
    public static void insert(String word) {
        root = insertRec(root, word, 0);
    }

    // Recursive method to insert a character of the word into the TST
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

    // Method to search for a word in the TST
    public static boolean search(TSTNode current, String word) {
        return searchRec(current, word, 0);
    }

    // Recursive method to search for a character of the word in the TST
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
