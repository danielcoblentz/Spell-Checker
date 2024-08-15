import java.util.*;
import java.io.*;

public class miniProject {

    public static void main(String[] args) {
        String dictPath = "dictionary.txt"; // Paths to the dictionary and file to be spell-checked.
        String filePath = "testfile.txt";
        ArrayList<String> dictionary = new ArrayList<>(); // ArrayList to hold the dictionary words for access

        // Load the dictionary from the file
        try (Scanner scanner = new Scanner(new File(dictPath))) {
            while (scanner.hasNext()) {
                dictionary.add(scanner.next().toLowerCase()); // converts to lowercase characters for case sensitivity
            }
        } catch (FileNotFoundException e) {
            System.out.println("Dictionary file not found.");
            System.exit(1);
        }

        // Process the file to be checked for spelling errors.
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0; // Line counter to track the line number of potential spelling errors

            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                lineCount++;
                String[] words = line.split("\\W+"); // Split line into individual words using non-word characters

                // Check each word against the dictionary.
                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
                    if (!word.isEmpty() && !binarySearch(dictionary, word.toLowerCase())) {
                        System.out.println("Misspelled word '" + word + "' found at line " + lineCount);
                        if (word.matches("[a-zA-Z]+")) {
                            suggestSimilarWords(dictionary, word);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
            System.exit(1);
        }
    }

    // Binary search method to check if a word exists in the dictionary.
    public static boolean binarySearch(ArrayList<String> list, String key) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            String midVal = list.get(mid);
            int cmp = midVal.compareTo(key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return true;
            }
        }
        return false;
    }

    // Method to calculate the minimum number of single-character edits required to change one word into another
    public static int editDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) dp[i][0] = i;
        for (int j = 0; j <= len2; j++) dp[0][j] = j;

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int substitute = dp[i - 1][j - 1] + 1;
                    int insert = dp[i][j - 1] + 1;
                    int delete = dp[i - 1][j] + 1;
                    dp[i][j] = Math.min(substitute, Math.min(insert, delete));
                }
            }
        }
        return dp[len1][len2];
    }

    // Method to suggest the most similar words from the dictionary
    public static void suggestSimilarWords(ArrayList<String> dictionary, String misspelled) {
        final int MAX_SUGGESTIONS = 3; // Limit to a few close suggestions
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
        final int MAX_DISTANCE = 3; // Adjust this threshold as needed
    
        for (String word : dictionary) {
            int dist = editDistance(misspelled.toLowerCase(), word);
            if (dist <= MAX_DISTANCE) { // Only consider words within the max distance
                pq.offer(new AbstractMap.SimpleEntry<>(word, dist));
            }
        }
    
        if (pq.isEmpty()) {
            System.out.println("No close matches found.");
        } else {
            System.out.print("Did you mean: ");
            for (int i = 0; i < MAX_SUGGESTIONS && !pq.isEmpty(); i++) {
                System.out.print(pq.poll().getKey() + (i < MAX_SUGGESTIONS - 1 && !pq.isEmpty() ? ", " : "? "));
            }
            System.out.println();
        }
    }
    
}
