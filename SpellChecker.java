import java.util.*;
import java.io.*;

public class SpellChecker {

    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);

        // ask the user for dictionary and input file paths
        System.out.println("plz enter the path to the dictionary file:");
        String dictPath = userInput.nextLine();
        System.out.println("enter the path to the file to check for spelling errors:"); // enter the path directly to rea contents from
        String filePath = userInput.nextLine();

        ArrayList<String> dictionary = new ArrayList<>();

        // load input dictionary
        if (!loadDictionary(dictPath, dictionary)) {
            System.out.println("error, unable to load dictionary");
            return;
        }

        // process the input file
        processFile(filePath, dictionary);
    }



    private static boolean loadDictionary(String dictPath, ArrayList<String> dictionary) { // valdiate dictionary path, and sort the file
        try (Scanner scanner = new Scanner(new File(dictPath))) {
            while (scanner.hasNext()) { // read each word from the dict
                dictionary.add(scanner.next().toLowerCase());
            }
            Collections.sort(dictionary); // sort  the dictionary for binary search
            System.out.println("dictionary loaded successfully with " + dictionary.size() + " words."); //output message to confrim or raise an error for the input dict
            return true; // file found
        } catch (FileNotFoundException e) {
            System.out.println("dictionary file not found at " + dictPath);
            return false; // return False file not found
        }
    }


// process the input file and check for spelling errors
    private static void processFile(String filePath, ArrayList<String> dictionary) { // process the file and mark any spelling errors in the input file to the command line interface for user to view
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0;
            List<String> misspelledWords = new ArrayList<>();

            System.out.println("\nchecking file for spelling errors...\n");
            while ((line = reader.readLine()) != null) {
                lineCount++;
                String[] words = line.split("\\W+");

                for (String word : words) {
                    if (!word.isEmpty() && !binarySearch(dictionary, word.toLowerCase())) {
                        System.out.println("Misspelled word: '" + word + "' at line " + lineCount);
                        misspelledWords.add(word);

                        //suggest similar words
                        suggestSimilarWords(dictionary, word);
                    }
                }
            }



//summary of reuslts after processing file
            if (misspelledWords.isEmpty()) {
                System.out.println("\nno spelling errors found!");
            } else {
                System.out.println("\nsummary of Misspelled Words:"); // return all marked words form hte method processFile
                System.out.println(String.join(", ", misspelledWords));
            }
        } catch (FileNotFoundException e) {                     //debugging with try-catch blocks
            System.out.println(" file not found at " + filePath);
        } catch (IOException e) {
            System.out.println("Error cannot read file at: " + filePath);
        }
    }


//Binary Search algorithm (checking if a word exists in the dict)
    private static boolean binarySearch(ArrayList<String> list, String key) { // main binarySearch method to run on hte input file after processing
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = list.get(mid).compareTo(key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return true; // match found
            }
        }
        return false; // word not found
    }


    //extra credit option -- editDiastacne method (Levenshtein distance algorithm) reccomended
    private static int editDistance(String word1, String word2) {
        int len1 = word1.length(), len2 = word2.length();
        int[][] dp = new int[len1 + 1][len2 + 1]; // DP table for sorting intermediate results


        //base cases
        for (int i = 0; i <= len1; i++) dp[i][0] = i;
        for (int j = 0; j <= len2; j++) dp[0][j] = j;

        //fill DP table
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // no edit needed
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1; // get the miniumum of hte three operands
                }
            }
        }
        return dp[len1][len2]; // edit distance
    }


//method to suggest similar words for a misspeleld word
    private static void suggestSimilarWords(ArrayList<String> dictionary, String misspelled) { // implement a way to sugggest similar words within a certain edit distance 1-3, 2 seems ot be the most accurate
        final int MAX_SUGGESTIONS = 3; // num of suggestions if a word is spelled incorrectly
        final int MAX_DISTANCE = 3; // edit distance (1-3) works best

        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
//calc edit distance for each wordin the dictionary
        for (String word : dictionary) {
            int dist = editDistance(misspelled.toLowerCase(), word);
            if (dist <= MAX_DISTANCE) {
                pq.offer(new AbstractMap.SimpleEntry<>(word, dist)); //add close matches to the priority queue
            }
        }


    //dsipaly suggestions to user in the command line after running program
        if (pq.isEmpty()) {
            System.out.println("no close matches found for '" + misspelled + "'.");
        } else {
            System.out.print("suggestions: ");
            for (int i = 0; i < MAX_SUGGESTIONS && !pq.isEmpty(); i++) {
                System.out.print(pq.poll().getKey() + (i < MAX_SUGGESTIONS - 1 ? ", " : ""));
            }
            System.out.println();
        }
    }
}
